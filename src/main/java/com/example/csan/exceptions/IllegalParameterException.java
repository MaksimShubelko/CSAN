package com.example.csan.exceptions;

public class IllegalParameterException extends Exception {
    public IllegalParameterException() {
        super();
    }

    public IllegalParameterException(String message) {
        super(message);
    }

    public IllegalParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalParameterException(Throwable cause) {
        super(cause);
    }

    protected IllegalParameterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
