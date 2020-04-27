package org.digitalmind.buildingblocks.core.i18n.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.digitalmind.buildingblocks.core.i18n.config.I18nCoreModuleConfig.*;

@Configuration
@ComponentScan({
        SERVICE_PACKAGE,
        API_PACKAGE
})
@EnableCaching
@ConditionalOnProperty(name = ENABLED, havingValue = "true")
@Slf4j
public class I18nCoreModuleConfig {
    public static final String MODULE = "i18n";
    public static final String PREFIX = "application.modules.common." + MODULE;
    public static final String ENABLED = PREFIX + ".enabled";
    public static final String API_ENABLED = PREFIX + ".api.enabled";

    public static final String ROOT_PACKAGE = "org.digitalmind.buildingblocks.core." + MODULE;
    public static final String CONFIG_PACKAGE = ROOT_PACKAGE + ".config";
    public static final String ENTITY_PACKAGE = ROOT_PACKAGE + ".entity";
    public static final String REPOSITORY_PACKAGE = ROOT_PACKAGE + ".repository";
    public static final String SERVICE_PACKAGE = ROOT_PACKAGE + ".service";
    public static final String API_PACKAGE = ROOT_PACKAGE + ".api";

    public static final String CACHE_NAME = MODULE + "-cache";

}
