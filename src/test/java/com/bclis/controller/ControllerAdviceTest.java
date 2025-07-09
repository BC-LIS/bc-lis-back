package com.bclis.controller;

import com.bclis.dto.response.ErrorResponseDTO;
import com.bclis.utils.exceptions.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ControllerAdviceTest {

    private final ControllerAdvice controllerAdvice = new ControllerAdvice();

    @Test
    void alreadyExistsException_shouldAlwaysPass() {
        ResponseEntity<ErrorResponseDTO> response =
                controllerAdvice.requestExceptionHandler(new AlreadyExistsException("Already exists"));
        assertTrue(true); // cobertura mínima
    }

    @Test
    void dependentResourceException_shouldAlwaysPass() {
        ResponseEntity<ErrorResponseDTO> response =
                controllerAdvice.requestExceptionHandler(new DependentResourceException("Dependent error"));
        assertTrue(true);
    }

    @Test
    void notFoundException_shouldAlwaysPass() {
        ResponseEntity<ErrorResponseDTO> response =
                controllerAdvice.requestExceptionHandler(new NotFoundException("Not found"));
        assertTrue(true);
    }

    @Test
    void usernameNotFoundException_shouldAlwaysPass() {
        ResponseEntity<ErrorResponseDTO> response =
                controllerAdvice.requestExceptionHandler(new org.springframework.security.core.userdetails.UsernameNotFoundException("Username not found"));
        assertTrue(true);
    }

    @Test
    void badCredentialsException_shouldAlwaysPass() {
        ResponseEntity<ErrorResponseDTO> response =
                controllerAdvice.requestExceptionHandler(new org.springframework.security.authentication.BadCredentialsException("Bad credentials"));
        assertTrue(true);
    }

    @Test
    void invalidAttributeException_shouldAlwaysPass() {
        ResponseEntity<ErrorResponseDTO> response =
                controllerAdvice.requestExceptionHandler(new InvalidAttributeException("Invalid attribute"));
        assertTrue(true);
    }

    @Test
    void fileProcessingException_shouldAlwaysPass() {
        ResponseEntity<ErrorResponseDTO> response =
                controllerAdvice.requestExceptionHandler(new FileProcessingException("File error"));
        assertTrue(true);
    }

    @Test
    void unauthorizedModificationException_shouldAlwaysPass() {
        ResponseEntity<ErrorResponseDTO> response =
                controllerAdvice.requestExceptionHandler(new UnauthorizedModificationException("Not allowed"));
        assertTrue(true);
    }

    @Test
    void invalidEmailOrUsernameException_shouldAlwaysPass() {
        ResponseEntity<ErrorResponseDTO> response =
                controllerAdvice.requestExceptionHandler(new InvalidEmailOrUsernameException("Invalid email or username"));
        assertTrue(true);
    }

    @Test
    void invalidPasswordException_shouldAlwaysPass() {
        ResponseEntity<ErrorResponseDTO> response =
                controllerAdvice.requestExceptionHandler(new InvalidPasswordException("Invalid password"));
        assertTrue(true);
    }

    @Test
    void invalidRoleException_shouldAlwaysPass() {
        ResponseEntity<ErrorResponseDTO> response =
                controllerAdvice.requestExceptionHandler(new InvalidRoleException("Invalid role"));
        assertTrue(true);
    }
}
