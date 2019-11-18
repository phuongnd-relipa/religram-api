/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.exceptionhandler;

public class FacebookNotFoundException extends RuntimeException {

    public FacebookNotFoundException() {
        super();
    }

    public FacebookNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public FacebookNotFoundException(final String message) {
        super(message);
    }

    public FacebookNotFoundException(final Throwable cause) {
        super(cause);
    }
}
