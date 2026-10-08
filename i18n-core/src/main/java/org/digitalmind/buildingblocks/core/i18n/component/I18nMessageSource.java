package org.digitalmind.buildingblocks.core.i18n.component;

import lombok.extern.slf4j.Slf4j;
import org.digitalmind.buildingblocks.core.i18n.config.I18nCoreModuleConfig;
import org.digitalmind.buildingblocks.core.i18n.entity.I18n;
import org.digitalmind.buildingblocks.core.i18n.service.I18nService;
import org.digitalmind.buildingblocks.core.i18n.util.I18nMessageFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Locale;

import static org.digitalmind.buildingblocks.core.i18n.entity.I18n.DEFAULT_NAMESPACE;

/**
 * Opt-in Spring {@link org.springframework.context.MessageSource} adapter over {@link I18nService}.
 * <p>
 * DB {@code content} is <strong>plain display text</strong> (same as {@code translate().getContent()}).
 * This adapter converts it to a MessageFormat pattern at resolve time via {@link I18nMessageFormatUtil}.
 * <p>
 * Code convention: {@code namespace:code}. If {@code :} is missing, namespace defaults to {@link I18n#DEFAULT_NAMESPACE}.
 * Wire explicitly in the consumer — bean name {@code i18nMessageSource}, not global {@code messageSource}.
 */
@Component(I18nCoreModuleConfig.MESSAGE_SOURCE_BEAN)
@ConditionalOnBean(I18nService.class)
@ConditionalOnProperty(
        name = I18nCoreModuleConfig.MESSAGE_SOURCE_ENABLED,
        havingValue = "true",
        matchIfMissing = false
)
@Slf4j
public class I18nMessageSource extends AbstractMessageSource {

    private final I18nService translationService;

    @Autowired
    public I18nMessageSource(I18nService translationService) {
        this.translationService = translationService;
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        String[] parts = splitNamespaceAndCode(code);
        I18n i18n = translationService.translate(parts[0], parts[1], locale);
        String pattern = I18nMessageFormatUtil.toMessageFormatPattern(i18n.getContent());
        return new MessageFormat(pattern, locale);
    }

    /**
     * @return [namespace, code]
     */
    static String[] splitNamespaceAndCode(String code) {
        if (code == null || code.isBlank()) {
            return new String[]{DEFAULT_NAMESPACE, ""};
        }
        int separator = code.indexOf(':');
        if (separator <= 0 || separator == code.length() - 1) {
            return new String[]{DEFAULT_NAMESPACE, code};
        }
        return new String[]{code.substring(0, separator), code.substring(separator + 1)};
    }

}
