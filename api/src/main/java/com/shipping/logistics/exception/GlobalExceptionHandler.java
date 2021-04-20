package com.shipping.logistics.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import java.util.Set;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFound(Exception e) {
		return buildResponseEntity(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgument(Exception e) {
		return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> handleConstraintViolation(Exception e) {
		return buildResponseEntity(new ErrorResponse(HttpStatus.CONFLICT.value(), "Ship name already exists"));
	}

	@ExceptionHandler(javax.validation.ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> handleFieldConstraintViolation(javax.validation.ConstraintViolationException e) {
		Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
		String errorMessage = constraintViolations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(","));

		return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage));
	}

	private ResponseEntity<ErrorResponse> buildResponseEntity(ErrorResponse errorResponse) {
		return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getErrorCode()));
	}

	static class ErrorResponse {
		private final int errorCode;
		private final String message;

		public ErrorResponse(int errorCode, String message) {
			this.errorCode = errorCode;
			this.message = message;
		}

		public int getErrorCode() {
			return errorCode;
		}

		public String getMessage() {
			return message;
		}
	}
}
