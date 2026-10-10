package org.digitalmind.buildingblocks.core.i18n.service.impl;

import org.digitalmind.buildingblocks.core.i18n.config.I18nConfig;
import org.digitalmind.buildingblocks.core.i18n.config.I18nStoreFsProperties;
import org.digitalmind.buildingblocks.core.i18n.entity.I18n;
import org.digitalmind.buildingblocks.core.i18n.exception.I18nInitializeException;
import org.digitalmind.buildingblocks.core.i18n.exception.I18nNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class I18nStoreServiceFSImplTest {

    @TempDir
    Path root;

    private I18nStoreServiceFSImpl store;

    @BeforeEach
    void setUp() {
        store = new I18nStoreServiceFSImpl(root);
    }

    @Test
    void save_and_find_byKey_and_preferenceList() {
        I18n saved = store.save(row("template", "esign/sms/welcome", "en", "Hello"));
        assertTrue(Files.isRegularFile(root.resolve("en/template/esign/sms/welcome")));

        I18n loaded = store.findByNamespaceAndCodeAndLocale("template", "esign/sms/welcome", "en");
        assertEquals(saved.getId(), loaded.getId());
        assertEquals("Hello", loaded.getContent());

        store.save(row("template", "esign/sms/welcome", "ro", "Salut"));
        I18n preferred = store.findFirstByNamespaceAndCodeAndLocales(
                "template", "esign/sms/welcome", List.of("de", "ro", "en")
        );
        assertEquals("ro", preferred.getLocale());
        assertEquals("Salut", preferred.getContent());
    }

    @Test
    void save_overwritesExistingContent() {
        store.save(row("default", "hello", "en", "v1"));
        store.save(row("default", "hello", "en", "v2"));
        assertEquals("v2", store.findByNamespaceAndCodeAndLocale("default", "hello", "en").getContent());
    }

    @Test
    void findByKey_miss_returnsNull() {
        assertNull(store.findByNamespaceAndCodeAndLocale("default", "missing", "en"));
        assertNull(store.findFirstByNamespaceAndCodeAndLocales("default", "missing", List.of("en", "ro")));
    }

    @Test
    void findByKey_localeOverload_normalizesUs() {
        store.save(row("default", "hello", "en-us", "Hi US"));
        I18n hit = store.findByNamespaceAndCodeAndLocale("default", "hello", Locale.US);
        assertEquals("en-us", hit.getLocale());
        assertEquals("Hi US", hit.getContent());
    }

    @Test
    void deleteByNamespaceAndCodeAndLocale_counts() {
        assertEquals(0L, store.deleteByNamespaceAndCodeAndLocale("default", "hello", "en"));
        store.save(row("default", "hello", "en", "Hi"));
        assertEquals(1L, store.deleteByNamespaceAndCodeAndLocale("default", "hello", "en"));
        assertEquals(0L, store.deleteByNamespaceAndCodeAndLocale("default", "hello", "en"));
        assertNull(store.findByNamespaceAndCodeAndLocale("default", "hello", "en"));
    }

    @Test
    void getById_and_deleteById() {
        I18n saved = store.save(row("default", "hello", "en", "Hi"));
        assertEquals("Hi", store.getById(saved.getId()).getContent());

        store.deleteById(saved.getId());
        assertNull(store.findByNamespaceAndCodeAndLocale("default", "hello", "en"));
        assertThrows(I18nNotFoundException.class, () -> store.getById(saved.getId()));
    }

    @Test
    void save_rejectsMultiSegmentNamespace() {
        assertThrows(
                I18nInitializeException.class,
                () -> store.save(row("a/b", "code", "en", "x"))
        );
    }

    @Test
    void save_rejectsPathTraversalInCode() {
        assertThrows(
                I18nInitializeException.class,
                () -> store.save(row("default", "../etc/passwd", "en", "x"))
        );
    }

    @Test
    void listAll_skipsReadmeAtRoot() throws Exception {
        Files.writeString(root.resolve("README.md"), "# ignore me");
        store.save(row("default", "hello", "en", "Hi"));
        I18n byId = store.getById(store.findByNamespaceAndCodeAndLocale("default", "hello", "en").getId());
        assertEquals("Hi", byId.getContent());
    }

    @Test
    void bootstrap_missingRootFolder_failsFast() {
        I18nConfig config = new I18nConfig();
        config.setFs(new I18nStoreFsProperties());
        assertThrows(I18nInitializeException.class, () -> new I18nStoreServiceFSImpl(config));
    }

    @Test
    void bootstrap_missingDirectory_failsFast() {
        I18nConfig config = new I18nConfig();
        I18nStoreFsProperties fs = new I18nStoreFsProperties();
        fs.setRootFolder(root.resolve("does-not-exist").toString());
        config.setFs(fs);
        assertThrows(I18nInitializeException.class, () -> new I18nStoreServiceFSImpl(config));
    }

    @Test
    void fallback_skipsMissingThenHitsLaterLocale() {
        store.save(row("template", "esign/sms/status", "en", "SMS EN"));
        store.save(row("template", "esign/sms/status", "de", "SMS DE"));
        I18n hit = store.findFirstByNamespaceAndCodeAndLocales(
                "template", "esign/sms/status", List.of("fr", "it", "de", "en")
        );
        assertEquals("de", hit.getLocale());
        assertEquals("SMS DE", hit.getContent());
    }

    @Test
    void overwrite_singleLocale_preservesSiblingLocales() {
        store.save(row("default", "msg", "en", "EN-1"));
        store.save(row("default", "msg", "ro", "RO-1"));
        store.save(row("default", "msg", "en", "EN-2"));

        assertEquals("EN-2", store.findByNamespaceAndCodeAndLocale("default", "msg", "en").getContent());
        assertEquals("RO-1", store.findByNamespaceAndCodeAndLocale("default", "msg", "ro").getContent());
        assertEquals("ro", store.findFirstByNamespaceAndCodeAndLocales(
                "default", "msg", List.of("ro", "en")
        ).getLocale());
    }

    @Test
    void fallback_allMissing_returnsNull() {
        store.save(row("default", "only-en", "en", "EN"));
        assertNull(store.findFirstByNamespaceAndCodeAndLocales(
                "default", "only-en", List.of("de", "fr")
        ));
    }

    @Test
    void delete_oneLocale_fallbackMovesToNext() {
        store.save(row("default", "msg", "ro", "RO"));
        store.save(row("default", "msg", "en", "EN"));
        assertEquals(1L, store.deleteByNamespaceAndCodeAndLocale("default", "msg", "ro"));
        I18n hit = store.findFirstByNamespaceAndCodeAndLocales(
                "default", "msg", List.of("ro", "en")
        );
        assertEquals("en", hit.getLocale());
        assertEquals("EN", hit.getContent());
    }

    private static I18n row(String namespace, String code, String locale, String content) {
        return I18n.builder()
                .namespace(namespace)
                .code(code)
                .locale(locale)
                .content(content)
                .build();
    }
}
