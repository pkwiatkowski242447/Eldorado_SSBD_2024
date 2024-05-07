package pl.lodz.p.it.ssbd2024.ssbd03.aspects.exception.facade;

import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationDatabaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationInternalServerErrorException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationOptimisticLockException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

import java.sql.SQLException;

@Aspect
@Order(10)
@Component
public class GenericFacadeAspect {

    /**
     * Pointcut definition for every method that is inside a class with @Repository annotation (from org.springframework.stereotype)
     * effectively executing corresponding advice method for every method called from any facade component.
     */
    @Pointcut(value = "@annotation(org.springframework.stereotype.Repository)")
    private void facadeMethodPointcut() {}

    /**
     * This advice method is called when facadeMethodPointcut() is triggered (when method from any facade component in this
     * application is called). The main responsibility of this method is to pack exceptions which could be thrown the by facade layer
     * into application exceptions to standardize exception handling in the application.
     *
     * @param proceedingJoinPoint Join point used to execute method that was being intercepted by this aspect.
     *
     * @return Result of the executed method is returned, as this method is executed around certain method
     * that performs operations in a transaction.
     *
     * @throws Exception As this method only performs "repacking" of exceptions thrown by facade layer, then
     * main result of this method is returned exception.
     */
    @Around(value = "facadeMethodPointcut()")
    private Object handleFacadeMethodExceptions(ProceedingJoinPoint proceedingJoinPoint) throws Exception {
        try {
            return proceedingJoinPoint.proceed();
        } catch (OptimisticLockException optimisticLockException) {
            throw new ApplicationOptimisticLockException();
        } catch (ApplicationBaseException applicationBaseException) {
            throw applicationBaseException;
        } catch (PersistenceException | SQLException exception) {
            throw new ApplicationDatabaseException(exception);
        } catch (Throwable throwable) {
            throw new ApplicationInternalServerErrorException();
        }
    }
}
