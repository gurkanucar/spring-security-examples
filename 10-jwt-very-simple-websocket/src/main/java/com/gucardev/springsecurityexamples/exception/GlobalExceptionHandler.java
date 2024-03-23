package com.gucardev.springsecurityexamples.exception;

import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<Map<String, Object>> handleAllException(
      Exception ex, WebRequest request) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public final ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidEx(
      MethodArgumentNotValidException ex, WebRequest request) {
    return getMapResponseEntity(ex);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public final ResponseEntity<Map<String, Object>> handleConstraintViolationEx(
      MethodArgumentNotValidException ex, WebRequest request) {
    return getMapResponseEntity(ex);
  }

  private ResponseEntity<Map<String, Object>> getMapResponseEntity(
      MethodArgumentNotValidException ex) {
    Map<String, Object> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            x -> {
              String errorMessage = x.getDefaultMessage();
              if (x instanceof FieldError) {
                String fieldName = ((FieldError) x).getField();
                errors.put(fieldName, errorMessage);
              } else {
                String objectName = x.getObjectName();
                errors.put(objectName, errorMessage);
              }
            });
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }
}
