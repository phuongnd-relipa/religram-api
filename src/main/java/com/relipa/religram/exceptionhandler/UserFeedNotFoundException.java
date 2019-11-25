package com.relipa.religram.exceptionhandler;

public class UserFeedNotFoundException extends RuntimeException {

    public UserFeedNotFoundException() {
        super();
    }

    public UserFeedNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserFeedNotFoundException(final String message) {
        super(message);
    }

    public UserFeedNotFoundException(final Throwable cause) {
        super(cause);
    }
}
