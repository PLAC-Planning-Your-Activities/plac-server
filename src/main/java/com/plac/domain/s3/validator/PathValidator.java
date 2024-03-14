package com.plac.domain.s3.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PathValidator implements ConstraintValidator<ValidateImagePath, String> {

    private ValidateImagePath validateImagePath;

    @Override
    public void initialize(ValidateImagePath constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Enum<?>[] enumValues = this.validateImagePath.enumClass().getEnumConstants();

        if (enumValues != null) {
            for (Object enumValue : enumValues) {
                if (value.equals(enumValue.toString()) ||
                        this.validateImagePath.ignoreCase() && value.equalsIgnoreCase(enumValue.toString())
                ) {
                    return true;
                }
            }
        }

        return false;
    }
}
