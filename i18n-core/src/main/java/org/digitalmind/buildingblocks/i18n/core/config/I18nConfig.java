package org.digitalmind.buildingblocks.i18n.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static org.digitalmind.buildingblocks.i18n.core.config.I18nModuleConfig.ENABLED;
import static org.digitalmind.buildingblocks.i18n.core.config.I18nModuleConfig.PREFIX;

@Configuration
@ConditionalOnProperty(name = ENABLED, havingValue = "true")
@ConfigurationProperties(prefix = PREFIX)
@EnableConfigurationProperties
@Getter
@Setter
public class I18nConfig {
    private boolean enabled;
    private String defaultLocale;
    //private DynamicCacheDefinition.DynamicCacheProperties cache;

}
