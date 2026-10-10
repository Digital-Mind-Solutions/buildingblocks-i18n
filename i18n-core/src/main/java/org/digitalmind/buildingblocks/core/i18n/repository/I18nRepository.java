package org.digitalmind.buildingblocks.core.i18n.repository;

import org.digitalmind.buildingblocks.core.i18n.entity.I18n;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import static org.digitalmind.buildingblocks.core.i18n.config.I18nCoreModuleConfig.ENABLED;
import static org.digitalmind.buildingblocks.core.i18n.config.I18nCoreModuleConfig.STORE_IMPLEMENTATION;

@Repository
@ConditionalOnProperty(name = ENABLED, havingValue = "true")
@ConditionalOnProperty(name = STORE_IMPLEMENTATION, havingValue = "DB", matchIfMissing = true)
public interface I18nRepository extends JpaRepository<I18n, Long>, I18nRepositoryCustom {

    I18n findByNamespaceAndCodeAndLocale(String namespace, String code, String locale);

    long deleteByNamespaceAndCodeAndLocale(String namespace, String code, String locale);

}
