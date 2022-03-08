package com.sq.SYTreeHole.exception;

public class LoginControllerException extends RuntimeException{

    private final String message;

    public LoginControllerException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
