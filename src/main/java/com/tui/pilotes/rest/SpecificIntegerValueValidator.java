package com.tui.pilotes.rest;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SpecificIntegerValueValidator implements ConstraintValidator<SpecificIntegerValue, Integer> {

    private int[] values;

    @Override
    public void initialize(SpecificIntegerValue constraintAnnotation) {
        this.values = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        for (int validValue : values) {
            if (value == validValue) {
                return true;
            }
        }
        return false;
    }
}