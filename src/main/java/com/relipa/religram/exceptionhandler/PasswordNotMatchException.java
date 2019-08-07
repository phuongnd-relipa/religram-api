/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.exceptionhandler;

public class PasswordNotMatchException extends RuntimeException {

    public PasswordNotMatchException() {
        super();
    }

    public PasswordNotMatchException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PasswordNotMatchException(final String message) {
        super(message);
    }

    public PasswordNotMatchException(final Throwable cause) {
        super(cause);
    }
}
