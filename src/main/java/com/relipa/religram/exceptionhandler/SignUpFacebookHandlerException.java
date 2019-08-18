/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.exceptionhandler;

import com.relipa.religram.error.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.social.ExpiredAuthorizationException;
import org.springframework.social.InvalidAuthorizationException;
import org.springframework.social.ResourceNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SignUpFacebookHandlerException {

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ErrorMessage handleException(ExpiredAuthorizationException exc) {

        ErrorMessage err = new ErrorMessage(401, exc.getMessage());
        return err;
    }

    @ExceptionHandler
    public ErrorMessage handleException(InvalidAuthorizationException exc) {

        ErrorMessage err = new ErrorMessage(401, exc.getMessage());
        return err;
    }

    @ExceptionHandler
    public ErrorMessage handleException(ResourceNotFoundException exc) {

        ErrorMessage err = new ErrorMessage(401, exc.getMessage());
        return err;
    }
}
