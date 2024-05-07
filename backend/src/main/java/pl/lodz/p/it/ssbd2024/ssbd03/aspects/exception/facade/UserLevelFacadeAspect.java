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

    @Pointcut(value = "within(pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.UserLevelFacade)")
    private void userLevelFacadeMethodAspect() {}

    @Around(value = "userLevelFacadeMethodAspect()")
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
