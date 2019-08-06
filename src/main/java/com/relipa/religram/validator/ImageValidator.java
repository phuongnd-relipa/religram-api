/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.validator;

import com.relipa.religram.constant.Constant;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageValidator implements ConstraintValidator<ValidImage, String> {

    @Override
    public void initialize(ValidImage constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return validateUserName(value);
    }

    private boolean validateUserName(final String imageString) {
        if (imageString == null || imageString.isEmpty()) {
            return true;
        }

        Pattern pattern = Pattern.compile(Constant.IMAGE_BASE64_PATTERN);
        Matcher matcher = pattern.matcher(imageString);
        return matcher.matches();
    }
}
