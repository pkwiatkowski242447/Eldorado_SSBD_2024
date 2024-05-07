package pl.lodz.p.it.ssbd2024.ssbd03.aspects.exception.facade;

import jakarta.persistence.OptimisticLockException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationInternalServerErrorException;

@Aspect
@Order(8)
@Component
public class UserLevelFacadeAspect {

    /**
     * Pointcut definition for any method within UserLevelFacade.
     */
    @Pointcut(value = "within(pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.UserLevelFacade)")
    private void userLevelFacadeMethodPointcut() {}

    /**
     * This method is called when userLevelFacadeMethodPointcut is triggered, so basically when any method from UserLevelFacade
     * is called. The main responsibility of this method is to transform exceptions thrown by the facade into other,
     * checked exceptions, in order to standardize the exception handling in the application.
     *
     * @param proceedingJoinPoint Join point used to execute method that was being intercepted by this aspect.
     *
     * @throws Exception When exception is thrown in the intercepted method, then this method will transform in
     * into other, checked exception and propagate further, or rethrow them in order to process them in the next aspect.
     */
    @Around(value = "userLevelFacadeMethodPointcut()")
    private Object handleUserLevelFacadeMethodExceptions(ProceedingJoinPoint proceedingJoinPoint) throws Exception {
        try {
            return proceedingJoinPoint.proceed();
        } catch (OptimisticLockException optimisticLockException) {
            throw optimisticLockException;
        } catch (Throwable throwable) {
            throw new ApplicationInternalServerErrorException();
        }
    }
}
