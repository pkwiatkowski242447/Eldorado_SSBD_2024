package pl.lodz.p.it.ssbd2024.ssbd03.aspects.util;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Roles;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.roles.RolesMapper;

import java.util.List;
import java.util.UUID;

/**
 * Aspect used to create authentication object for SYSTEM actor
 * and place it in SecurityContext in order to make calling other
 * methods possible.
 */
@Slf4j
@Aspect
@Order(50)
@Component
public class RunAsImpl {

    /**
     * Role mapper component used for mapping application roles
     * into roles that represent use cases.
     */
    private final RolesMapper rolesMapper;

    /**
     * Autowired constructor for this aspect.
     * @param rolesMapper Role mapping component.
     */
    @Autowired
    public RunAsImpl(RolesMapper rolesMapper) {
        this.rolesMapper = rolesMapper;
    }

    /**
     * Pointcut for all methods to be executed with SYSTEM user level.
     * @see pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor
     */
    @Pointcut(value = "@annotation(pl.lodz.p.it.ssbd2024.ssbd03.aspects.util.RunAsSystem) || " +
            "@within(pl.lodz.p.it.ssbd2024.ssbd03.aspects.util.RunAsSystem)")
    private void runAsSystemPointcut() {}

    /**
     * Advice method used to
     * @param point Object used to traverse the call stack down to the called method.
     * @return Value returned by the called method, intercepted by this aspect.
     */
    @Around(value = "runAsSystemPointcut()")
    private Object runAsSystemAdvice(ProceedingJoinPoint point) {
        Object result = null;
        try {
            List<SimpleGrantedAuthority> listOfRoles = rolesMapper.getAuthorities(Roles.SYSTEM);
            Authentication authentication = new AnonymousAuthenticationToken(UUID.randomUUID().toString(), "SYSTEM", listOfRoles);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            result = point.proceed();

            SecurityContextHolder.getContext().setAuthentication(null);
        } catch (Throwable throwable) {
            log.error("Exception: {} was thrown during aspect method execution. Message: {}. Cause: ",
                    throwable.getClass().getSimpleName(), throwable.getMessage(), throwable.getCause());
        }
        return result;
    }
}
