package pl.lodz.p.it.ssbd2024.ssbd03.commons.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
@NotBlank(message = AccountMessages.LOGIN_BLANK)
@Pattern(regexp = AccountsConsts.LOGIN_REGEX, message = AccountMessages.LOGIN_REGEX_NOT_MET)
@Size(min = AccountsConsts.LOGIN_MIN_LENGTH, message = AccountMessages.LOGIN_TOO_SHORT)
@Size(max = AccountsConsts.LOGIN_MAX_LENGTH, message = AccountMessages.LOGIN_TOO_LONG)
@Constraint(validatedBy = {})
public @interface Login {

    String message() default AccountMessages.LOGIN_INVALID;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default {};
}

