package com.nuhi.Nuhi.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ConcurrentModificationException;

// GlobalExceptionHandler.java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {
            EmailAlreadyExistsException.class,
            UserNotFoundException.class,
            InvalidCredentialsException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestExceptions(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("Access denied"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalError(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Internal server error"));
    }

    @ExceptionHandler(ConcurrentModificationException.class)
    public ResponseEntity<String> handleConcurrentModification(ConcurrentModificationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<String> handleOptimisticLocking(ObjectOptimisticLockingFailureException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Data was modified by another user. Please refresh and try again.");
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ErrorResponse> handleTransactionException(TransactionSystemException ex) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(ex); // Changed here
        String message = "Database operation failed";

        if (rootCause instanceof ConstraintViolationException) {
            message = "Data validation failed: " + rootCause.getMessage();
        } else if (rootCause instanceof DataIntegrityViolationException) {
            message = "Data integrity violation: " + rootCause.getMessage();
        }

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(message));
    }
}

