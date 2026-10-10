package org.digitalmind.buildingblocks.core.i18n.service;

import org.digitalmind.buildingblocks.core.i18n.dto.I18nSearchOperator;
import org.digitalmind.buildingblocks.core.i18n.entity.I18n;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Locale;

/**
 * Service API aligned with the 4.1.0 surface.
 * <p>
 * On 4.0.0, namespace-scoped methods are declared for compile compatibility but are
 * <strong>not implemented</strong> (they throw {@link UnsupportedOperationException}).
 * Use the {@code (code, locale)} methods on this line, or upgrade to 4.1.0 for namespace support.
 */
public interface I18nService {

    I18n translate(String namespace, String code, List<?> locales);

    I18n translate(String namespace, String code, String locale);

    I18n translate(String namespace, String code, Locale locale);

    I18n translate(String code, String locale);

    I18n translate(String code, Locale locale);

    void clearCache();

    I18n getById(Long id);

    I18n findByNamespaceAndCodeAndLocale(String namespace, String code, String locale);

    I18n findByNamespaceAndCodeAndLocale(String namespace, String code, Locale locale);

    long deleteByNamespaceAndCodeAndLocale(String namespace, String code, String locale);

    long deleteByNamespaceAndCodeAndLocale(String namespace, String code, Locale locale);

    void deleteById(Long id);

    I18n save(I18n i18n);

    /** 4.0.0 line: lookup by {@code (code, locale)}. */
    I18n findByCodeAndLocale(String code, String locale);

    /** 4.0.0 line: paged search by code/locale. */
    Page<I18n> findByCodeAndLocale(String code, I18nSearchOperator operator, String locale, Pageable pageable);

    /** 4.0.0 line: delete by {@code (code, locale)}. */
    long deleteByCodeAndLocale(String code, String locale);

}
