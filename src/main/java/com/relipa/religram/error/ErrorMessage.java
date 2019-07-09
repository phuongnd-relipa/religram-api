package com.relipa.religram.error;


public class ErrorMessage<T> {
    private int statusCode;
    private T message;

    public ErrorMessage(int statusCode, T message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }
}
