package com.bclis.controller;

import com.bclis.dto.response.ErrorResponseDTO;
import com.bclis.utils.exceptions.AlreadyExistsException;
import com.bclis.utils.exceptions.InvalidAttributeException;
import com.bclis.utils.exceptions.NotFoundException;
import com.bclis.utils.exceptions.DependentResourceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @ExceptionHandler(value = DependentResourceException.class)
    public ResponseEntity<ErrorResponseDTO> requestExceptionHandler(DependentResourceException ex){
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .code("409")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> requestExceptionHandler(NotFoundException ex){
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .code("404")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> requestExceptionHandler(UsernameNotFoundException ex){
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .code("404")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> requestExceptionHandler(BadCredentialsException ex){
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .code("401")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = InvalidAttributeException.class)
    public ResponseEntity<ErrorResponseDTO> requestExceptionHandler(InvalidAttributeException ex){
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .code("400")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
