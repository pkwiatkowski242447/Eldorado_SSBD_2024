package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.validation.AccountConstraintViolationException;

import java.util.Set;

@Getter @Setter
@NoArgsConstructor
public class AccountConstraintViolationExceptionDTO  extends ExceptionDTO {

    private Set<String> violations;

    public AccountConstraintViolationExceptionDTO(AccountConstraintViolationException exception) {
        super(exception);
        this.violations = exception.getViolations();
    }
}
