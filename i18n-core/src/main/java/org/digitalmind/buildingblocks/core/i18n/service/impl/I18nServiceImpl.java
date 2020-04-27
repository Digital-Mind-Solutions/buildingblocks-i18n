package org.digitalmind.buildingblocks.core.i18n.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.digitalmind.buildingblocks.core.i18n.config.I18nConfig;
import org.digitalmind.buildingblocks.core.i18n.dto.I18nSearchOperator;
import org.digitalmind.buildingblocks.core.i18n.entity.I18n;
import org.digitalmind.buildingblocks.core.i18n.repository.I18nRepository;
import org.digitalmind.buildingblocks.core.i18n.service.I18nService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

import static org.digitalmind.buildingblocks.core.i18n.config.I18nCoreModuleConfig.CACHE_NAME;
import static org.digitalmind.buildingblocks.core.i18n.config.I18nCoreModuleConfig.ENABLED;

@Service
@ConditionalOnProperty(name = ENABLED, havingValue = "true")
@Slf4j
@Transactional
public class I18nServiceImpl implements I18nService {

    private final I18nConfig i18nConfig;
    private final I18nRepository i18nRepository;
    //private final DynamicCacheResolver cacheResolver;

    @Autowired
    public I18nServiceImpl(
            I18nConfig i18nConfig,
            I18nRepository i18nRepository
            //,
            //@Qualifier(DYNAMIC_CACHE_RESOLVER) DynamicCacheResolver cacheResolver
    ) {

        this.i18nConfig = i18nConfig;
        this.i18nRepository = i18nRepository;
        //this.cacheResolver = cacheResolver;

        //        try {
        //            this.cacheResolver.registerCacheDefinition(
        //                    DynamicCacheDefinition.builder()
        //                            .method(I18nServiceImpl.class.getMethod("translate", new Class[]{String.class, String.class}))
        //                            .operation((new CacheableOperation.Builder()).build())
        //                            .cach(
        //                                    DynamicCacheDefinition.DynamicCacheProperties.builder()
        //                                            .cacheManager(i18nConfig.getCache().getCacheManager())
        //                                            .cacheNames(i18nConfig.getCache().getCacheNames())
        //                                            .build()
        //                            )
        //                            .build()
        //            );
        //            this.cacheResolver.registerCacheDefinition(
        //                    DynamicCacheDefinition.builder()
        //                            .method(I18nServiceImpl.class.getMethod("clearCache", new Class[]{}))
        //                            .operation((new CacheEvictOperation.Builder()).build())
        //                            .cach(
        //                                    DynamicCacheDefinition.DynamicCacheProperties.builder()
        //                                            .cacheManager(i18nConfig.getCache().getCacheManager())
        //                                            .cacheNames(i18nConfig.getCache().getCacheNames())
        //                                            .build()
        //                            )
        //                            .build()
        //            );
        //        } catch (NoSuchMethodException e) {
        //            throw new I18nInitializeException(e);
        //        }
    }

    @Override
    public I18n getOne(Long id) {
        return i18nRepository.getOne(id);
    }

    @Override
    public I18n findByCodeAndLocale(String code, String locale) {
        return i18nRepository.findByCodeAndLocale(code, locale);
    }

    @Override
    public Page<I18n> findByCodeAndLocale(String code, I18nSearchOperator operator, String locale, Pageable pageable) {
        switch (operator) {
            case CONTAINS:
                return this.i18nRepository.findByCodeContainingAndLocaleStartingWith(code, locale, pageable);

            case START_WITH:
                return this.i18nRepository.findByCodeStartingWithAndLocaleStartingWith(code, locale, pageable);

            case EQUALS:
                return this.i18nRepository.findByCodeAndLocaleStartingWith(code, locale, pageable);
        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        i18nRepository.deleteById(id);
    }

    @Override
    public long deleteByCodeAndLocale(String code, String locale) {
        return i18nRepository.deleteByCodeAndLocale(code, locale);
    }

    @Override
    public I18n save(I18n i18n) {
        return i18nRepository.save(i18n);
    }


    private I18n defaultLocale(I18n i18n, String key, String locale) {
        if (i18n == null) {
            return I18n.builder().id(0L).code(key).content(key).locale(locale).build();
        }
        return i18n;
    }

    @Override
    //@Cacheable(cacheResolver = DYNAMIC_CACHE_RESOLVER)
    @Cacheable(cacheNames = CACHE_NAME)
    public I18n translate(String key, Locale locale) {
        String localeString = locale.toString();
        I18n i18n = i18nRepository.findByCodeAndLocale(key, localeString);
        if (i18n == null) {
            localeString = locale.getLanguage();
            i18n = i18nRepository.findByCodeAndLocale(key, localeString);
        }
        return defaultLocale(i18n, key, localeString);
    }

    @Override
    //@Cacheable(cacheResolver = DYNAMIC_CACHE_RESOLVER)
    @Cacheable(cacheNames = CACHE_NAME)
    public I18n translate(String key, String locale) {
        I18n i18n = i18nRepository.findByCodeAndLocale(key, locale);
        if (i18n == null) {
            i18n = i18nRepository.findByCodeAndLocale(key, i18nConfig.getDefaultLocale());
        }
        return defaultLocale(i18n, key, locale);
    }

    //@CacheEvict(cacheResolver = DYNAMIC_CACHE_RESOLVER, allEntries = true)
    @CacheEvict(cacheNames = CACHE_NAME, allEntries = true)
    public void clearCache() {
    }

}
