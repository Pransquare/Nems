package com.pransquare.nems.exceptions;

public class FileStorageException extends RuntimeException {

	static final long serialVersionUID = 1L;

	public FileStorageException(String message) {
	        super(message);
	    }

	    public FileStorageException(String message, Throwable cause) {
	        super(message, cause);
	    }
}
