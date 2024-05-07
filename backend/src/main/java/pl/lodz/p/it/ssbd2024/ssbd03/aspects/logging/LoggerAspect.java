package pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Aspect
@Order(1)
@Component
public class LoggerAspect {

    /**
     * Pointcut definition for every method that is inside mok package (pl.lodz.p.it.ssbd2024.ssbd03.mok)
     * effectively executing corresponding advice method for every method called from mok package (from
     * any facade, controller or service component).
     */
    @Pointcut(value = "within(pl.lodz.p.it.ssbd2024.ssbd03.mok..*)")
    private void mokPointcut() {}

    /**
     * Pointcut definition for every method that is inside mop package (pl.lodz.p.it.ssbd2024.ssbd03.mop)
     * effectively executing corresponding advice method for every method called from mop package (from
     * any facade, controller or service component).
     */
    @Pointcut(value = "within(pl.lodz.p.it.ssbd2024.ssbd03.mop..*)")
    private void mopPointcut() {}

    /**
     * Pointcut definition for every method that is inside provider package (pl.lodz.p.it.ssbd2024.ssbd03.utils.provider)
     * effectively executing corresponding advice method for every method called from utils package (from
     * any provider).
     */
    @Pointcut(value = "within(pl.lodz.p.it.ssbd2024.ssbd03.utils.providers..*)")
    private void utilsPointcut() {}

    /**
     * The main responsibility of this method is to log method call information like:
     * <ul>
     *     <li>Name of the called method</li>
     *     <li>Name of the class, which the method is called from</li>
     *     <li>Identity of the user calling given method</li>
     *     <li>User levels of the authenticated user, or GUEST otherwise.</li>
     *     <li>List of parameters of the given method</li>
     * </ul>
     *
     * Optionally, this method is used to log value returned by the intercepted method (if not exceptions are thrown)
     * or specify which exception was thrown and why.
     *
     * @param point Join point used to execute method that was being intercepted by this aspect.
     *
     * @return Result of the executed method is returned, as this method is executed around certain method
     * that performs operations in a transaction.
     *
     * @throws Throwable If any exception is thrown during this methods execution, then it should be propagated to the
     * next aspect (depending on the type of the component) that will handle this exception.
     */
    @Around(value = "mokPointcut() || mokPointcut() || utilsPointcut()")
    private Object methodLoggerAdvice(ProceedingJoinPoint point) throws Throwable {
        StringBuilder stringBuilder = new StringBuilder("Method: ");
        Object result = null;
        try {
            try {
                String callerIdentity = "Anonymous";
                List<String> callerRoleList = List.of("GUEST");

                if (SecurityContextHolder.getContext().getAuthentication() != null) {
                    Authentication authenticationObj = SecurityContextHolder.getContext().getAuthentication();
                    callerIdentity = authenticationObj.getName();
                    callerRoleList = authenticationObj.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
                }

                stringBuilder.append(point.getSignature().getName())
                        .append(" | Class: ")
                        .append(point.getTarget().getClass().getSimpleName())
                        .append('\n')
                        .append("Invoked by user authenticated as:  ")
                        .append(callerIdentity)
                        .append(" | List of users levels: ")
                        .append(callerRoleList)
                        .append('\n');

                stringBuilder.append("List of parameters: ")
                        .append("[ ");
                for (Object parameter : point.getArgs()) {
                    stringBuilder.append(parameter).append(": ").append(parameter.getClass().getSimpleName());
                    if (Arrays.stream(point.getArgs()).toList().getLast() != parameter) stringBuilder.append(", ");
                }
                stringBuilder.append(" ]")
                        .append('\n');
            } catch (Exception exception) {
                log.error("Exception occurred while processing logger aspect message: ", exception);
                throw exception;
            }

            result = point.proceed();
        } catch (Throwable throwable) {
            stringBuilder.append("Exception: ")
                    .append(throwable.getClass().getSimpleName())
                    .append(" was thrown during method execution, since: ")
                    .append(throwable.getMessage());
            log.error(stringBuilder.toString());
            throw throwable;
        }

        if (result != null) {
            stringBuilder.append(" Method returned value: ")
                    .append(result)
                    .append(" of type: ")
                    .append(result.getClass().getSimpleName());
        } else {
            stringBuilder.append(" Method did not return any value.");
        }
        log.info(stringBuilder.toString());

        return result;
    }
}
