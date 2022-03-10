package com.sq.SYTreeHole.exception;


public class PublishListException extends RuntimeException{

    private final String message;

    public PublishListException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
