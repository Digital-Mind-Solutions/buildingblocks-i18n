package org.digitalmind.buildingblocks.core.i18n.service;

import org.digitalmind.buildingblocks.core.i18n.entity.I18n;
import org.digitalmind.buildingblocks.core.i18n.util.I18nLocaleUtil;

import java.util.List;
import java.util.Locale;

/**
 * Persistence backend for i18n rows (DB or filesystem).
 * String {@code locale} arguments should already be normalized ({@link I18nLocaleUtil#normalize(String)}).
 * {@link Locale} overloads are defaulted here — impls only implement the String variants.
 * Translate / cache / miss-sentinel stay on {@link I18nService}.
 */
public interface I18nStoreService {

    I18n getById(Long id);

    I18n findByNamespaceAndCodeAndLocale(String namespace, String code, String locale);

    default I18n findByNamespaceAndCodeAndLocale(String namespace, String code, Locale locale) {
        return findByNamespaceAndCodeAndLocale(namespace, code, I18nLocaleUtil.normalize(locale));
    }

    long deleteByNamespaceAndCodeAndLocale(String namespace, String code, String locale);

    default long deleteByNamespaceAndCodeAndLocale(String namespace, String code, Locale locale) {
        return deleteByNamespaceAndCodeAndLocale(namespace, code, I18nLocaleUtil.normalize(locale));
    }

    void deleteById(Long id);

    I18n save(I18n i18n);

    I18n findFirstByNamespaceAndCodeAndLocales(String namespace, String code, List<?> locales);

}
