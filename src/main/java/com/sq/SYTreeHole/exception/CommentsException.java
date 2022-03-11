package com.sq.SYTreeHole.exception;

public class CommentsException extends RuntimeException{

    private final String message;

    public CommentsException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}