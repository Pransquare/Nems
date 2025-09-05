package com.pransquare.nems.exceptions;

import java.io.IOException;

public class FileAlreadyExistException extends IOException {

	private static final long serialVersionUID = 1L;

	public FileAlreadyExistException(String message) {
		super(message);
	}

	public FileAlreadyExistException(String message, Throwable cause) {
		super(message, cause);
	}

}
