package pl.lodz.p.it.ssbd2024.ssbd03.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@Slf4j
@Aspect
@Component
public class TxAspect extends TransactionInterceptor {


    @Pointcut(value = "@annotation(org.springframework.transaction.annotation.Transactional) || @within(org.springframework.transaction.annotation.Transactional)")
    private void txPointcut() {}

    @Around(value = "txPointcut()")
    private Object aroundTxPointcut(ProceedingJoinPoint proceedingJoinPoint) {
        StringBuilder message = new StringBuilder("Method call: ");
        Object result = new Object();
        try {
            try {
                TransactionInfo transactionInfo = TransactionInterceptor.currentTransactionInfo();
                message.append(proceedingJoinPoint.getSignature().getName());
                message.append(" | Class: ").append(proceedingJoinPoint.getTarget().getClass().getSimpleName());
                message.append(" | TransactionStatus before: ").append(transactionInfo != null ? transactionInfo.getTransactionStatus() : "NULL");
                message.append(" | User: ").append(null != SecurityContextHolder.getContext().getAuthentication() ? SecurityContextHolder.getContext().getAuthentication().getName() : "--ANONYMOUS--");
            } catch (Exception e) {
                log.error(" | Unexpected exception within aspect: ", e);
                throw e;
            }
            result = proceedingJoinPoint.proceed();

            TransactionInfo transactionInfo = TransactionInterceptor.currentTransactionInfo();
            message.append(" | TransactionStatus after: ").append(transactionInfo != null ? transactionInfo.getTransactionStatus() : "NULL");
        } catch (Throwable e) {
            message.append(" | Thrown exception: ").append(e);
            log.error(message.toString(), e);
        }

        message.append(" | Returned: ").append(result).append(" ");
        log.info(message.toString());

        return result;
    }
}
