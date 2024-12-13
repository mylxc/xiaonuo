package com.jackli.common.annotation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AlphaNumericValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AlphaNumeric {

    String message() default "只能使用字母或数据";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

 class AlphaNumericValidator implements ConstraintValidator<AlphaNumeric, String> {
    @Override
    public void initialize(AlphaNumeric constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.matches("^[a-zA-Z0-9]*$");
    }
}