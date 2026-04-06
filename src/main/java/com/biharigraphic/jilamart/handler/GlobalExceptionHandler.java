package com.biharigraphic.jilamart.handler;

import com.biharigraphic.jilamart.exception.base.AppException;
import com.biharigraphic.jilamart.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //base exception handler
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<?>> handleAppException(AppException ex) {
        return ResponseEntity.badRequest().body(
                ApiResponse.builder()
                        .success(false)
                        .message(ex.getMessage())
                        .errorCode(ex.getErrorCode())
                        .data(null)
                        .build()
        );
    }


    // ✅ Validation errors (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationErrors(MethodArgumentNotValidException ex) {

        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse.builder()
                        .success(false)
                        .message(errors)
                        .errorCode("VALIDATION_ERROR")
                        .data(null)
                        .build()
        );
    }


    // ✅ Catch all (last fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.builder()
                        .success(false)
                        .message("Something went wrong")
                        .errorCode("INTERNAL_ERROR")
                        .data(null)
                        .build()
        );
    }
}