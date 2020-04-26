package org.digitalmind.buildingblocks.i18n.core.repository;

import org.digitalmind.buildingblocks.i18n.core.entity.I18n;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface I18nRepository extends JpaRepository<I18n, Long> {

    I18n findByCodeAndLocale(String code, String locale);

    Page<I18n> findByCodeAndLocaleStartingWith(String code, String locale, Pageable pageable);

    Page<I18n> findByCodeContainingAndLocaleStartingWith(String code, String locale, Pageable pageable);

    Page<I18n> findByCodeStartingWithAndLocaleStartingWith(String code, String locale, Pageable pageable);

    long deleteByCodeAndLocale(String code, String locale);

}
