package com.polevchshikov.studentapi.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Turns exceptions into clear JSON error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(StudentNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleNotFound(StudentNotFoundException ex, HttpServletRequest request) {
		return error(HttpStatus.NOT_FOUND, ex.getMessage(), request);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex,
			HttpServletRequest request) {
		Map<String, String> fields = new LinkedHashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(e -> fields.put(e.getField(), e.getDefaultMessage()));
		ResponseEntity<Map<String, Object>> response = error(HttpStatus.BAD_REQUEST, "Validation failed", request);
		response.getBody().put("fields", fields);
		return response;
	}

	private ResponseEntity<Map<String, Object>> error(HttpStatus status, String message, HttpServletRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", Instant.now().toString());
		body.put("status", status.value());
		body.put("error", status.getReasonPhrase());
		body.put("message", message);
		body.put("path", request.getRequestURI());
		return ResponseEntity.status(status).body(body);
	}

}
