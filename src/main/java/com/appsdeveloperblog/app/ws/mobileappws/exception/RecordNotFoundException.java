package com.appsdeveloperblog.app.ws.mobileappws.exception;

public class RecordNotFoundException extends ApplicationException {

    public RecordNotFoundException(String message, Throwable throwable) {
        super(RECORD_NOT_FOUND_EXCEPTION, message, throwable);
    }

    public RecordNotFoundException(String message) {
        this(message,null);
    }
}
