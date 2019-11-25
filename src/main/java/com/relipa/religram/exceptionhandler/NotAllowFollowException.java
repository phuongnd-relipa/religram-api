package com.relipa.religram.exceptionhandler;

public class NotAllowFollowException extends RuntimeException {

    public NotAllowFollowException() {
        super();
    }

    public NotAllowFollowException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NotAllowFollowException(final String message) {
        super(message);
    }

    public NotAllowFollowException(final Throwable cause) {
        super(cause);
    }
}
