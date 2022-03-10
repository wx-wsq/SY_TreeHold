package com.sq.SYTreeHole.exception;

public class PublishDetailException  extends RuntimeException{

    private final String message;

    public PublishDetailException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}

