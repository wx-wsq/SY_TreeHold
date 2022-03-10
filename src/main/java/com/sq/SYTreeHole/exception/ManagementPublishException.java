package com.sq.SYTreeHole.exception;

public class ManagementPublishException extends RuntimeException{

    private final String message;

    public ManagementPublishException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}