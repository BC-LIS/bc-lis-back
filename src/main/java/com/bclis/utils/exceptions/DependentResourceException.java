package com.bclis.utils.exceptions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DependentResourceException extends RuntimeException{
    public DependentResourceException(String message) {
        super(message);
    }
}
