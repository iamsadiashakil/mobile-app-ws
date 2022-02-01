package com.appsdeveloperblog.app.ws.mobileappws.exception;

public class UserServiceException extends ApplicationException {

    public UserServiceException(int code, String message, Throwable throwable){
        super(code, message, throwable);
    }

    public UserServiceException(String message, Throwable throwable) {
        this(USER_SERVICE_EXCEPTION, message, throwable);
    }

    public UserServiceException(String message) {
        this(message,null);
    }
}
