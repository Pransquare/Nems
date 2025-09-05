package com.pransquare.nems.exceptions;

import java.io.FileNotFoundException;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class AppExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		ErrorResponse errorDetails = new ErrorResponse();
		errorDetails.setStatus(HttpStatus.NOT_FOUND.value());
		errorDetails.setTimestamp(new Date());
		errorDetails.setMessage(ex.getMessage());
		errorDetails.setDetails(request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> globleExcpetionHandler(Exception ex, WebRequest request) {
		ErrorResponse errorDetails = new ErrorResponse();
		errorDetails.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		errorDetails.setTimestamp(new Date());
		errorDetails.setMessage(ex.getMessage());
		errorDetails.setDetails(request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = FileNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleFileNotFoundException(FileNotFoundException fileNotFoundException,
			WebRequest webRequest) {
		ErrorResponse errorDetails = new ErrorResponse();
		errorDetails.setStatus(HttpStatus.NOT_FOUND.value());
		errorDetails.setTimestamp(new Date());
		errorDetails.setMessage(fileNotFoundException.getMessage());
		errorDetails.setDetails(webRequest.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = NullPointerException.class)
	public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException nullPointerException,
			WebRequest webRequest) {
		ErrorResponse errorDetails = new ErrorResponse();
		errorDetails.setStatus(HttpStatus.NO_CONTENT.value());
		errorDetails.setTimestamp(new Date());
		errorDetails.setMessage(nullPointerException.getMessage());
		errorDetails.setDetails(webRequest.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.NO_CONTENT);

	}
	
	@ExceptionHandler(ExcelReadException.class)
	public ResponseEntity<ErrorResponse> handleExcelReadException(ExcelReadException ex, WebRequest request) {
	    ErrorResponse errorDetails = new ErrorResponse();
	    errorDetails.setStatus(HttpStatus.BAD_REQUEST.value()); // or whatever status you prefer
	    errorDetails.setTimestamp(new Date());
	    errorDetails.setMessage(ex.getMessage());
	    errorDetails.setDetails(request.getDescription(false));
	    return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

}
