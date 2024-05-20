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

    private final JWTProvider jwtProvider;

    public TokenProvider(JWTProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public Token generateRefreshToken(Account account) {
        String tokenValue = jwtProvider.generateActionToken(account, refreshTokenTTL, ChronoUnit.MINUTES);
        return new Token(tokenValue, account, Token.TokenType.REFRESH_TOKEN);
    }
}
