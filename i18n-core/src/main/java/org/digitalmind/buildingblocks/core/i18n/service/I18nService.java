package org.digitalmind.buildingblocks.core.i18n.service;

import org.digitalmind.buildingblocks.core.i18n.entity.I18n;
import org.digitalmind.buildingblocks.core.i18n.dto.I18nSearchOperator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Locale;

public interface I18nService {

    I18n translate (String code, Locale locale);

    I18n translate(String code, String locale);

    void clearCache();

    I18n getOne(Long id);

    I18n findByCodeAndLocale(String code, String locale);

    Page<I18n> findByCodeAndLocale(String code, I18nSearchOperator operator, String locale, Pageable pageable);

    void deleteById(Long id);

    long deleteByCodeAndLocale(String code, String locale);

    I18n save(I18n i18n);

}
