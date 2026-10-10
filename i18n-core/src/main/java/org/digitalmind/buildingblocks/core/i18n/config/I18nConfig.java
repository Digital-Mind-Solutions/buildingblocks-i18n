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
    /**
     * Store backend: {@link I18nStoreImplementation#DB} (default) or {@link I18nStoreImplementation#FS}.
     * <pre>
     * application.modules.common.i18n:
     *   implementation: DB
     *   fs:
     *     rootFolder: /var/i18n
     * </pre>
     * FS tree: {@code {rootFolder}/{locale}/{namespace}/{code…}}.
     */
    private I18nStoreImplementation implementation = I18nStoreImplementation.DB;
    private I18nStoreDbProperties db = new I18nStoreDbProperties();
    private I18nStoreFsProperties fs = new I18nStoreFsProperties();
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
