package org.digitalmind.buildingblocks.core.i18n.support;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.digitalmind.buildingblocks.core.i18n.config.I18nCoreModuleConfig.CACHE_NAME;

/**
 * In-memory {@link CacheManager} for FS ITs — host apps supply their own; tests need one
 * because {@code I18nCoreModuleConfig} enables caching and {@code translate} is {@code @Cacheable}.
 */
@Configuration
@EnableCaching
public class I18nFsTestCacheConfiguration implements CachingConfigurer {

    @Bean
    @Override
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(CACHE_NAME);
    }
}
