package com.biharigraphic.jilamart.auth.exception_handler;

import com.biharigraphic.jilamart.exception.user.UsernameAlreadyExistsException;
import com.biharigraphic.jilamart.user.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@Component
public class AuthExceptionHandler {
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUsernameExists(UsernameAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409
                .body(Map.of("error", ex.getMessage()));
    }



    @ExceptionHandler(UserException.class)
    public ResponseEntity<Map<String, String>> handleUserException(UserException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }
}
