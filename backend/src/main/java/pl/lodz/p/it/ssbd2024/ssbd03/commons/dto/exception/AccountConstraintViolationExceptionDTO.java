package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.exception;

import lombok.Getter;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.validation.AccountConstraintViolationException;

import java.util.Set;

@Getter
public class AccountConstraintViolationExceptionDTO {

    private final String message;
    private final Set<String> violations;

    public AccountConstraintViolationExceptionDTO(AccountConstraintViolationException exception) {
        this.message = exception.getMessage();
        this.violations = exception.getViolations();
    }
}
