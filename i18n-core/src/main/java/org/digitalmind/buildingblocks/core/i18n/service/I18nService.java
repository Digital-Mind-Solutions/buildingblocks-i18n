package org.digitalmind.buildingblocks.core.i18n.service;

import org.digitalmind.buildingblocks.core.i18n.entity.I18n;

import java.util.List;
import java.util.Locale;

public interface I18nService {

    I18n translate(String namespace, String code, List<?> locales);

    I18n translate(String namespace, String code, String locale);

    I18n translate(String namespace, String code, Locale locale);

    I18n translate(String code, String locale);

    I18n translate(String code, Locale locale);

    void clearCache();

}
