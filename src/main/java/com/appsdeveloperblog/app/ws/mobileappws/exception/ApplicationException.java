package com.appsdeveloperblog.app.ws.mobileappws.exception;

public class ApplicationException  extends RuntimeException{
    public static final int USER_SERVICE_EXCEPTION = 1001;
    public static final int UNKNOWN_EXCEPTION_ERROR = 1002;
    public static final int ERROR_ENDPOINT_NOT_FOUND = 1003;
    public static final int ERROR_HTTP_METHOD_NOT_SUPPORTED = 1004;
    public static final int ERROR_UNABLE_TO_MAP_OBJECT = 1005;
    public static final int INVALID_INPUT_EXCEPTION = 1006;
    public static final int RECORD_NOT_FOUND_EXCEPTION = 1007;
    public static final int UN_AUTHORIZED = 1008;

    private final int code;

    public ApplicationException(int code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
    }

    public ApplicationException(int code, String message) {
        this(code, message, null);
    }

    public int getCode() {
        return code;
    }
}
