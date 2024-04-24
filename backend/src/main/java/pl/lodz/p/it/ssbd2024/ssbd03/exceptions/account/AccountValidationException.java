package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account;

import lombok.Getter;

import java.util.Set;

@Getter
public class AccountValidationException extends Exception {

    public record FieldConstraintViolation(String field, String value, String message) {
    }

    private final Set<FieldConstraintViolation> constraintViolations;

    public AccountValidationException(String message, Set<FieldConstraintViolation> constraintViolations) {
        super(message);
        this.constraintViolations = constraintViolations;
    }

    public AccountValidationException(String message, Throwable cause, Set<FieldConstraintViolation> constraintViolations) {
        super(message, cause);
        this.constraintViolations = constraintViolations;
    }
}
