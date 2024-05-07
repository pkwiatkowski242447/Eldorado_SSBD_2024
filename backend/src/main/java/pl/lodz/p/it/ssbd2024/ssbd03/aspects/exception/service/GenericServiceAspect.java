package pl.lodz.p.it.ssbd2024.ssbd03.aspects.exception.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Order(15)
@Component
public class GenericServiceAspect {

    @Pointcut(value = "@annotation(org.springframework.stereotype.Service)")
    private void serviceMethodPointcut() {}

    @Around(value = "serviceMethodPointcut()")
    private Object handleServiceMethodExceptions(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return proceedingJoinPoint.proceed();
    }
}
