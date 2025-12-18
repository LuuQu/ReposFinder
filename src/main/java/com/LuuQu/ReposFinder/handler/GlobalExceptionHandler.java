package com.LuuQu.ReposFinder.handler;

import com.LuuQu.ReposFinder.model.ErrorResponse;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(FeignException.NotFound ex) {
        String message =ex.contentUTF8();
        if(message == null || message.isBlank()) {
            message = ex.getMessage();
        }
        ErrorResponse body = new ErrorResponse(ex.status(), message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
