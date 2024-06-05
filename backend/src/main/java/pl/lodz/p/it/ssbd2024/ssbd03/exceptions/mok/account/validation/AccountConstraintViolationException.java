package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.validation;

import lombok.Getter;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

import java.util.HashSet;
import java.util.Set;

@Getter
public class AccountConstraintViolationException extends AccountValidationException {

    private Set<String> violations = new HashSet<String>();

    public AccountConstraintViolationException(Set<String> violations) {
        super(I18n.ACCOUNT_CONSTRAINT_VIOLATION);
        this.violations = violations;
    }

    public AccountConstraintViolationException(Throwable cause, Set<String> violations) {
        super(I18n.ACCOUNT_CONSTRAINT_VIOLATION, cause);
        this.violations = violations;
    }
}
