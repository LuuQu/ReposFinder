package com.LuuQu.ReposFinder.handler;

import com.LuuQu.ReposFinder.model.ErrorResponse;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(FeignException.NotFound ex) {
        String message = ex.contentUTF8();
        try {
            String content = ex.contentUTF8();
            if (content != null && !content.isBlank()) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(content);
                if (node.has("message")) {
                    message = node.get("message").asString();
                }
            }
        } catch (Exception _) {
        }
        ErrorResponse body = new ErrorResponse(ex.status(), message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
