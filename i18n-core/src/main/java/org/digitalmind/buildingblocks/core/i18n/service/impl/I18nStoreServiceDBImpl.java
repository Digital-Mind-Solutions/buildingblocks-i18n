package org.digitalmind.buildingblocks.core.i18n.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.digitalmind.buildingblocks.core.i18n.entity.I18n;
import org.digitalmind.buildingblocks.core.i18n.exception.I18nNotFoundException;
import org.digitalmind.buildingblocks.core.i18n.repository.I18nRepository;
import org.digitalmind.buildingblocks.core.i18n.service.I18nStoreService;
import org.digitalmind.buildingblocks.core.i18n.util.I18nLocaleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.digitalmind.buildingblocks.core.i18n.config.I18nCoreModuleConfig.ENABLED;
import static org.digitalmind.buildingblocks.core.i18n.config.I18nCoreModuleConfig.STORE_IMPLEMENTATION;

@Service
@Transactional
@ConditionalOnProperty(name = ENABLED, havingValue = "true")
@ConditionalOnProperty(name = STORE_IMPLEMENTATION, havingValue = "DB", matchIfMissing = true)
@Slf4j
public class I18nStoreServiceDBImpl implements I18nStoreService {

    private final I18nRepository i18nRepository;

    @Autowired
    public I18nStoreServiceDBImpl(I18nRepository i18nRepository) {
        this.i18nRepository = i18nRepository;
    }

    @Override
    public I18n getById(Long id) {
        return i18nRepository.findById(id)
                .orElseThrow(() -> new I18nNotFoundException("I18n not found for id=" + id));
    }

    @Override
    public I18n findByNamespaceAndCodeAndLocale(String namespace, String code, String locale) {
        return i18nRepository.findByNamespaceAndCodeAndLocale(namespace, code, locale);
    }

    @Override
    public long deleteByNamespaceAndCodeAndLocale(String namespace, String code, String locale) {
        return i18nRepository.deleteByNamespaceAndCodeAndLocale(namespace, code, locale);
    }

    @Override
    public void deleteById(Long id) {
        i18nRepository.deleteById(id);
    }

    @Override
    public I18n save(I18n i18n) {
        return i18nRepository.save(i18n);
    }

    @Override
    public I18n findFirstByNamespaceAndCodeAndLocales(String namespace, String code, List<?> locales) {
        return i18nRepository.findFirstByNamespaceAndCodeAndLocales(
                namespace, code, I18nLocaleUtil.normalizeOrderedObjects(locales)
        );
    }
}
