package org.digitalmind.buildingblocks.core.i18n.service.impl;

import org.digitalmind.buildingblocks.core.i18n.config.I18nConfig;
import org.digitalmind.buildingblocks.core.i18n.config.I18nCoreModuleConfig;
import org.digitalmind.buildingblocks.core.i18n.config.I18nStoreImplementation;
import org.digitalmind.buildingblocks.core.i18n.entity.I18n;
import org.digitalmind.buildingblocks.core.i18n.service.I18nService;
import org.digitalmind.buildingblocks.core.i18n.service.I18nStoreService;
import org.digitalmind.buildingblocks.core.i18n.support.I18nFsTestCacheConfiguration;
import org.digitalmind.buildingblocks.core.i18n.support.I18nFsTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Boots i18n with {@code implementation=FS} and root =
 * classpath {@code /test-samples/i18n}.
 */
@SpringBootTest(classes = {
        I18nFsStoreSpringIT.FsTestApp.class,
        I18nFsTestCacheConfiguration.class
})
class I18nFsStoreSpringIT {

    @DynamicPropertySource
    static void fsRoot(DynamicPropertyRegistry registry) {
        registry.add(
                "application.modules.common.i18n.fs.rootFolder",
                () -> I18nFsTestSupport.samplesRoot().toString()
        );
        registry.add("application.modules.common.i18n.enabled", () -> "true");
        registry.add("application.modules.common.i18n.implementation", () -> "FS");
        registry.add("application.modules.common.i18n.default-locale", () -> "en");
        registry.add("application.modules.common.i18n.api.enabled", () -> "false");
        registry.add("application.modules.common.i18n.message-source.enabled", () -> "false");
    }

    @Autowired
    private I18nStoreService i18nStoreService;

    @Autowired
    private I18nService i18nService;

    @Autowired
    private I18nConfig i18nConfig;

    @Test
    void boots_fsStore_notDb() {
        assertEquals(I18nStoreImplementation.FS, i18nConfig.getImplementation());
        assertNotNull(i18nConfig.getFs().getRootFolder());
        assertTrue(i18nConfig.getFs().getRootFolder().endsWith("test-samples/i18n")
                || i18nConfig.getFs().getRootFolder().endsWith("test-samples\\i18n")
                || i18nConfig.getFs().getRootFolder().contains("test-samples"));
        assertInstanceOf(I18nStoreServiceFSImpl.class, i18nStoreService);
    }

    @Test
    void store_findByKey_defaultNamespace() {
        I18n en = i18nStoreService.findByNamespaceAndCodeAndLocale("default", "hello", "en");
        I18n ro = i18nStoreService.findByNamespaceAndCodeAndLocale("default", "hello", "ro");
        assertEquals("Hello", en.getContent().trim());
        assertEquals("Salut", ro.getContent().trim());
    }

    @Test
    void store_findByKey_nestedCode_template() {
        I18n en = i18nStoreService.findByNamespaceAndCodeAndLocale(
                "template", "esign/sms/welcome", "en"
        );
        assertEquals("Welcome to eSign SMS", en.getContent().trim());
        assertEquals("template", en.getNamespace());
        assertEquals("esign/sms/welcome", en.getCode());
        assertEquals("en", en.getLocale());
    }

    @Test
    void store_findFirst_preferenceList_prefersRoOverEn() {
        I18n hit = i18nStoreService.findFirstByNamespaceAndCodeAndLocales(
                "template", "esign/sms/welcome", List.of("de", "ro", "en")
        );
        assertEquals("ro", hit.getLocale());
        assertEquals("Bun venit la eSign SMS", hit.getContent().trim());
    }

    @Test
    void store_findFirst_enUs_email() {
        I18n hit = i18nStoreService.findFirstByNamespaceAndCodeAndLocales(
                "template", "esign/email/welcome", List.of("en_US", "en")
        );
        assertEquals("en-us", hit.getLocale());
        assertEquals("Welcome email (en-US)", hit.getContent().trim());
    }

    @Test
    void store_getById_stable() {
        I18n row = i18nStoreService.findByNamespaceAndCodeAndLocale("default", "hello", "en");
        I18n byId = i18nStoreService.getById(row.getId());
        assertEquals(row.getContent(), byId.getContent());
        assertEquals(row.getId(), byId.getId());
    }

    @Test
    void service_translate_hit_and_missSentinel() {
        I18n hit = i18nService.translate("template", "esign/sms/welcome", List.of("ro"));
        assertEquals("Bun venit la eSign SMS", hit.getContent().trim());
        assertTrue(hit.getId() != null && hit.getId() != 0L);

        I18n miss = i18nService.translate("template", "missing/key", List.of("en"));
        assertEquals(0L, miss.getId());
        assertEquals("missing/key", miss.getContent());
    }

    @Test
    void service_translate_fallsBackToDefaultLocale() {
        // only en exists for email sample under en-us; asking "fr" then default en should still miss email under en
        I18n missFr = i18nService.translate("template", "esign/email/welcome", "fr");
        // preference includes configured default-locale=en — still no en/ file for email → miss
        assertEquals(0L, missFr.getId());

        I18n hitUs = i18nService.translate("template", "esign/email/welcome", "en_US");
        assertEquals("Welcome email (en-US)", hitUs.getContent().trim());
    }

    @Test
    void store_miss_returnsNull_service_miss_returnsSentinel() {
        assertNull(i18nStoreService.findByNamespaceAndCodeAndLocale("default", "no-such-key", "en"));

        I18n sentinel = i18nService.translate("default", "no-such-key", "en");
        assertEquals(0L, sentinel.getId());
        assertEquals("no-such-key", sentinel.getContent());
        assertEquals("default", sentinel.getNamespace());
    }

    @Test
    void store_localeOverload_readsEnUsSample() {
        I18n hit = i18nStoreService.findByNamespaceAndCodeAndLocale(
                "template", "esign/email/welcome", Locale.US
        );
        assertEquals("en-us", hit.getLocale());
        assertEquals("Welcome email (en-US)", hit.getContent().trim());
    }

    @Test
    void service_translate_codeOnly_usesDefaultNamespace() {
        I18n hit = i18nService.translate("hello", "en");
        assertEquals("Hello", hit.getContent().trim());
        assertEquals("default", hit.getNamespace());
        assertTrue(hit.getId() != null && hit.getId() != 0L);
    }

    @Test
    void service_translate_emptyPreference_usesConfiguredDefaultLocale() {
        I18n hit = i18nService.translate("default", "hello", List.of());
        assertEquals("Hello", hit.getContent().trim());
        assertEquals("en", hit.getLocale());
    }

    @Test
    void service_clearCache_afterStoreOverwrite() {
        String code = "cache/probe-" + System.nanoTime();
        i18nStoreService.save(I18n.builder()
                .namespace("default")
                .code(code)
                .locale("en")
                .content("v1")
                .build());
        assertEquals("v1", i18nService.translate("default", code, "en").getContent().trim());

        i18nStoreService.save(I18n.builder()
                .namespace("default")
                .code(code)
                .locale("en")
                .content("v2")
                .build());
        // still served from cache
        assertEquals("v1", i18nService.translate("default", code, "en").getContent().trim());

        i18nService.clearCache();
        assertEquals("v2", i18nService.translate("default", code, "en").getContent().trim());

        assertEquals(1L, i18nStoreService.deleteByNamespaceAndCodeAndLocale("default", code, "en"));
        i18nService.clearCache();
    }

    @Test
    void store_fallback_skipsMissingLocales_hitsFirstAvailable() {
        // fr + bg missing → de present for status
        I18n hit = i18nStoreService.findFirstByNamespaceAndCodeAndLocales(
                "template", "esign/sms/status", List.of("fr", "bg", "de", "en")
        );
        assertEquals("de", hit.getLocale());
        assertEquals("SMS DE", hit.getContent().trim());
    }

    @Test
    void store_fallback_prefersEarlierLocaleWhenAllPresent() {
        I18n hit = i18nStoreService.findFirstByNamespaceAndCodeAndLocales(
                "template", "esign/sms/status", List.of("ro", "de", "en")
        );
        assertEquals("ro", hit.getLocale());
        assertEquals("SMS RO", hit.getContent().trim());
    }

    @Test
    void service_fallback_onlyEn_whenPreferredLocalesMissing() {
        I18n hit = i18nService.translate(
                "default", "fallback-only-en", List.of("de", "ro", "en")
        );
        assertEquals("en", hit.getLocale());
        assertEquals("Fallback EN only", hit.getContent().trim());
    }

    @Test
    void service_fallback_stringLocale_appendsDefaultLocale() {
        // primary fr missing; default-locale=en has fallback-only-en
        I18n hit = i18nService.translate("default", "fallback-only-en", "fr");
        assertEquals("en", hit.getLocale());
        assertEquals("Fallback EN only", hit.getContent().trim());
    }

    @Test
    void service_fallback_nestedCode_otherNamespace() {
        I18n ro = i18nService.translate("app", "errors/not-found", List.of("it", "ro", "en"));
        assertEquals("ro", ro.getLocale());
        assertEquals("Eroare RO", ro.getContent().trim());

        I18n en = i18nService.translate("app", "errors/not-found", List.of("en"));
        assertEquals("Error EN", en.getContent().trim());
    }

    @Test
    void store_and_service_overwrite_sampleFile_then_restore() {
        String ns = "default";
        String code = "overwrite-demo";
        String locale = "en";
        String original = i18nStoreService.findByNamespaceAndCodeAndLocale(ns, code, locale)
                .getContent()
                .trim();
        assertEquals("overwrite-v1", original);

        i18nStoreService.save(I18n.builder()
                .namespace(ns)
                .code(code)
                .locale(locale)
                .content("overwrite-v2")
                .build());
        i18nService.clearCache();

        assertEquals("overwrite-v2",
                i18nStoreService.findByNamespaceAndCodeAndLocale(ns, code, locale).getContent().trim());
        assertEquals("overwrite-v2",
                i18nService.translate(ns, code, locale).getContent().trim());

        // restore sample for other runs / IDE re-runs
        i18nStoreService.save(I18n.builder()
                .namespace(ns)
                .code(code)
                .locale(locale)
                .content("overwrite-v1")
                .build());
        i18nService.clearCache();
        assertEquals("overwrite-v1",
                i18nService.translate(ns, code, List.of("en")).getContent().trim());
    }

    @Test
    void store_overwrite_doesNotAffectOtherLocales() {
        i18nStoreService.save(I18n.builder()
                .namespace("template")
                .code("esign/sms/status")
                .locale("en")
                .content("SMS EN overwritten")
                .build());
        i18nService.clearCache();

        assertEquals("SMS EN overwritten",
                i18nStoreService.findByNamespaceAndCodeAndLocale("template", "esign/sms/status", "en")
                        .getContent().trim());
        assertEquals("SMS RO",
                i18nStoreService.findByNamespaceAndCodeAndLocale("template", "esign/sms/status", "ro")
                        .getContent().trim());
        // preference still picks ro first
        assertEquals("ro", i18nStoreService.findFirstByNamespaceAndCodeAndLocales(
                "template", "esign/sms/status", List.of("ro", "en")
        ).getLocale());

        // restore
        i18nStoreService.save(I18n.builder()
                .namespace("template")
                .code("esign/sms/status")
                .locale("en")
                .content("SMS EN")
                .build());
        i18nService.clearCache();
    }

    @Test
    void service_translate_mixedLocaleTokens_inPreferenceList() {
        I18n hit = i18nService.translate(
                "template",
                "esign/sms/status",
                List.of(Locale.GERMAN, "RO", "en_US")
        );
        assertEquals("de", hit.getLocale());
        assertEquals("SMS DE", hit.getContent().trim());
    }

    @SpringBootConfiguration
    @EnableAutoConfiguration(exclude = {
            DataSourceAutoConfiguration.class,
            HibernateJpaAutoConfiguration.class,
            DataJpaRepositoriesAutoConfiguration.class
    })
    @ComponentScan(basePackages = "org.digitalmind.buildingblocks.core.i18n")
    @Import({I18nCoreModuleConfig.class, I18nConfig.class})
    static class FsTestApp {
    }
}
