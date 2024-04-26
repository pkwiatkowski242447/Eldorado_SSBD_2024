package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account;

import lombok.Getter;

import java.util.Set;

/**
 * Used to specify an Exception related to problems with validation of the Account.
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account
 */
@Getter
public class AccountValidationException extends Exception {

    /**
     * Used to store information about the incorrect fields.
     *
     * @param field Name of the field.
     * @param value Incorrect value passed to the Account.
     * @param message Description of the violation.
     */
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
