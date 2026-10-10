package org.digitalmind.buildingblocks.core.i18n.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class I18nNotFoundException extends RuntimeException {

    public I18nNotFoundException() {
    }

    public I18nNotFoundException(String message) {
        super(message);
    }

    public I18nNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public I18nNotFoundException(Throwable cause) {
        super(cause);
    }
}
