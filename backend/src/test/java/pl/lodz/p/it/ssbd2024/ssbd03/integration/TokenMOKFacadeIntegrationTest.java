package pl.lodz.p.it.ssbd2024.ssbd03.integration;


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
import pl.lodz.p.it.ssbd2024.ssbd03.entities.Token;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.UserLevel;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountMOKFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AuthenticationFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.TokenFacade;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
@ExtendWith(SpringExtension.class)
public class TokenMOKFacadeIntegrationTest extends TestcontainersConfig {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    //INITIAL DATA
    @Autowired
    private TokenFacade tokenFacade;

    @Autowired
    private AccountMOKFacade accountMOKFacade;

    private UUID uuidNo1 = UUID.fromString("582c432a-c5d9-4758-863a-4999a7d95de5");
    private Token.TokenType tokenTypeRegister = Token.TokenType.REGISTER;
    private Token.TokenType tokenTypeResetPassword = Token.TokenType.RESET_PASSWORD;
    private Token.TokenType tokenTypeConfirmEmail = Token.TokenType.CONFIRM_EMAIL;
    private Token.TokenType tokenTypeChangeOverwrittenPassword = Token.TokenType.CHANGE_OVERWRITTEN_PASSWORD;
    private UUID userUuidNo1 = UUID.fromString("f5afc042-79b0-47fe-87ee-710c14af888c");
    private String tokenValueNo1 = "TEST_VALUE90";




    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findByTokenValue() {
        Optional<Token> token = tokenFacade.findByTokenValue(tokenValueNo1);

        Assertions.assertNotNull(token.get());

        Assertions.assertEquals(tokenTypeConfirmEmail, token.get().getType());
        Assertions.assertEquals(userUuidNo1 ,token.get().getAccount().getId());
        Assertions.assertEquals(tokenValueNo1 ,token.get().getTokenValue());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void createAndRemovePositiveTest() {
        String tokenValue = "testValueToken";
        Account account = accountMOKFacade.findByLoginAndRefresh("jerzybem").get();

        Assertions.assertNotNull(account);

        Token token = new Token(tokenValue, account, tokenTypeChangeOverwrittenPassword);

        tokenFacade.create(token);

        Token tokenFind = tokenFacade.findByTokenValue(tokenValue).get();

        tokenFacade.remove(tokenFind);

        Optional<Token> tokenRemoved = tokenFacade.findByTokenValue(tokenValue);

        Assertions.assertTrue(tokenRemoved.isEmpty());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findReturnsExistingTokenPositiveTest() {
        Optional<Token> token = tokenFacade.find(uuidNo1);

        Assertions.assertFalse(token.isEmpty());

        Assertions.assertEquals(tokenTypeConfirmEmail, token.get().getType());
        Assertions.assertEquals(userUuidNo1 ,token.get().getAccount().getId());
        Assertions.assertEquals(tokenValueNo1 ,token.get().getTokenValue());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findAllPositiveTest() {
        List<Token> tokenList = tokenFacade.findAll();

        Assertions.assertEquals(6, tokenList.size());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void countPositiveTest() {
        int count = tokenFacade.count();

        Assertions.assertEquals(6, count);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findByTypeAndAccountTest() {
        Optional<Token> token = tokenFacade.findByTypeAndAccount(tokenTypeConfirmEmail, UUID.fromString("d20f860d-555a-479e-8783-67aee5b66692"));

        Assertions.assertFalse(token.isEmpty());

        Assertions.assertEquals(tokenTypeConfirmEmail, token.get().getType());
        Assertions.assertEquals(UUID.fromString("d20f860d-555a-479e-8783-67aee5b66692") ,token.get().getAccount().getId());
        Assertions.assertEquals("TEST_VALUE93" ,token.get().getTokenValue());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findByTokenTypeTest() {
        List<Token> tokens = tokenFacade.findByTokenType(tokenTypeConfirmEmail);

        Assertions.assertEquals(4, tokens.size());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeByAccountTest() {
        Account account = new Account("wiktorptak", "$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa", "wiktor", "ptak",
                "wiktorptak@gmail.com", "123567123");
        UserLevel userLevelClientNo1 = new Client();
        userLevelClientNo1.setAccount(account);
        account.addUserLevel(userLevelClientNo1);

        account.setAccountLanguage("PL");
        accountMOKFacade.create(account);

        Account findAccount = accountMOKFacade.findByLoginAndRefresh("wiktorptak").get();

        Token token = new Token("WiktorPtakToken", findAccount, tokenTypeChangeOverwrittenPassword);

        tokenFacade.create(token);

        Token findToken = tokenFacade.findByTokenValue("WiktorPtakToken").get();

        Assertions.assertNotNull(findToken);

        tokenFacade.removeByAccount(findAccount.getId());

        Optional<Token> doeleteToken = tokenFacade.findByTokenValue("WiktorPtakToken");

        Assertions.assertTrue(doeleteToken.isEmpty());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeByTypeAndAccount() {
        Account account = new Account("wiktorptak", "$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa", "wiktor", "ptak",
                "wiktorptak@gmail.com", "123567123");
        UserLevel userLevelClientNo1 = new Client();
        userLevelClientNo1.setAccount(account);
        account.addUserLevel(userLevelClientNo1);

        account.setAccountLanguage("PL");
        accountMOKFacade.create(account);

        Account findAccount = accountMOKFacade.findByLoginAndRefresh("wiktorptak").get();

        Token token1 = new Token("WiktorPtakToken1", findAccount, tokenTypeChangeOverwrittenPassword);
        Token token2 = new Token("WiktorPtakToken2", findAccount, tokenTypeChangeOverwrittenPassword);

        tokenFacade.create(token1);
        tokenFacade.create(token2);

        int count = tokenFacade.count();

        Assertions.assertEquals(8, count);

        tokenFacade.removeByTypeAndAccount(tokenTypeChangeOverwrittenPassword, findAccount.getId());

        int countDelete = tokenFacade.count();

        Assertions.assertEquals(6, countDelete);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void editTestPositive() {
        Account account = new Account("wiktorptak", "$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa", "wiktor", "ptak",
                "wiktorptak@gmail.com", "123567123");
        UserLevel userLevelClientNo1 = new Client();
        userLevelClientNo1.setAccount(account);
        account.addUserLevel(userLevelClientNo1);

        account.setAccountLanguage("PL");
        accountMOKFacade.create(account);

        Account findAccount = accountMOKFacade.findByLoginAndRefresh("wiktorptak").get();

        Token token1 = new Token("WiktorPtakToken1", findAccount, tokenTypeChangeOverwrittenPassword);

        tokenFacade.create(token1);

        Token findToken = tokenFacade.findByTokenValue("WiktorPtakToken1").get();

        findToken.setTokenValue("EDIT");

        tokenFacade.edit(findToken);

        Optional<Token> findEditedToken = tokenFacade.findByTokenValue("EDIT");

        Assertions.assertFalse(findEditedToken.isEmpty());
        Assertions.assertEquals(tokenTypeChangeOverwrittenPassword, findEditedToken.get().getType());
        Assertions.assertEquals(account.getId(), findEditedToken.get().getAccount().getId());
        Assertions.assertEquals("EDIT", findEditedToken.get().getTokenValue());

        tokenFacade.remove(findEditedToken.get());
    }
}
