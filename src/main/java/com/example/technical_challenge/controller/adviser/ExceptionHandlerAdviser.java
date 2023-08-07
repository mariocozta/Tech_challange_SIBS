package com.example.technical_challenge.controller.adviser;

import com.example.technical_challenge.dto.ResponseDto;
import com.example.technical_challenge.exception.SIBSRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ExceptionHandlerAdviser extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SIBSRuntimeException.class)
    public ResponseEntity<Object> handleUnprocessableVariantException(SIBSRuntimeException ex,
                                                                      WebRequest request) {
        ResponseDto<Object> body = ResponseDto.wrapper(null, ex.getMessage(),
                ex.getResponseCode());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
