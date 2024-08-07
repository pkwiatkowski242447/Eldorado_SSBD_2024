package pl.lodz.p.it.ssbd2024.ssbd03.commons.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.validators.JWTConstraintValidation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = JWTConstraintValidation.class)
@Target(value = {ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsJsonWebToken {

    String message() default "{jakarta.validation.constraint.IsJsonWebToken.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default {};
}
