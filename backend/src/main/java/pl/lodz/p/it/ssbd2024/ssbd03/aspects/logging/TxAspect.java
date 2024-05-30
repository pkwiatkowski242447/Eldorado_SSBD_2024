package pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging;

import jakarta.transaction.Synchronization;
import jakarta.transaction.Transaction;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
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
     * Pointcut definition for every method or class with @TxTracked annotation (from pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging)
     * effectively executing corresponding advice method for every call of any method annotated with this annotation, thus
     * logging also transaction state.
     */
    @Pointcut(value = "@annotation(pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked) || " +
            "@within(pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked)")
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
    private Object aroundTxPointcut(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String transactionKey = null;
        StringBuilder message = new StringBuilder("Method call: ");
        message.append(proceedingJoinPoint.getSignature().getName());
        message.append(" | Class: ").append(proceedingJoinPoint.getTarget().getClass().getSimpleName());
        Object result;
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            Class<?> transactionSynchronizationRegistry = contextClassLoader.loadClass("com.atomikos.icatch.jta.TransactionSynchronizationRegistryImp");

            Method getTransactionKey = transactionSynchronizationRegistry.getMethod("getTransactionKey", (Class<?>[]) null);
            Transaction transaction = (Transaction) getTransactionKey.invoke(transactionSynchronizationRegistry.getDeclaredConstructor().newInstance());

            if (transaction != null) transactionKey = transaction.toString();

            if (transactionKey != null && !transactionIds.contains(transactionKey)) {
                Method registerSynchronization = transactionSynchronizationRegistry.getMethod("registerInterposedSynchronization", Synchronization.class);
                registerSynchronization.invoke(transactionSynchronizationRegistry.getDeclaredConstructor().newInstance(), new TransactionSynchronization(transactionKey));
                transactionIds.add(transactionKey);
            }

            try {
                if (transactionKey == null) message.append(" | This method is not called in the transactional context.");
                else message.append(" | Transaction key: ").append(transactionKey);
                message.append(" | User: ").append(null != SecurityContextHolder.getContext().getAuthentication() ? SecurityContextHolder.getContext().getAuthentication().getName() : "ANONYMOUS");
            } catch (Exception exception) {
                log.error(" | Unexpected exception: {} within aspect occurred due to: ", exception.getClass().getSimpleName(), exception.getCause());
                throw exception;
            }
            result = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            message.append(" | Thrown exception: ").append(throwable);
            log.error(message.toString(), throwable);
            throw throwable;
        }

        message.append(" | Returned ");
        if (result != null) message.append(": ").append(result).append(".");
        else message.append("no value.");

        log.info(message.toString());

        return result;
    }
}
