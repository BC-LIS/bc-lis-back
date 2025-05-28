package com.bclis.utils.exceptions;

public class InvalidEmailOrUsernameException extends RuntimeException{

    public InvalidEmailOrUsernameException(String message){
        super(message);
    }
}
