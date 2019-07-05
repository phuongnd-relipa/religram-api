package com.relipa.religram.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator implements ConstraintValidator<ValidUserName, String> {

    private static final String USERNAME_PATTERN = "^[a-z0-9]+$";

    @Override
    public void initialize(final ValidUserName constraintAnnotation) {
    }

    @Override
    public boolean isValid(final String userName, final ConstraintValidatorContext context) {
        return (validateUserName(userName));
    }

    private boolean validateUserName(final String userName) {
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(userName);
        return matcher.matches();
    }
}
