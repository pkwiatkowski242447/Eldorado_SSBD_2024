package pl.lodz.p.it.ssbd2024.ssbd03.mok.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.Token;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.TokenFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces.TokenServiceInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.JWTProvider;

import java.time.temporal.ChronoUnit;

/**
 * Service managing Tokens.
 *
 * @see Token
 */
@Slf4j
@Service
public class TokenService implements TokenServiceInterface {

    @Value("${account.password.reset.period.length.minutes}")
    private int passwordResetPeriodLengthMinutes;

    /**
     * TokenFacade used for operations on token entities.
     */
    private final TokenFacade tokenFacade;
    /**
     * JWTProvider used for managing JWT TOKENS.
     */
    private final JWTProvider jwtProvider;

    /**
     * Autowired constructor for the service.
     *
     * @param tokenFacade   Facade containing method for token manipulation.
     * @param jwtProvider   Component used to provide JWT token generation mechanism for a certain payload.
     */
    @Autowired
    public TokenService(TokenFacade tokenFacade,
                        JWTProvider jwtProvider) {
        this.tokenFacade = tokenFacade;
        this.jwtProvider = jwtProvider;
    }

    /**
     * Creates and persists registration token for the Account.
     *
     * @param account Account for which the token is created.
     * @return Returns newly created registration token value.
     */
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public String createRegistrationToken(Account account) throws ApplicationBaseException {
        String tokenValue = this.jwtProvider.generateActionToken(account,24, ChronoUnit.HOURS);

        Token registrationToken = new Token(tokenValue, account, Token.TokenType.REGISTER);
        this.tokenFacade.create(registrationToken);

        return tokenValue;
    }

    /**
     * Creates and persists E-mail confirmation Token for the Account.
     * Removes any e-mail confirmation tokens related to the given account previously on the database.
     *
     * @param account Account for which the token is created.
     * @return Token's value(JWT).
     * @throws ApplicationBaseException General superclass for all exceptions thrown by exception handling aspects in facade
     * layer.
     */
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public String createEmailConfirmationToken(Account account, String email) throws ApplicationBaseException{
        tokenFacade.findByTypeAndAccount(Token.TokenType.CONFIRM_EMAIL, account.getId()).ifPresent(tokenFacade::remove);

        String tokenValue = this.jwtProvider.generateEmailToken(account, email, 24);
        Token emailToken = new Token(tokenValue, account, Token.TokenType.CONFIRM_EMAIL);
        this.tokenFacade.create(emailToken);

        return emailToken.getTokenValue();
    }

    /**
     * Creates and persists password reset token for the Account.
     *
     * @param account Account for which the token is created.
     *
     * @return Returns newly created password reset token value.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by exception handling aspects in facade
     * layer.
     */
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public String createPasswordResetToken(Account account) throws ApplicationBaseException {
        tokenFacade.findByTypeAndAccount(Token.TokenType.RESET_PASSWORD, account.getId()).ifPresent(tokenFacade::remove);

        String tokenValue = this.jwtProvider.generateActionToken(account, this.passwordResetPeriodLengthMinutes, ChronoUnit.MINUTES);
        Token passwordToken = new Token(tokenValue, account, Token.TokenType.RESET_PASSWORD);
        this.tokenFacade.create(passwordToken);

        return passwordToken.getTokenValue();
    }

    /**
     * Removes token from the database if exists.
     *
     * @param token Confirmation token value of the token to be removed
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeAccountsEmailConfirmationToken(String token) {
        tokenFacade.findByTypeAndAccount(Token.TokenType.CONFIRM_EMAIL,
                jwtProvider.extractAccountId(token)).ifPresent(tokenFacade::remove);
    }
}
