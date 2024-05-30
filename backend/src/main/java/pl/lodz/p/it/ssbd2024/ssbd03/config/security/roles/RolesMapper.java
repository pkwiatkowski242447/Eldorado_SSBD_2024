package pl.lodz.p.it.ssbd2024.ssbd03.config.security.roles;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Roles;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils.UnsupportedRoleException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@PropertySource("classpath:properties/roles.properties")
@LoggerInterceptor
public class RolesMapper {

    @Value("${role.admin}")
    private String[] roleAdmin;

    @Value("${role.staff}")
    private String[] roleStaff;

    @Value("${role.client}")
    private String[] roleClient;

    @Value("${role.authenticated}")
    private String[] roleAuthenticated;

    @Value("${role.anonymous}")
    private String[] roleAnonymous;

    public List<String> getAuthoritiesAsStrings(String role) throws UnsupportedRoleException {
        return Stream.of(switch (role.toUpperCase()) {
                    case Roles.ADMIN -> roleAdmin;
                    case Roles.STAFF -> roleStaff;
                    case Roles.CLIENT -> roleClient;
                    case Roles.AUTHENTICATED -> roleAuthenticated;
                    case Roles.ANONYMOUS -> roleAnonymous;
                    default -> throw new UnsupportedRoleException();
                })
                .map(authority -> "ROLE_" + authority)
                .collect(Collectors.toList());
    }

    public List<SimpleGrantedAuthority> getAuthorities(String role) throws UnsupportedRoleException{
        return getAuthoritiesAsStrings(role)
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
