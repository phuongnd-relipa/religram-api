/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.validator;

import com.relipa.religram.constant.Constant;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator implements ConstraintValidator<ValidUserName, String> {

    @Override
    public void initialize(ValidUserName constraintAnnotation) {

    }

    @Override
    public boolean isValid(String userName, ConstraintValidatorContext context) {
        return validateUserName(userName);
    }

    private boolean validateUserName(final String userName) {
        Pattern pattern = Pattern.compile(Constant.USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(userName);
        return matcher.matches();
    }
}
