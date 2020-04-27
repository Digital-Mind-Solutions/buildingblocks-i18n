package org.digitalmind.buildingblocks.core.i18n.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = I18nCoreModuleConfig.ENABLED, havingValue = "true")
@ConfigurationProperties(prefix = I18nCoreModuleConfig.PREFIX)
@EnableConfigurationProperties
@Getter
@Setter
public class I18nConfig {
    private boolean enabled;
    private String defaultLocale;
    //private DynamicCacheDefinition.DynamicCacheProperties cache;

}
