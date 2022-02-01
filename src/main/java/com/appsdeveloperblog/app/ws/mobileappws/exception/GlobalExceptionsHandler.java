package com.appsdeveloperblog.app.ws.mobileappws.exception;

import com.appsdeveloperblog.app.ws.mobileappws.shared.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionsHandler {
    protected static final Logger logger = LoggerFactory.getLogger(GlobalExceptionsHandler.class);

    @ExceptionHandler(value = {NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ErrorResponse> handleError404(ServletException exception, HttpServletRequest request) {
        int code;
        String message;

        if (exception instanceof NoHandlerFoundException) {
            code = ApplicationException.ERROR_ENDPOINT_NOT_FOUND;
            message = "Requested END POINT not found";
        } else {
            code = ApplicationException.ERROR_HTTP_METHOD_NOT_SUPPORTED;
            message = "HTTP Method Not supported" + request.getMethod();
        }
        ErrorResponse response = new ErrorResponse(code, message);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {UserServiceException.class})
    public ResponseEntity<Object> handleUserServiceException(UserServiceException ex) {
        int code;
        String message;

        code = ApplicationException.ERROR_ENDPOINT_NOT_FOUND;
        message = "Requested END POINT not found";
        ErrorResponse response = new ErrorResponse(code, message);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<ErrorResponse> handleAllExceptions(Exception exception) {
        if (exception instanceof ApplicationException) {
            return handleApplicationException((ApplicationException) exception);
        } else if (exception instanceof HttpMessageNotReadableException) {
            return handleFormattingException((HttpMessageNotReadableException) exception);
        } else if (exception instanceof MethodArgumentTypeMismatchException) {
            return handleFormattingException((MethodArgumentTypeMismatchException) exception);
        }
        logger.error("System exception occurred", exception);

        return new ResponseEntity<>(new ErrorResponse(ApplicationException.UNKNOWN_EXCEPTION_ERROR, "Unknown error occurred please contact site admin"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> handleFormattingException(MethodArgumentTypeMismatchException exception) {
        String message;

        if (exception.getCause() instanceof NumberFormatException) {
            message = "Parsing error, input is not a valid number";
        } else {
            message = "Parser error, argument type does not match";
        }

        return new ResponseEntity<>(new ErrorResponse(ApplicationException.ERROR_UNABLE_TO_MAP_OBJECT,
                message),
                HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> handleFormattingException(HttpMessageNotReadableException exception) {
        String message;

        boolean jsonMapping = exception.getCause() instanceof com.fasterxml.jackson.databind.JsonMappingException;

        if (exception.getCause() instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException) {
            message = "Parsing error, Data format might not be correct";
        } else if (jsonMapping
                && exception.getCause().getMessage().contains("out of range")) {
            message = exception.getCause().getMessage();
        } else {
            message = "Parser error, please make sure JSON is properly formatted";
        }

        return new ResponseEntity<>(new ErrorResponse(ApplicationException.ERROR_UNABLE_TO_MAP_OBJECT,
                message),
                HttpStatus.BAD_REQUEST);
    }

    protected ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException exception) {
        HttpStatus status = HttpStatus.CONFLICT;
        if (exception instanceof RecordNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else if (exception instanceof InvalidInputException) {
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(new ErrorResponse(exception), status);
    }
}
