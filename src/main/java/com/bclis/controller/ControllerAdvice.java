package com.bclis.controller;

import com.bclis.dto.response.ErrorResponseDTO;
import com.bclis.utils.exceptions.AlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = AlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> requestExceptionHandler(AlreadyExistsException ex){
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .code("409")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}
