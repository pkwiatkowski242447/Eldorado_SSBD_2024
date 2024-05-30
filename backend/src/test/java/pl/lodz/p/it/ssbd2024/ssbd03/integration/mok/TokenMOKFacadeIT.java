package pl.lodz.p.it.ssbd2024.ssbd03.integration.mok;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import pl.lodz.p.it.ssbd2024.ssbd03.TestcontainersConfig;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.config.webconfig.WebConfig;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Token;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountMOKFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.TokenFacade;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
@ExtendWith(SpringExtension.class)
public class TokenMOKFacadeIT extends TestcontainersConfig {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("jdbc.ssbd03.url", () -> String.format("jdbc:postgresql://localhost:%s/ssbd03", postgres.getFirstMappedPort()));
    }

    @AfterEach
    void teardown() {
        ((AtomikosDataSourceBean) webApplicationContext.getBean("dataSourceAdmin")).close();
        ((AtomikosDataSourceBean) webApplicationContext.getBean("dataSourceAuth")).close();
        ((AtomikosDataSourceBean) webApplicationContext.getBean("dataSourceMOP")).close();
        ((AtomikosDataSourceBean) webApplicationContext.getBean("dataSourceMOK")).close();
    }

    //INITIAL DATA
    @Autowired
    private TokenFacade tokenFacade;

    @Autowired
    private AccountMOKFacade accountMOKFacade;

    private final UUID uuidNo1 = UUID.fromString("582c432a-c5d9-4758-863a-4999a7d95de5");
    private final Token.TokenType tokenTypeRegister = Token.TokenType.REGISTER;
    private final Token.TokenType tokenTypeResetPassword = Token.TokenType.RESET_PASSWORD;
    private final Token.TokenType tokenTypeConfirmEmail = Token.TokenType.CONFIRM_EMAIL;
    private final Token.TokenType tokenTypeChangeOverwrittenPassword = Token.TokenType.CHANGE_OVERWRITTEN_PASSWORD;
    private final UUID userUuidNo1 = UUID.fromString("f5afc042-79b0-47fe-87ee-710c14af888c");
    private final String tokenValueNo1 = "TEST_VALUE90";

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(username = "ExampleUser", roles = {Authorities.CHANGE_USER_PASSWORD})
    public void findByTokenValue() throws Exception {
        Optional<Token> token = tokenFacade.findByTokenValue(tokenValueNo1);

        assertNotNull(token.orElseThrow(NoSuchElementException::new));

        assertEquals(tokenTypeConfirmEmail, token.get().getType());
        assertEquals(userUuidNo1, token.get().getAccount().getId());
        assertEquals(tokenValueNo1, token.get().getTokenValue());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(roles = {Authorities.CHANGE_USER_PASSWORD})
    public void createAndRemovePositiveTest() throws ApplicationBaseException {
        String tokenValue = "testValueToken";
        Account account = accountMOKFacade.findByLogin("jerzybem").orElseThrow(NoSuchElementException::new);

        assertNotNull(account);

        Token token = new Token(tokenValue, account, tokenTypeChangeOverwrittenPassword);

        tokenFacade.create(token);

        Token tokenFind = tokenFacade.findByTokenValue(tokenValue).orElseThrow(NoSuchElementException::new);

        tokenFacade.remove(tokenFind);

        Optional<Token> tokenRemoved = tokenFacade.findByTokenValue(tokenValue);

        assertTrue(tokenRemoved.isEmpty());
    }

//    @Test
//    @Transactional(propagation = Propagation.REQUIRED)
//    public void findReturnsExistingTokenPositiveTest() throws ApplicationBaseException {
//        Optional<Token> token = tokenFacade.find(uuidNo1);
//
//        assertFalse(token.isEmpty());
//
//        assertEquals(tokenTypeConfirmEmail, token.get().getType());
//        assertEquals(userUuidNo1, token.get().getAccount().getId());
//        assertEquals(tokenValueNo1, token.get().getTokenValue());
//    }

//    @Test
//    @Transactional(propagation = Propagation.REQUIRED)
//    @WithMockUser(roles = {Roles.ADMIN})
//    public void findAllPositiveTest() throws Exception {
//        List<Token> tokenList = tokenFacade.findAll();
//
//        assertEquals(6, tokenList.size());
//    }

//    @Test
//    @Transactional(propagation = Propagation.REQUIRED)
//    @WithMockUser(roles = {Roles.ADMIN})
//    public void countPositiveTest() throws Exception {
//        int count = tokenFacade.count();
//
//        assertEquals(6, count);
//    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(username = "ExampleUse", roles = {Authorities.RESEND_EMAIL_CONFIRMATION_MAIL})
    public void findByTypeAndAccountTest() throws Exception {
        Optional<Token> token = tokenFacade.findByTypeAndAccount(tokenTypeConfirmEmail, UUID.fromString("d20f860d-555a-479e-8783-67aee5b66692"));

        assertFalse(token.isEmpty());

        assertEquals(tokenTypeConfirmEmail, token.get().getType());
        assertEquals(UUID.fromString("d20f860d-555a-479e-8783-67aee5b66692"), token.get().getAccount().getId());
        assertEquals("TEST_VALUE93", token.get().getTokenValue());
    }

//    @Test
//    @Transactional(propagation = Propagation.REQUIRED)
//    public void findByTokenTypeTest() throws Exception {
//        List<Token> tokens = tokenFacade.findByTokenType(tokenTypeConfirmEmail);
//
//        assertEquals(4, tokens.size());
//    }

//    @Test
//    @Transactional(propagation = Propagation.REQUIRED)
//    @WithMockUser(roles = {Roles.AUTHENTICATED})
//    public void removeByAccountTest() throws ApplicationBaseException {
//        Account account = new Account("wiktorptak", "$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa", "wiktor", "ptak",
//                "wiktorptak@gmail.com", "123567123");
//        UserLevel userLevelClientNo1 = new Client();
//        userLevelClientNo1.setAccount(account);
//        account.addUserLevel(userLevelClientNo1);
//
//        account.setAccountLanguage("PL");
//        accountMOKFacade.create(account);
//
//        Account findAccount = accountMOKFacade.findByLogin("wiktorptak").orElseThrow(NoSuchElementException::new);
//
//        Token token = new Token("WiktorPtakToken", findAccount, tokenTypeChangeOverwrittenPassword);
//
//        tokenFacade.create(token);
//
//        Token findToken = tokenFacade.findByTokenValue("WiktorPtakToken").orElseThrow(NoSuchElementException::new);
//
//        assertNotNull(findToken);
//
//        tokenFacade.removeByAccount(findAccount.getId());
//
//        Optional<Token> deleteToken = tokenFacade.findByTokenValue("WiktorPtakToken");
//
//        assertTrue(deleteToken.isEmpty());
//    }

//    @Test
//    @Transactional(propagation = Propagation.REQUIRED)
//    @WithMockUser(roles = {Authorities.RESET_PASSWORD, Authorities.REGISTER_CLIENT})
//    public void removeByTypeAndAccount() throws ApplicationBaseException {
//        Account account = new Account("wiktorptak", "$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa", "wiktor", "ptak",
//                "wiktorptak@gmail.com", "123567123");
//        UserLevel userLevelClientNo1 = new Client();
//        userLevelClientNo1.setAccount(account);
//        account.addUserLevel(userLevelClientNo1);
//
//        account.setAccountLanguage("PL");
//        accountMOKFacade.create(account);
//
//        Account findAccount = accountMOKFacade.findByLogin("wiktorptak").orElseThrow(NoSuchElementException::new);
//
//        Token token1 = new Token("WiktorPtakToken1", findAccount, tokenTypeChangeOverwrittenPassword);
//        Token token2 = new Token("WiktorPtakToken2", findAccount, tokenTypeChangeOverwrittenPassword);
//
//        tokenFacade.create(token1);
//        tokenFacade.create(token2);
//
//        int count = tokenFacade.count();
//
//        assertEquals(8, count);
//
//        tokenFacade.removeByTypeAndAccount(tokenTypeChangeOverwrittenPassword, findAccount.getId());
//
//        int countDelete = tokenFacade.count();
//
//        assertEquals(6, countDelete);
//    }

//    @Test
//    @Transactional(propagation = Propagation.REQUIRED)
//    @WithMockUser(roles = {Roles.AUTHENTICATED, Roles.ANONYMOUS})
//    public void editTestPositive() throws ApplicationBaseException {
//        Account account = new Account("wiktorptak", "$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa", "wiktor", "ptak",
//                "wiktorptak@gmail.com", "123567123");
//        UserLevel userLevelClientNo1 = new Client();
//        userLevelClientNo1.setAccount(account);
//        account.addUserLevel(userLevelClientNo1);
//
//        account.setAccountLanguage("PL");
//        accountMOKFacade.create(account);
//
//        Account findAccount = accountMOKFacade.findByLogin("wiktorptak").orElseThrow(NoSuchElementException::new);
//
//        Token token1 = new Token("WiktorPtakToken1", findAccount, tokenTypeChangeOverwrittenPassword);
//
//        tokenFacade.create(token1);
//
//        Token findToken = tokenFacade.findByTokenValue("WiktorPtakToken1").orElseThrow(NoSuchElementException::new);
//
//        findToken.setTokenValue("EDIT");
//
//        tokenFacade.edit(findToken);
//
//        Optional<Token> findEditedToken = tokenFacade.findByTokenValue("EDIT");
//
//        assertFalse(findEditedToken.isEmpty());
//        assertEquals(tokenTypeChangeOverwrittenPassword, findEditedToken.get().getType());
//        assertEquals(account.getId(), findEditedToken.get().getAccount().getId());
//        assertEquals("EDIT", findEditedToken.get().getTokenValue());
//
//        tokenFacade.remove(findEditedToken.get());
//    }
}
