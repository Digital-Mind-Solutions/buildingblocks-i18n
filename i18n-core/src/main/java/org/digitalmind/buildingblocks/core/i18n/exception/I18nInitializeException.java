package org.digitalmind.buildingblocks.core.i18n.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
public class I18nInitializeException extends RuntimeException {
    public I18nInitializeException() {
    }

    public I18nInitializeException(String message) {
        super(message);
    }

    public I18nInitializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public I18nInitializeException(Throwable cause) {
        super(cause);
    }

    public I18nInitializeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
