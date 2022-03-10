package com.sq.SYTreeHole.exception;

public class LoginException extends RuntimeException{

    private final String message;

    public LoginException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
