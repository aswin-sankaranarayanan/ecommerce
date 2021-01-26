package com.ecommerce.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler{

	private static final Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		StringBuffer allErrors = new StringBuffer();
		BindingResult bindingResult = ex.getBindingResult();
		bindingResult.getAllErrors().forEach(error->allErrors.append(error.getDefaultMessage()));
		logger.error("Exception Occured -->"+allErrors.toString());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(allErrors.toString());
	}
	
	@ExceptionHandler(ApplicationException.class)
	protected ResponseEntity<Object> handleApplicationException(ApplicationException ex) {
		ex.printStackTrace();
		logger.error("Exception Occured -->"+ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}
	
}
