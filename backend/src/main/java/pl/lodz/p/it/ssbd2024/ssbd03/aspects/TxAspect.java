package pl.lodz.p.it.ssbd2024.ssbd03.aspects;

import jakarta.transaction.Synchronization;
import jakarta.transaction.Transaction;
import jakarta.transaction.TransactionSynchronizationRegistry;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Aspect
@Component
public class TxAspect {

    /**
     * This set contains all transaction identifiers that currently exist in the application. Method executed around
     * txPointcut() add an identifier (if it is not already in the set) and that identifier is removed by afterCompletionMethod()
     * of TransactionSynchronization.
     */
    @Getter
    private static final Set<String> transactionIds = new HashSet<>();

    /**
     * Pointcut definition for every method or class with @Transactional annotation (from org.springframework.transaction.annotation)
     * effectively executing corresponding advice method for every transactional call of any method.
     */
    @Pointcut(value = "@annotation(org.springframework.transaction.annotation.Transactional) || @within(org.springframework.transaction.annotation.Transactional)")
    private void txPointcut() {}

    /**
     * Method called when the specified txPointcut is triggered (when execution of @Transactional annotated method begins).
     * It is used to log transaction related information like:
     * <ul>
     *    <li>Name of the executed method.</li>
     *    <li>Class, where the executed method is located.</li>
     *    <li>Identifier of the transaction.</li>
     *    <li>Username of the user, who is currently logged into the application.</li>
     *    <li>Value returned by the executed method.</li>
     * </ul>
     *
     * Optionally, this method is able to log error related information like:
     * <ul>
     *    <li>Exception throw during aspect execution.</li>
     * <ul/>
     *
     * @param proceedingJoinPoint Join point used to execute method that was being intercepted by this aspect.
     *
     * @return Result of the executed method is returned, as this method is executed around certain method
     * that performs operations in a transaction.
     */

    @Around(value = "txPointcut()")
    private Object aroundTxPointcut(ProceedingJoinPoint proceedingJoinPoint) {
        String transactionKey;
        StringBuilder message = new StringBuilder("Method call: ");
        Object result = new Object();
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            Class<?> transactionSynchronizationRegistry = contextClassLoader.loadClass("com.atomikos.icatch.jta.TransactionSynchronizationRegistryImp");

            Method getTransactionKey = transactionSynchronizationRegistry.getMethod("getTransactionKey", (Class<?>[]) null);
            Transaction transaction = (Transaction) getTransactionKey.invoke(transactionSynchronizationRegistry.getDeclaredConstructor().newInstance());
            transactionKey = transaction.toString();

            if (!transactionIds.contains(transactionKey)) {
                Method registerSynchronization = transactionSynchronizationRegistry.getMethod("registerInterposedSynchronization", Synchronization.class);
                registerSynchronization.invoke(transactionSynchronizationRegistry.getDeclaredConstructor().newInstance(), new TransactionSynchronization(transactionKey));
                transactionIds.add(transactionKey);
            }

            try {
                message.append(proceedingJoinPoint.getSignature().getName());
                message.append(" | Class: ").append(proceedingJoinPoint.getTarget().getClass().getSimpleName());
                message.append(" | Transaction key: ").append(transactionKey != null ? transactionKey : "NULL");
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
