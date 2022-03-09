package com.sq.SYTreeHole.exception;


public class PublishControllerException extends RuntimeException{

    private final String message;

    public PublishControllerException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
