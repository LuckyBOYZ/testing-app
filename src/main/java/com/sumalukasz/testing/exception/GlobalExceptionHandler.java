package com.sumalukasz.testing.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        super();
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(InvalidPageNumberException.class)
    public ResponseEntity<Object> handleInvalidPageNumberException(InvalidPageNumberException ex) {
        Map<String, Object> errorBody = createErrorBody(ex.getMessage(), ex.getPageNumber());
        return ResponseEntity.badRequest().body(errorBody);
    }

    @ExceptionHandler(InvalidOffsetNumberException.class)
    public ResponseEntity<Object> handleInvalidOffsetNumberException(InvalidOffsetNumberException ex) {
        Map<String, Object> errorBody = createErrorBody(ex.getMessage(), ex.getOffset());
        return ResponseEntity.badRequest().body(errorBody);
    }

    @ExceptionHandler()
    public ResponseEntity<Object> handleNoValidProfileException(NoValidProfileException ex) {
        Map<String, Object> errorBody = createErrorBody(ex.getMessage(), ex.getActiveProfile());
        return ResponseEntity.internalServerError().body(errorBody);
    }

    @ExceptionHandler(InvalidEmployeeIdException.class)
    public ResponseEntity<Object> handleInvalidEmployeeIdException(InvalidEmployeeIdException ex) {
        Map<String, Object> errorBody = createErrorBody(ex.getMessage(), ex.getId());
        return ResponseEntity.badRequest().body(errorBody);
    }

    @ExceptionHandler(InvalidParameterValueException.class)
    public ResponseEntity<Object> handleInvalidParameterValueExceptionAndTooHighNumberValueException(InvalidParameterValueException ex) {
        Map<String, Object> errorBody = createErrorBody(ex.getMessage(), ex.getValue());
        return ResponseEntity.badRequest().body(errorBody);
    }

    @ExceptionHandler(TooHighNumberValueException.class)
    public ResponseEntity<Object> handleInvalidParameterValueExceptionAndTooHighNumberValueException(TooHighNumberValueException ex) {
        Map<String, Object> errorBody = createErrorBody(ex.getMessage(), ex.getValue());
        return ResponseEntity.badRequest().body(errorBody);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ignore) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(InvalidDepartmentIdException.class)
    public ResponseEntity<Object> handleInvalidDepartmentIdException(InvalidDepartmentIdException ex) {
        Map<String, Object> errorBody = createErrorBody(ex.getMessage(), ex.getDepartmentId());
        return ResponseEntity.badRequest().body(errorBody);
    }

    @ExceptionHandler(InvalidRequestBodyException.class)
    public ResponseEntity<Object> handleInvalidRequestBodyException(InvalidRequestBodyException ex) throws JsonProcessingException {
        Map<String, Object> errorBody = createErrorBody(ex.getMessage(), objectMapper.writeValueAsString(ex.getInvalidFields()));
        return ResponseEntity.badRequest().body(errorBody);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        Map<String, Object> errorBody = createErrorBody(ex.getMessage(), null);
        return ResponseEntity.internalServerError().body(errorBody);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, Object> errorBody = createErrorBody("The body in the request is required", null);
        return ResponseEntity.badRequest().body(errorBody);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, Object> errorBody = createErrorBody(ex.getMessage(), null);
        return ResponseEntity.status(status).body(errorBody);
    }

    private static Map<String, Object> createErrorBody(String errorMessage, Object value) {
        if (value == null) {
            return Map.of(
                    "errorMessage", errorMessage);
        } else {
            return Map.of(
                    "errorMessage", errorMessage, "value", value);
        }
    }
}
