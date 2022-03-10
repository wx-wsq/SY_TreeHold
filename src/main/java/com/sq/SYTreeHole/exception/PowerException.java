package com.sq.SYTreeHole.exception;

public class PowerException extends RuntimeException{

    private final String message;

    public PowerException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
