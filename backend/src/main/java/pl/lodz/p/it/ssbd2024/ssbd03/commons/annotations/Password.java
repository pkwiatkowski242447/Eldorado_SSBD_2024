package pl.lodz.p.it.ssbd2024.ssbd03.commons.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.mok.AccountsConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mok.AccountMessages;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(value = {ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = AccountMessages.PASSWORD_BLANK)
@Size(min = AccountsConsts.PASSWORD_LENGTH, max = AccountsConsts.PASSWORD_LENGTH, message = AccountMessages.PASSWORD_INVALID_LENGTH)
@Constraint(validatedBy = {})
public @interface Password {

    String message() default AccountMessages.PASSWORD_INVALID;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default {};
}

