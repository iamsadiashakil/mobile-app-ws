package com.appsdeveloperblog.app.ws.mobileappws.exception;

public class InvalidInputException extends ApplicationException {

    public InvalidInputException(int code, String message, Throwable throwable){
        super(code, message, throwable);
    }

    public InvalidInputException(String message, Throwable throwable) {
        this(INVALID_INPUT_EXCEPTION, message, throwable);
    }

    public InvalidInputException(String message) {
        this(message,null);
    }
}