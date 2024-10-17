package com.bclis.utils.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Setter
@Getter
public class AlreadyExistsException extends RuntimeException {
    private final String code;
    private final HttpStatus status;

    public AlreadyExistsException(String code, HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }
}
