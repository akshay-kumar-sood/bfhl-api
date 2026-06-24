package com.chitkara.bfhl.exception;

import com.chitkara.bfhl.dto.BfhlResponse;
import java.util.Collections;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BfhlResponse> handleInvalidJson(HttpMessageNotReadableException ex) {
        return ResponseEntity.ok(buildErrorResponse());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BfhlResponse> handleGenericException(Exception ex) {
        return ResponseEntity.ok(buildErrorResponse());
    }

    private BfhlResponse buildErrorResponse() {
        BfhlResponse response = new BfhlResponse();
        response.setSuccess(false);
        response.setOddNumbers(Collections.emptyList());
        response.setEvenNumbers(Collections.emptyList());
        response.setAlphabets(Collections.emptyList());
        response.setSpecialCharacters(Collections.emptyList());
        response.setSum("0");
        response.setConcatString("");
        return response;
    }
}
