package org.digitalmind.buildingblocks.i18n.core.component;

import lombok.extern.slf4j.Slf4j;
import org.digitalmind.buildingblocks.i18n.core.entity.I18n;
import org.digitalmind.buildingblocks.i18n.core.service.I18nService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Locale;

import static org.digitalmind.buildingblocks.i18n.core.config.I18nModuleConfig.ENABLED;


@Component("messageSource")
@ConditionalOnProperty(name = ENABLED, havingValue = "true")
@Slf4j
public class I18nMessageSource extends AbstractMessageSource {

    private final I18nService translationService;

    @Autowired
    public I18nMessageSource(I18nService translationService) {
        this.translationService = translationService;
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        I18n i18n = translationService.translate(code, locale.getLanguage());
        if (i18n == null) {
            return new MessageFormat(code, locale);
        }
        return new MessageFormat(i18n.getContent(), locale);
    }

}
