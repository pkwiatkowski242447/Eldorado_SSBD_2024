package pl.lodz.p.it.ssbd2024.ssbd03.aspects.exception.facade;

import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.postgresql.util.PSQLException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationDatabaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationInternalServerErrorException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.token.conflict.TokenValueAlreadyTakenException;

@Aspect
@Order(7)
@Component
public class TokenFacadeAspect {

    @Pointcut(value = "within(pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.TokenFacade)")
    private void tokenFacadeMethodAspect() {}

    @Around(value = "tokenFacadeMethodAspect()")
    private Object handleTokenFacadeMethodExceptions(ProceedingJoinPoint proceedingJoinPoint) throws Exception {
        try {
            return proceedingJoinPoint.proceed();
        } catch (OptimisticLockException optimisticLockException) {
            throw optimisticLockException;
        } catch (PersistenceException | PSQLException exception) {
            Throwable exceptionCopy = exception;
            do {
                if (exceptionCopy.getMessage().contains("token_tokenValue_key")) {
                    throw new TokenValueAlreadyTakenException();
                }
                exceptionCopy = exceptionCopy.getCause();
            } while (exceptionCopy != null);
            throw new ApplicationDatabaseException(exception);
        } catch (Throwable throwable) {
            throw new ApplicationInternalServerErrorException();
        }
    }
}

