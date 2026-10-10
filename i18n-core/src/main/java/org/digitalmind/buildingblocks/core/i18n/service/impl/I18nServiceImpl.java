package org.digitalmind.buildingblocks.core.i18n.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.digitalmind.buildingblocks.core.i18n.config.I18nConfig;
import org.digitalmind.buildingblocks.core.i18n.entity.I18n;
import org.digitalmind.buildingblocks.core.i18n.service.I18nService;
import org.digitalmind.buildingblocks.core.i18n.service.I18nStoreService;
import org.digitalmind.buildingblocks.core.i18n.util.I18nLocaleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

import static org.digitalmind.buildingblocks.core.i18n.config.I18nCoreModuleConfig.CACHE_NAME;
import static org.digitalmind.buildingblocks.core.i18n.config.I18nCoreModuleConfig.ENABLED;
import static org.digitalmind.buildingblocks.core.i18n.entity.I18n.DEFAULT_NAMESPACE;

@Service
@ConditionalOnProperty(name = ENABLED, havingValue = "true")
@Slf4j
@Transactional
public class I18nServiceImpl implements I18nService {

    private final I18nConfig i18nConfig;
    private final I18nStoreService i18nStoreService;
    /**
     * Concrete proxy so {@link #translateForStringLocales} stays off the interface but still hits cache.
     */
    private final I18nServiceImpl self;

    @Autowired
    public I18nServiceImpl(
            I18nConfig i18nConfig,
            I18nStoreService i18nStoreService,
            @Lazy I18nServiceImpl self
    ) {
        this.i18nConfig = i18nConfig;
        this.i18nStoreService = i18nStoreService;
        this.self = self;
    }

    private I18n missingTranslation(String namespace, String code, List<String> locales) {
        String locale = (locales == null || locales.isEmpty()) ? null : locales.get(0);
        return I18n.builder()
                .id(0L)
                .namespace(namespace != null ? namespace : DEFAULT_NAMESPACE)
                .code(code)
                .content(code)
                .locale(locale)
                .build();
    }

    /**
     * Cached string-locale lookup — implementation detail (not on {@link I18nService}).
     */
    @Cacheable(cacheNames = CACHE_NAME, unless = "#result == null || #result.id == null || #result.id == 0")
    public I18n translateForStringLocales(String namespace, String code, List<String> locales) {
        String resolvedNamespace = (namespace == null || namespace.isBlank()) ? DEFAULT_NAMESPACE : namespace;
        List<String> orderedLocales = I18nLocaleUtil.normalizeOrdered(locales);
        if (orderedLocales.isEmpty()) {
            String fallback = I18nLocaleUtil.normalize(i18nConfig.getDefaultLocale());
            orderedLocales = fallback == null ? List.of() : List.of(fallback);
        }
        I18n i18n = i18nStoreService.findFirstByNamespaceAndCodeAndLocales(
                resolvedNamespace, code, orderedLocales
        );
        if (i18n == null) {
            return missingTranslation(resolvedNamespace, code, orderedLocales);
        }
        return i18n;
    }

    @Override
    public I18n translate(String namespace, String code, List<?> locales) {
        return self.translateForStringLocales(
                namespace, code, I18nLocaleUtil.normalizeOrderedObjects(locales)
        );
    }

    @Override
    public I18n translate(String namespace, String code, Locale locale) {
        return self.translateForStringLocales(
                namespace, code, I18nLocaleUtil.preferenceList(locale, i18nConfig.getDefaultLocale())
        );
    }

    @Override
    public I18n translate(String namespace, String code, String locale) {
        return self.translateForStringLocales(namespace, code, I18nLocaleUtil.normalizeOrdered(
                List.of(locale, i18nConfig.getDefaultLocale()))
        );
    }

    @Override
    public I18n translate(String code, Locale locale) {
        return self.translateForStringLocales(DEFAULT_NAMESPACE, code,
                I18nLocaleUtil.preferenceList(locale, i18nConfig.getDefaultLocale())
        );
    }

    @Override
    public I18n translate(String code, String locale) {
        return translate(DEFAULT_NAMESPACE, code, locale);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, allEntries = true)
    public void clearCache() {
    }

}
