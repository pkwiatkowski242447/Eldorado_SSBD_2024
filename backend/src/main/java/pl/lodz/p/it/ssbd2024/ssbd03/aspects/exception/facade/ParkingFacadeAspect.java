package pl.lodz.p.it.ssbd2024.ssbd03.aspects.exception.facade;

import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationDatabaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.parking.conflict.ParkingDeleteException;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

import java.sql.SQLException;

@Slf4j
@Aspect
@Order(10)
@Component
public class ParkingFacadeAspect {

    /**
     * Pointcut definition for any method inside ParkingFacade.
     */
    @Pointcut(value = "within(pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.ParkingFacade)")
    private void parkingFacadeMethodPointcut() {}

    /**
     * This method is executed when parkingFacadeMethodPointcut was triggered, that is when any method from
     * the parking facade is executed.
     * The main responsibility is to transform exceptions related to parking into other exceptions
     * in order to standardize the exception handling in the application.
     *
     * @param proceedingJoinPoint Join point used to execute method that was being intercepted by this aspect.
     *
     * @return Result of the executed method is returned, as this method is executed around certain method
     * that performs operations in a transaction.
     *
     * @throws Exception As this method only performs "repacking" of exceptions thrown by facade layer, then
     * main result of this method is returned exception.
     */
    @Around(value = "parkingFacadeMethodPointcut()")
    private Object handleParkingRelatedMethodsExceptions(ProceedingJoinPoint proceedingJoinPoint) throws Exception {
        try {
            return proceedingJoinPoint.proceed();
        } catch (OptimisticLockException | AccessDeniedException exception) {
            throw exception;
        } catch (PersistenceException | SQLException | ConstraintViolationException exception) {
            Throwable exceptionCopy = exception;
            do {
                if (exceptionCopy.getMessage().contains("sector_parking_id_fk")) {
                    throw new ParkingDeleteException();
                }
                exceptionCopy = exceptionCopy.getCause();
            } while (exceptionCopy != null);
            throw new ApplicationDatabaseException(I18n.UNEXPECTED_DATABASE_EXCEPTION, exception);
        } catch (Throwable throwable) {
            throw new ApplicationBaseException();
        }
    }
}
