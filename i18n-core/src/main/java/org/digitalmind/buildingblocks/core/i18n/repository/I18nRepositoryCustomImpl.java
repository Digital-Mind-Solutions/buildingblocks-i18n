package org.digitalmind.buildingblocks.core.i18n.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.digitalmind.buildingblocks.core.i18n.entity.I18n;

import java.util.List;

public class I18nRepositoryCustomImpl implements I18nRepositoryCustom {

    private final EntityManager entityManager;

    public I18nRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public I18n findFirstByNamespaceAndCodeAndLocales(String namespace, String code, List<String> locales) {
        if (namespace == null || code == null || locales == null || locales.isEmpty()) {
            return null;
        }

        StringBuilder jpql = new StringBuilder(
                "SELECT i FROM I18n i WHERE i.namespace = :namespace AND i.code = :code AND i.locale IN :locales ORDER BY CASE i.locale");
        for (int index = 0; index < locales.size(); index++) {
            jpql.append(" WHEN :loc").append(index).append(" THEN ").append(index);
        }
        jpql.append(" ELSE ").append(locales.size()).append(" END");

        TypedQuery<I18n> query = entityManager.createQuery(jpql.toString(), I18n.class);
        query.setParameter("namespace", namespace);
        query.setParameter("code", code);
        query.setParameter("locales", locales);
        for (int index = 0; index < locales.size(); index++) {
            query.setParameter("loc" + index, locales.get(index));
        }
        query.setMaxResults(1);

        List<I18n> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

}
