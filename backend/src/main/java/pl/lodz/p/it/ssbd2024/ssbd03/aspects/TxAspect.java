package pl.lodz.p.it.ssbd2024.ssbd03.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@Aspect
public class TxAspect {

    @Pointcut(value = "@annotation(org.springframework.transaction.annotation.Transactional) || @within(org.springframework.transaction.annotation.Transactional)")
    private void txPointcut() {}

    @Around(value = "txPointcut()")
    private Object aroundTxPointcut(ProceedingJoinPoint proceedingJoinPoint) {
        StringBuilder message = new StringBuilder("Method call: ");
        Object result = new Object();
        try {
            try {
                message.append(proceedingJoinPoint.getSignature().getName());
                message.append(" | Class: ").append(proceedingJoinPoint.getTarget().getClass().getSimpleName());
                message.append(" | TransactionKey: ").append("NULL");
                message.append(" | User: ").append(null != SecurityContextHolder.getContext().getAuthentication() ? SecurityContextHolder.getContext().getAuthentication().getName() : "--ANONYMOUS--");
            } catch (Exception e) {
                log.error(" | Unexpected exception within aspect: ", e);
                throw e;
            }
            result = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            message.append(" | Thrown exception: ").append(e);
            log.error(message.toString(), e);
        }

        message.append(" | Returned: ").append(result).append(" ");
        log.info(message.toString());

        return result;
    }
}
