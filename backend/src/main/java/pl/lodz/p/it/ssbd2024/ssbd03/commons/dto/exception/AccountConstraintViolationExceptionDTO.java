package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.validation.AccountConstraintViolationException;

import java.util.Set;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountConstraintViolationExceptionDTO {

    private String message;
    private Set<String> violations;

    public AccountConstraintViolationExceptionDTO(AccountConstraintViolationException exception) {
        this.message = exception.getMessage();
        this.violations = exception.getViolations();
    }
}
