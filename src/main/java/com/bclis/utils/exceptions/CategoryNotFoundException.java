package com.bclis.utils.exceptions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException(String message){
        super(message);
    }
}
