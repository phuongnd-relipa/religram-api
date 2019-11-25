package com.relipa.religram.exceptionhandler;

public class UserNotFollowException extends RuntimeException {

    public UserNotFollowException() {
        super();
    }

    public UserNotFollowException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserNotFollowException(final String message) {
        super(message);
    }

    public UserNotFollowException(final Throwable cause) {
        super(cause);
    }
}
