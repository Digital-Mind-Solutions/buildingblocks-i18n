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
    /** REST admin API — off by default; set {@code api.enabled=true} to expose. */
    private ApiProperties api = new ApiProperties();
    private MessageSourceProperties messageSource = new MessageSourceProperties();

    @Getter
    @Setter
    public static class ApiProperties {
        private boolean enabled = false;
    }

    @Getter
    @Setter
    public static class MessageSourceProperties {
        /** When true, registers bean {@code i18nMessageSource}. Host wires it explicitly. */
        private boolean enabled = false;
    }

}
