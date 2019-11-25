package com.relipa.religram.exceptionhandler;

public class PageInvalidException extends RuntimeException {

    public PageInvalidException() {
        super();
    }

    public PageInvalidException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PageInvalidException(final String message) {
        super(message);
    }

    public PageInvalidException(final Throwable cause) {
        super(cause);
    }
}
