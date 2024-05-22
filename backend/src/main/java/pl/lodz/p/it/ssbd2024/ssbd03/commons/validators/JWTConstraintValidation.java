package pl.lodz.p.it.ssbd2024.ssbd03.commons.validators;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.annotations.IsJsonWebToken;

public class JWTConstraintValidation implements ConstraintValidator<IsJsonWebToken, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            JWT.decode(value);
        } catch (JWTDecodeException jwtDecodeException) {
            return false;
        }
        return true;
    }
}
