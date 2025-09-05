package com.pransquare.nems.utils;

import java.time.LocalDate;

public class StringUtil {

	// Private constructor to prevent instantiation
    private StringUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
	public static Boolean isNotNull(String str) {
        return str != null && !str.isEmpty() && !str.equals("");
    }

    public static Boolean isNotNull(LocalDate str) {
        return str != null;
    }
}
