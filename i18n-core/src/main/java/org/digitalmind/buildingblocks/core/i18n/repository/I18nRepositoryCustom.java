package org.digitalmind.buildingblocks.core.i18n.repository;

import org.digitalmind.buildingblocks.core.i18n.entity.I18n;

import java.util.List;

public interface I18nRepositoryCustom {

    /**
     * Single query: match namespace + code + locale IN list, ordered by list position, first row.
     * {@code locales} must already be normalized and ordered by preference.
     */
    I18n findFirstByNamespaceAndCodeAndLocales(String namespace, String code, List<String> locales);

}
