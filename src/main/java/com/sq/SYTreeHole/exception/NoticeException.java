package com.sq.SYTreeHole.exception;

public class NoticeException extends RuntimeException{
    private final String message;

    public NoticeException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
