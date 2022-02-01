package com.appsdeveloperblog.app.ws.mobileappws.shared.utils;

import java.math.BigDecimal;
import java.util.Collection;

public class ValueUtils {

    private ValueUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isNull(Long value) {
        return null == value;
    }

    public static boolean isNull(Integer value) {
        return null == value;
    }

    public static boolean isId(Long value) {
        return null != value && value.longValue() > 0;
    }

    public static boolean isId(Integer value) {
        return isPositive(value);
    }

    public static boolean isPositive(Integer value) {
        return null != value && value.intValue() > 0;
    }

    public static boolean isNonNegative(Integer value) {
        return null != value && value.intValue() >= 0;
    }

    public static boolean isEmpty(String value) {
        return null == value || value.isEmpty();
    }

    public static boolean isEmpty(Object value) {
        return null == value;
    }

    public static boolean isPositive(BigDecimal value) {
        return null != value && value.doubleValue() > 0.0;
    }

    public static boolean isNonNegative(BigDecimal value) {
        return null != value && value.doubleValue() >= 0.0;
    }

    public static boolean isNonNegative(Long value) {
        return null != value && value.longValue() >= 0.0;
    }

    public static boolean isEmpty(Collection<?> collection) {
        return null == collection || collection.isEmpty();
    }
}
