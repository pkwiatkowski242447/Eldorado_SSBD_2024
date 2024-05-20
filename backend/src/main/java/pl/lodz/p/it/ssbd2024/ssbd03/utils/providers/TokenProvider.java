package pl.lodz.p.it.ssbd2024.ssbd03.utils.providers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Token;

import java.time.temporal.ChronoUnit;

@Component
@Slf4j
public class TokenProvider {

    @Value("${refresh.token.validity.period.length.minutes}")
    private int refreshTokenTTL;

    @Value("${account.password.reset.period.length.minutes}")
    private int passwordResetTokenTTL;

    @Value("${account.creation.confirmation.period.length.hours}")
    private int accountActivationTokenTTL;

    @Value("${email.change.confirmation.period.length.hours}")
    private int emailChangeTokenTTL;

    private final JWTProvider jwtProvider;

    public TokenProvider(JWTProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public Token generateRefreshToken(Account account) {
        String tokenValue = jwtProvider.generateActionToken(account, refreshTokenTTL, ChronoUnit.MINUTES);
        return new Token(tokenValue, account, Token.TokenType.REFRESH_TOKEN);
    }

    public Token generateMultiFactorAuthToken(Account account, String hashedAuthCode) {
        String tokenValue = jwtProvider.generateMultiFactorAuthToken(hashedAuthCode);
        return new Token(tokenValue, account, Token.TokenType.MULTI_FACTOR_AUTHENTICATION_CODE);
    }

    public Token generatePasswordResetToken(Account account) {
        String tokenValue = jwtProvider.generateActionToken(account, passwordResetTokenTTL, ChronoUnit.MINUTES);
        return new Token(tokenValue, account, Token.TokenType.RESET_PASSWORD);
    }

    public Token generateAccountActivationToken(Account account) {
        String tokenValue = jwtProvider.generateActionToken(account, accountActivationTokenTTL, ChronoUnit.HOURS);
        return new Token(tokenValue, account, Token.TokenType.REGISTER);
    }

    public Token generateEmailChangeToken(Account account, String newEmail) {
        String tokenValue = jwtProvider.generateEmailToken(account, newEmail, emailChangeTokenTTL);
        return new Token(tokenValue, account, Token.TokenType.CONFIRM_EMAIL);
    }
}
