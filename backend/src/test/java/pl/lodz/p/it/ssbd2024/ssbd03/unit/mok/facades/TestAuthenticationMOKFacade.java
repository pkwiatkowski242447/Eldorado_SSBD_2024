package pl.lodz.p.it.ssbd2024.ssbd03.unit.mok.facades;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import pl.lodz.p.it.ssbd2024.ssbd03.TestcontainersConfig;
import pl.lodz.p.it.ssbd2024.ssbd03.config.webconfig.WebConfig;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AuthenticationFacade;

@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
@ExtendWith(SpringExtension.class)
public class TestAuthenticationMOKFacade extends TestcontainersConfig {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    void AuthenticationMOKFacadeGetEntityManagerReturnsNotNullTestPositive() {
        Assertions.assertNotNull(authenticationFacade.getEntityManager());
    }
}
