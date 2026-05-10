package annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsbnValidator implements ConstraintValidator<ValidIsbn, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return (value == null) ? false : value.matches("\\d{13}");
    }

    @Override
    public void initialize(ValidIsbn constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
