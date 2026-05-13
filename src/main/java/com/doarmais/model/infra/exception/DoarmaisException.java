package com.doarmais.model.infra.exception;

public class DoarmaisException extends RuntimeException {
    public DoarmaisException(String message) {
        super(message);
    }

    public DoarmaisException(String message, Throwable cause) {
        super(message, cause);
    }
}
