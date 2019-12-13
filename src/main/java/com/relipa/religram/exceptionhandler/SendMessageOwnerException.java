/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.exceptionhandler;

public class SendMessageOwnerException extends RuntimeException {

    public SendMessageOwnerException() {
        super();
    }

    public SendMessageOwnerException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public SendMessageOwnerException(final String message) {
        super(message);
    }

    public SendMessageOwnerException(final Throwable cause) {
        super(cause);
    }
}
