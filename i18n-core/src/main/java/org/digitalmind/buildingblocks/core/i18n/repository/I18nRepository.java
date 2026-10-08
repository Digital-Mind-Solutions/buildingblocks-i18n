package org.digitalmind.buildingblocks.core.i18n.repository;

import org.digitalmind.buildingblocks.core.i18n.entity.I18n;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface I18nRepository extends JpaRepository<I18n, Long>, I18nRepositoryCustom {

    I18n findByNamespaceAndCodeAndLocale(String namespace, String code, String locale);

    long deleteByNamespaceAndCodeAndLocale(String namespace, String code, String locale);

}
