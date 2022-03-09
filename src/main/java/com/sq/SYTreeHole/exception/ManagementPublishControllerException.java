package com.sq.SYTreeHole.exception;

public class ManagementPublishControllerException extends RuntimeException{

    private final String message;

    public ManagementPublishControllerException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}