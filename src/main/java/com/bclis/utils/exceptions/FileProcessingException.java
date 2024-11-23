package com.bclis.utils.exceptions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileProcessingException extends RuntimeException {
    public FileProcessingException(String message) {
        super(message);
    }
}
