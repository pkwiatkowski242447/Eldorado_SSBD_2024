package pl.lodz.p.it.ssbd2024.ssbd03.aspects.exception.facade;

import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.postgresql.util.PSQLException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationDatabaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationInternalServerErrorException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.validation.AccountConstraintViolationException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.conflict.AccountEmailAlreadyTakenException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.conflict.AccountLoginAlreadyTakenException;

import java.util.HashSet;
import java.util.Set;

@Aspect
@Order(10)
@Component
public class AccountFacadeAspect {

    /**
     * Pointcut definition for any method inside AccountMOKFacade.
     */
    @Pointcut(value = "within(pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountMOKFacade)")
    private void accountMokFacadeMethodPointcut() {}

    /**
     * Pointcut definition for any method inside AuthenticationFacade.
     */
    @Pointcut(value = "within(pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AuthenticationFacade)")
    private void authenticationFacadeMethodPointcut() {}

    /**
     * This method is executed when either accountMokFacadeMethodPointcut or authenticationFacadeMethodPointcut was
     * triggered. The main responsibility is to transform exceptions related to user account into other exceptions
     * in order to standardize the exception handling in the application.
     *
     * @param proceedingJoinPoint Join point used to execute method that was being intercepted by this aspect.
     *
     * @return Value returned by the intercepted method.
     *
     * @throws Exception When exception is thrown in the intercepted method, then this method will transform in
     * into other, checked exception and propagate further, or rethrow them in order to process them in the next aspect.
     */
    @Around(value = "accountMokFacadeMethodPointcut() || authenticationFacadeMethodPointcut()")
    private Object handleAccountRelatedMethodsExceptions(ProceedingJoinPoint proceedingJoinPoint) throws Exception {
        try {
            return proceedingJoinPoint.proceed();
        } catch (OptimisticLockException optimisticLockException) {
            throw optimisticLockException;
        } catch (PersistenceException | PSQLException exception) {
            Throwable exceptionCopy = exception;
            do {
                if (exceptionCopy.getMessage().contains("account_login_key")) {
                    throw new AccountLoginAlreadyTakenException();
                } else if (exceptionCopy.getMessage().contains("account_email_key")) {
                    throw new AccountEmailAlreadyTakenException();
                }
                exceptionCopy = exceptionCopy.getCause();
            } while (exceptionCopy != null);
            throw new ApplicationDatabaseException(exception);
        } catch (ConstraintViolationException constraintViolationException) {
            Set<String> violations = new HashSet<>();
            for (ConstraintViolation<?> constraintViolation : constraintViolationException.getConstraintViolations()) {
                violations.add(constraintViolation.getMessage());
            }
            throw new AccountConstraintViolationException(violations);
        } catch (Throwable throwable) {
            throw new ApplicationInternalServerErrorException();
        }
    }
}
