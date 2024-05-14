package pl.lodz.p.it.ssbd2024.ssbd03.utils.providers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.SignableDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.utils.JWTConsts;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Component used for all operations connected with JSON Web Tokens.
 */
@Component
public class JWTProvider {

    @Value("${authentication.code.validity.period.length.minutes}")
    private int authenticationCodeValidityLength;

    private String secretKey;

    /**
     * Generates random secret key used for signing tokens.
     * New key is after each system boot.
     */
    @PostConstruct
    private void generateSignInKey() {
        byte[] keyBytes = new byte[JWTConsts.SECRET_KEY_LENGTH];
        for (int i = 0; i < keyBytes.length; i++) {
            keyBytes[i] = (byte) Math.floor(Math.random() * 128);
        }
        this.secretKey = Base64.getEncoder().encodeToString(keyBytes);
    }

    /**
     * Generates new JSON Web Token used to keep track of user session.
     * Token payload includes:
     * <ul>
     *     <li>sub - Login of the Account for which the token was issued</li>
     *     <li>account_id - ID of the Account for which the token was issued</li>
     *     <li>user_levels - List of Account user levels</li>
     *     <li>iat - Issue time of the token</li>
     *     <li>exp - Expiry time of the token</li>
     *     <li>iss - Token issuer</li>
     * </ul>
     *
     * @param account Account used to create the payload.
     * @return Returns new JSON Web Token.
     */
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
                .sign(Algorithm.HMAC256(this.getSignInKey()));
    }

    /**
     * Generates JWT used for keeping track of different actions which require confirmation.
     *
     * @param account    Account to which the change is related to.
     * @param tokenTTL   Token's time to live in specified time unit (by chronoUnit parameter).
     * @param chronoUnit Unit of time, that will be used to determine how long should the token live.
     * @return Returns a signed Json Web Token.
     */
    public String generateActionToken(Account account, int tokenTTL, ChronoUnit chronoUnit) {
        return JWT.create()
                .withSubject(account.getLogin())
                .withClaim(JWTConsts.ACCOUNT_ID, account.getId().toString())
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(tokenTTL, chronoUnit))
                .withIssuer(JWTConsts.TOKEN_ISSUER)
                .sign(Algorithm.HMAC256(this.getSignInKey()));
    }

    /**
     * Generates JWT used for keeping track of different actions which require confirmation.
     *
     * @param account  Account to which the change is related to.
     * @param tokenTTL Token's time to live in hours.
     * @return Returns a signed Json Web Token.
     */
    public String generateEmailToken(Account account, String email, int tokenTTL) {
        return JWT.create()
                .withSubject(account.getLogin())
                .withClaim(JWTConsts.ACCOUNT_ID, account.getId().toString())
                .withClaim(JWTConsts.EMAIL, email)
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(tokenTTL, ChronoUnit.HOURS))
                .withIssuer(JWTConsts.TOKEN_ISSUER)
                .sign(Algorithm.HMAC256(this.getSignInKey()));
    }

    /**
     * Extracts AccountID from the Token after verifying and decoding it.
     *
     * @param jwtToken Token from which the AccountID will be extracted.
     * @return Returns AccountID from the Token.
     */
    public UUID extractAccountId(String jwtToken) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(this.getSignInKey())).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(jwtToken);
        return UUID.fromString(decodedJWT.getClaim(JWTConsts.ACCOUNT_ID).asString());
    }

    /**
     * Extracts username from the Token after verifying and decoding it.
     *
     * @param jwtToken Token from which the username will be extracted.
     * @return Returns username from the Token.
     */
    public String extractUsername(String jwtToken) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(this.getSignInKey())).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(jwtToken);
        return decodedJWT.getSubject();
    }

    /**
     * Extracts email from the Token after verifying and decoding it.
     *
     * @param jwtToken Token from which the email will be extracted.
     * @return String containing new email.
     */
    public String extractEmail(String jwtToken) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(this.getSignInKey())).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(jwtToken);
        return decodedJWT.getClaim(JWTConsts.EMAIL).asString();
    }

    /**
     * Checks validity of the token.
     *
     * @param jwtToken Token to be checked.
     * @param account  Account for which the token was issued.
     * @return Returns true if token is valid, otherwise returns false.
     */
    public boolean isTokenValid(String jwtToken, Account account) {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(this.getSignInKey()))
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

    /**
     * Decodes the key to String format and returns it.
     *
     * @return Returns signing key.
     */
    private String getSignInKey() {
        byte[] keyBytes = Base64.getDecoder().decode(this.secretKey);
        return new String(keyBytes);
    }

    // Multifactor Auth

    /**
     * Method used to generate JWT token, which is valid for next couple of minutes
     * (default: 5 minutes), containing code used for second step in two factor
     * authentication.
     */
    public String generateMultiFactorAuthToken(String codeValue) {
        return JWT.create()
                .withClaim(JWTConsts.CODE_VALUE, codeValue)
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(this.authenticationCodeValidityLength, ChronoUnit.MINUTES))
                .withIssuer(JWTConsts.TOKEN_ISSUER)
                .sign(Algorithm.HMAC256(this.getSignInKey()));
    }

    public String extractHashedCodeValueFromToken(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(this.getSignInKey())).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT.getClaim(JWTConsts.CODE_VALUE).asString();
    }

    public boolean isMultiFactorAuthTokenValid(String multiFactorAuthToken) {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(this.getSignInKey()))
                    .withIssuer(JWTConsts.TOKEN_ISSUER)
                    .build();
            DecodedJWT decodedJWT = jwtVerifier.verify(multiFactorAuthToken);
            return decodedJWT.getExpiresAt().after(new Date());
        } catch (JWTVerificationException exception) {
            return false;
        }
    }

    //=================================================JWS==========================================================\\

    /**
     * Generates JWT used for prevent certain specified fields from being modified between requests.
     *
     * @param signableDTO Object that should be signed.
     * @return Returns a signed Json Web Token.
     */
    public String generateObjectSignature(SignableDTO signableDTO) {
        return JWT
                .create()
                .withPayload(signableDTO.getSigningFields())
                .sign(Algorithm.HMAC256(this.getSignInKey()));
    }
}
