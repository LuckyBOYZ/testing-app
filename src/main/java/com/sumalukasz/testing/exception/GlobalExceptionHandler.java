package com.sumalukasz.testing.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidPageNumberException.class)
    public ResponseEntity<Object> handleInvalidPageNumberException(InvalidPageNumberException ex) {
        Map<String, Object> errorBody = Map.of(
                "errorMessage", ex.getMessage(), "pageNumber", ex.getPageNumber());
        return ResponseEntity.badRequest().body(errorBody);
    }

    @ExceptionHandler(InvalidOffsetNumberException.class)
    public ResponseEntity<Object> handleInvalidOffsetNumberException(InvalidOffsetNumberException ex) {
        Map<String, Object> errorBody = Map.of(
                "errorMessage", ex.getMessage(), "offset", ex.getOffset());
        return ResponseEntity.badRequest().body(errorBody);
    }

    @ExceptionHandler()
    public ResponseEntity<Object> handleNoValidProfileException(NoValidProfileException ex) {
        Map<String, Object> errorBody = Map.of(
                "errorMessage", ex.getMessage(), "activeProfile", ex.getActiveProfile());
        return ResponseEntity.internalServerError().body(errorBody);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        return ResponseEntity.internalServerError().body(Map.of("errorMessage", ex.getMessage()));
    }

}
