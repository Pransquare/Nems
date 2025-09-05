package com.pransquare.nems.utils;

public class IntegerUtils {
	
	// Private constructor to prevent instantiation
    private IntegerUtils() {
    	throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	public static boolean isNotNull(Integer value) {
        return value != null && value != 0;
    }

    public static boolean isNotNull(Long value) {
        return value != null && value != 0;
    }

    public static boolean isNotNull(Float value) {
        return value != null && value != 0;
    }

    public static boolean isNotNull(Double value) {
        return value != null && value != 0;
    }
}
