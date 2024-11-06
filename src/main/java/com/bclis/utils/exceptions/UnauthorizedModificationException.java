package com.bclis.utils.exceptions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UnauthorizedModificationException extends RuntimeException{
    public UnauthorizedModificationException(String message) {
        super(message);
    }
}
