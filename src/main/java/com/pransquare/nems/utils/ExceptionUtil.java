package com.pransquare.nems.utils;

import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExceptionUtil {
	
	// Private constructor to prevent instantiation
    private ExceptionUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

	public static void handleException(HttpServletResponse response, String message, HttpStatus status) throws IOException {
        if (!response.isCommitted()) {
            response.setStatus(status.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + message + "\"}");
        }
    }
}
