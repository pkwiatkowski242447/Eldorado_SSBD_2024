package pl.lodz.p.it.ssbd2024.ssbd03.mok.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.Token;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.TokenFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.JWTProvider;

/**
 * Service managing Tokens.
 *
 * @see Token
 */
@Service
public class TokenService {

    private final TokenFacade tokenFacade;
    private final JWTProvider jwtProvider;

    /**
     * Autowired constructor for the service.
     *
     * @param tokenFacade
     * @param jwtProvider
     */
    @Autowired
    public TokenService(TokenFacade tokenFacade,
                        JWTProvider jwtProvider) {
        this.tokenFacade = tokenFacade;
        this.jwtProvider = jwtProvider;
    }

    /**
     * Creates and persists Registration Token for the Account.
     *
     * @param account Account for which the token is created.
     * @return Returns newly created Registration Token.
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public String createRegistrationToken(Account account) {
        String tokenValue = this.jwtProvider.generateRegistrationToken(account);

        Token registrationToken = new Token(tokenValue, account, Token.TokenType.REGISTER);
        this.tokenFacade.create(registrationToken);

        return tokenValue;
    }
}
