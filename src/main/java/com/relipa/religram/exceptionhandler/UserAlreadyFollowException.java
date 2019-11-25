package com.relipa.religram.exceptionhandler;

public class UserAlreadyFollowException extends RuntimeException {

    public UserAlreadyFollowException() {
        super();
    }

    public UserAlreadyFollowException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyFollowException(final String message) {
        super(message);
    }

    public UserAlreadyFollowException(final Throwable cause) {
        super(cause);
    }
}
