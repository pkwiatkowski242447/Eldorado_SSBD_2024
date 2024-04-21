package pl.lodz.p.it.ssbd2024.ssbd03.utils.providers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.*;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.JWTConsts;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class JWTProvider {

    private String secretKey;

    @PostConstruct
    private void generateSignInKey() {
        byte[] keyBytes = new byte[JWTConsts.SECRET_KEY_LENGTH];
        for (int i = 0; i < keyBytes.length; i++) {
            keyBytes[i] = (byte) Math.floor(Math.random() * 128);
        }
        this.secretKey = Base64.getEncoder().encodeToString(keyBytes);
    }

    public String generateJWTToken(Account account) {
        List<String> listOfRoles = new LinkedList<>();
        account.getUserLevels().forEach(userLevel -> listOfRoles.add("ROLE_" + userLevel.getClass().getSimpleName().toUpperCase()));

        return JWT.create()
                .withSubject(account.getLogin())
                .withClaim(JWTConsts.ACCOUNT_ID, account.getId().toString())
                .withClaim(JWTConsts.USER_LEVELS, listOfRoles)
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(15, ChronoUnit.MINUTES))
                .withIssuer(JWTConsts.TOKEN_ISSUER)
                .sign(Algorithm.HMAC256(this.getSingInKey()));
    }

    public String generateRegistrationToken(Account account) {
        return JWT.create()
                .withSubject(account.getLogin())
                .withClaim(JWTConsts.ACCOUNT_ID, account.getId().toString())
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(24, ChronoUnit.HOURS))
                .withIssuer(JWTConsts.TOKEN_ISSUER)
                .sign(Algorithm.HMAC256(this.getSingInKey()));
    }

    public UUID extractAccountId(String jwtToken) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(getSingInKey())).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(jwtToken);
        return UUID.fromString(decodedJWT.getClaim(JWTConsts.ACCOUNT_ID).asString());
    }

    public String extractUsername(String jwtToken) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(getSingInKey())).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(jwtToken);
        return decodedJWT.getSubject();
    }

    public boolean isTokenValid(String jwtToken, Account account) {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(getSingInKey()))
                    .withSubject(account.getLogin())
                    .withClaim(JWTConsts.ACCOUNT_ID, account.getId().toString())
                    .withIssuer(JWTConsts.TOKEN_ISSUER)
                    .build();
            DecodedJWT decodedJWT = jwtVerifier.verify(jwtToken);
            return decodedJWT.getExpiresAt().after(new Date());
        } catch (JWTVerificationException exception) {
            return false;
        }
    }

    private String getSingInKey() {
        byte[] keyBytes = Base64.getDecoder().decode(this.secretKey);
        return new String(keyBytes);
    }
}
