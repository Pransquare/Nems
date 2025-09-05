package com.pransquare.nems.exceptions;

public class ExcelReadException extends RuntimeException {
	private static final long serialVersionUID = 1l;

	public ExcelReadException(String message) {
		super(message);
	}

	public ExcelReadException(String message, Throwable cause) {
		super(message, cause);
	}
}