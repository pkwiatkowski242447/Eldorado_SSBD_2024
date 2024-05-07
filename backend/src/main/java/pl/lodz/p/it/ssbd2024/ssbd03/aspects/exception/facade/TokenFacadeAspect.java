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

    /**
     * Pointcut definition for any method within TokenFacade.
     */
    @Pointcut(value = "within(pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.TokenFacade)")
    private void tokenFacadeMethodPointcut() {}

    /**
     * This method is called when tokenFacadeMethodPointcut is triggered, so basically when any method from TokenFacade
     * is called. The main responsibility of this method is to transform exceptions thrown by the facade into other,
     * checked exceptions, in order to standardize the exception handling in the application.
     *
     * @param proceedingJoinPoint Join point used to execute method that was being intercepted by this aspect.
     *
     * @throws Exception When exception is thrown in the intercepted method, then this method will transform in
     * into other, checked exception and propagate further, or rethrow them in order to process them in the next aspect.
     */
    @Around(value = "tokenFacadeMethodPointcut()")
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

