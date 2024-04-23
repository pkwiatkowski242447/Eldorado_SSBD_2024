package pl.lodz.p.it.ssbd2024.ssbd03.config.webconfig;

import jakarta.servlet.Filter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.SecurityConfig;

public class SpringWebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { WebConfig.class, SecurityConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {};
    }

    @Override
    protected Filter[] getServletFilters() {
        /*
            If the JwtTokenAuthenticationFilter was directly used as a ServletFilter, then only this filter would be applied.
            In this case, chained filters managed by Spring Security (ExceptionTranslationFilter, SessionManagementFilter et FilterSecurityInterceptor, etc.)
            wouldn't be applied. As such, URL filtering wouldn't be secured as expected by the configuration).

            We need to specify the springSecurityFilterChain as the initial Servlet filter. This proxy takes care of chaining filter calls as they
            are indicated in the WebSecurityConfiguration class.
        */
        return new Filter[] { new DelegatingFilterProxy("springSecurityFilterChain") };
    }
}