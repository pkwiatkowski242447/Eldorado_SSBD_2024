package pl.lodz.p.it.ssbd2024.ssbd03.aspects.exception.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationInternalServerErrorException;

@Aspect
@Order(15)
@Component
public class GenericServiceAspect {

    /**
     * Pointcut for any method definition inside classes annotated with annotation @Service from (org.springframework.stereotype)
     */
    @Pointcut(value = "@within(org.springframework.stereotype.Service)")
    private void serviceMethodPointcut() {}

    /**
     * This method is called when serviceMethodPointcut is triggered, so basically when any method from any @Service
     * annotated class is called. The main responsibility of this method is to transform exceptions thrown by any service into other,
     * checked exceptions, in order to standardize the exception handling in the application.
     *
     * @param proceedingJoinPoint Join point used to execute method that was being intercepted by this aspect.
     *
     * @throws Exception When exception is thrown in the intercepted method, then this method will transform in
     * into other, checked exception and propagate further, or rethrow them in order to process them in the next aspect.
     */
    @Around(value = "serviceMethodPointcut()")
    private Object handleServiceMethodExceptions(ProceedingJoinPoint proceedingJoinPoint) throws Exception {
        try {
            return proceedingJoinPoint.proceed();
        } catch (ApplicationBaseException applicationBaseException) {
            throw applicationBaseException;
        } catch (Throwable throwable) {
            throw new ApplicationInternalServerErrorException();
        }
    }
}
