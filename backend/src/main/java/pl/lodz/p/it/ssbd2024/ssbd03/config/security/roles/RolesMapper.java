package pl.lodz.p.it.ssbd2024.ssbd03.config.security.roles;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Roles;

import java.util.List;
import java.util.stream.Collectors;

@Component
@PropertySource("classpath:properties/roles.properties")
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

    public List<String> getAuthoritiesAsStrings(String role) {
        return List.of(switch (role.toUpperCase()) {
            case Roles.ADMIN -> roleAdmin;
            case Roles.STAFF -> roleStaff;
            case Roles.CLIENT -> roleClient;
            case Roles.AUTHENTICATED -> roleAuthenticated;
            case Roles.ANONYMOUS -> roleAnonymous;
            default -> throw new IllegalStateException("Unexpected value: " + role);
        });
    }

    public List<SimpleGrantedAuthority> getAuthorities(String role) {
        return getAuthoritiesAsStrings(role)
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
