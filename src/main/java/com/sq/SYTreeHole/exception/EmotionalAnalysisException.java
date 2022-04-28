package com.sq.SYTreeHole.exception;

public class EmotionalAnalysisException extends RuntimeException {

    private final String message;

    public EmotionalAnalysisException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
