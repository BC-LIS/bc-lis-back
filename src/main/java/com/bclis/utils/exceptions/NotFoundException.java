package com.bclis.utils.exceptions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NotFoundException extends RuntimeException{
    public NotFoundException(String message){
        super(message);
    }
}
