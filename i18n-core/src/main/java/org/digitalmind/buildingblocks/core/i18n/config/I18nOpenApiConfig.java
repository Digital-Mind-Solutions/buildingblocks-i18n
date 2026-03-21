package org.digitalmind.buildingblocks.core.i18n.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = I18nCoreModuleConfig.API_ENABLED, havingValue = "true")
public class I18nOpenApiConfig {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class ApiSwaggerProperties {
        private ApiSwaggerDocketProperties docket;
        private ApiSwaggerInfoProperties info;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @ToString
        public static class ApiSwaggerDocketProperties {
            private String host;
            private String basePath;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @ToString
        public static class ApiSwaggerInfoProperties {
            private String groupName;
            private String title;
            private String description;
            private String version;
            private ApiSwaggerContactProperties contact;
            private String license;
            private String licenseUrl;

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            @ToString
            public static class ApiSwaggerContactProperties {
                private String name;
                private String url;
                private String email;
            }
        }
    }

    @Bean(I18nCoreModuleConfig.PREFIX + "ApiSwaggerProperties")
    @ConfigurationProperties(prefix = I18nCoreModuleConfig.PREFIX + ".api")
    public ApiSwaggerProperties apiProperties() {
        return new ApiSwaggerProperties();
    }

    @Bean(I18nCoreModuleConfig.PREFIX + "OpenAPI")
    public OpenAPI openAPI(ApiSwaggerProperties apiSwaggerProperties) {
        ApiSwaggerProperties.ApiSwaggerInfoProperties info = apiSwaggerProperties.getInfo();
        Contact contact = info.getContact() != null
                ? new Contact().name(info.getContact().getName()).url(info.getContact().getUrl()).email(info.getContact().getEmail())
                : null;
        License license = (info.getLicense() != null || info.getLicenseUrl() != null)
                ? new License().name(info.getLicense()).url(info.getLicenseUrl())
                : null;
        return new OpenAPI()
                .info(new Info()
                        .title(info.getTitle())
                        .description(info.getDescription())
                        .version(info.getVersion())
                        .contact(contact)
                        .license(license));
    }

    @Bean(I18nCoreModuleConfig.PREFIX + "GroupedOpenApi")
    public GroupedOpenApi i18nGroup(ApiSwaggerProperties apiSwaggerProperties) {
        String basePath = apiSwaggerProperties.getDocket().getBasePath();
        String pathPattern = basePath == null || basePath.isEmpty() ? "/**" : "**" + basePath + "/**";
        return GroupedOpenApi.builder()
                .group(apiSwaggerProperties.getInfo().getGroupName())
                .pathsToMatch(pathPattern)
                .build();
    }
}
