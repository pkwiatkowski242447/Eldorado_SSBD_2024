package pl.lodz.p.it.ssbd2024.ssbd03.mok.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.Token;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.TokenFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.JWTProvider;

@Service
public class TokenService {

    private final TokenFacade tokenFacade;
    private final JWTProvider jwtProvider;

    @Autowired
    public TokenService(TokenFacade tokenFacade,
                        JWTProvider jwtProvider) {
        this.tokenFacade = tokenFacade;
        this.jwtProvider = jwtProvider;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public String createRegistrationToken(Account account) {
        String tokenValue = this.jwtProvider.generateRegistrationToken(account);

        Token registrationToken = new Token(tokenValue, account, Token.TokenType.REGISTER);
        this.tokenFacade.create(registrationToken);

        return tokenValue;
    }

    public String createEmailConfirmationToken(Account account, String newEmail) {

        Token emailConfirmationToken = new Token(newEmail, account, Token.TokenType.CONFIRM_EMAIL);
        this.tokenFacade.create(emailConfirmationToken);

        return newEmail;
    }
}
