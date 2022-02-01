package com.appsdeveloperblog.app.ws.mobileappws.service;

import com.appsdeveloperblog.app.ws.mobileappws.exception.InvalidInputException;
import com.appsdeveloperblog.app.ws.mobileappws.exception.RecordNotFoundException;
import com.appsdeveloperblog.app.ws.mobileappws.exception.UserServiceException;
import com.appsdeveloperblog.app.ws.mobileappws.shared.utils.ValueUtils;

import java.math.BigDecimal;
import java.util.Collection;

public class BusinessService {
    public void validateId(Long id, String message) {
        if (!ValueUtils.isId(id)) {
            throwInputError(message + ": " + id);
        }
    }

    public void validateIdIfNotNull(Long id, String message) {
        if (!ValueUtils.isNull(id)) {
            validateId(id, message);
        }
    }

    public void validateId(Integer id, String message) {
        if (!ValueUtils.isId(id)) {
            throwInputError(message + ": " + id);
        }
    }

    public void throwNotFound(String message) {
        throw new RecordNotFoundException(message);
    }

    public void throwInputError(String message) {
        throw new InvalidInputException(message);
    }

    public void throwUserServiceException(String message) {
        throw new UserServiceException(message);
    }

    protected void validateRequest(Object object, String message) {
        if (null == object) {
            throw new InvalidInputException(message);
        }
    }

    protected void validateString(String value, String message) {
        if (ValueUtils.isEmpty(value)) {
            throwInputError(message);
        }
    }

    public void validateNonNegative(Integer value, String message) {
        if (!ValueUtils.isNonNegative(value)) {
            throwInputError(message + ": " + value);
        }
    }

    public void validatePositive(BigDecimal value, String message) {
        if (!ValueUtils.isPositive(value)) {
            throwInputError(message + ": " + value);
        }
    }

    public void validateNonNegative(BigDecimal value, String message) {
        if (!ValueUtils.isNonNegative(value)) {
            throwInputError(message + ": " + value);
        }
    }

    public void validateNonNegative(Long value, String message) {
        if (!ValueUtils.isNonNegative(value)) {
            throwInputError(message + ": " + value);
        }
    }

    public void validateEmptyCollection(Collection<?> value, String message) {
        if (ValueUtils.isEmpty(value)) {
            throwInputError(message + ": " + value);
        }
    }
}