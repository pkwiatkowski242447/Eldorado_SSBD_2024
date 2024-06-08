package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.validation.AccountConstraintViolationException;

import java.util.Set;

@Getter @Setter
@NoArgsConstructor
@LoggerInterceptor
public class AccountConstraintViolationExceptionDTO  extends ExceptionDTO {

    private Set<String> violations;

    public AccountConstraintViolationExceptionDTO(AccountConstraintViolationException exception) {
        super(exception);
        this.violations = exception.getViolations();
    }
}
