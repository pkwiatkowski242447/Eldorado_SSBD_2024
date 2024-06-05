package pl.lodz.p.it.ssbd2024.ssbd03.unit.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.AccountSignableDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountOutputDTO.AccountOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.userlevel.UserLevelDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mok.UserLevelMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Admin;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.UserLevel;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.token.TokenDataExtractionException;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.utils.JWTConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.JWTProvider;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class JWTProviderTest {

    private static final JWTProvider jwtProvider = new JWTProvider();

    private static final String secretTokenKey = "ZG9lc3Rob3V3b3JkbmVydm91c3NlZWRzdHJpcGNsb3RoZXNrZXllc3NlbnRpYWxmaW5hbGF0b21pY25lZWRzZG91YmxlZXZlcnNoZWVwcmFiYml0YXZhaWxhYmxlY29sbGVjdGNvbnZlcnNhdGlvbnNhbGVjYW1lc3RlcGhpZ2hlcnNodXRkaWdjYXN0bG9zc3RyaWJlb2xkbWFuYWdlZGN1cnJlbnRkaXNjdXNzaW9ubGVhdmVlaWdodHN0b3JlcHVsbGJlc2lkZXBhcnRzY2FtZWFnb2hhdmluZ2Jyb2Fkc2hhcGVleGNsYWltZWRhcmVhdm95YWdlbG92ZXRocg==";
    private static final String otherTokenKey = "ZXhjZXB0YWN0ZGFuZ2Vyb3VzaG91c2VzdW5mb3VydGhrbm93c2hhZGVsdW5nc25lZ2F0aXZlZmxvYXRpbmdlc3NlbnRpYWxmcmVlZmxvYXRpbmdjaG9zZW51c3dlbGNvbWVwcmVwYXJlY2hhbmdpbmdhbGxvd2NlbnRlcmVhc2lseXJvc2VtaXhuZWFyZXN0d29tZW5lbnRpcmVseXBlcmlvZGhpc3dyb3RlZG91YnRjb250cm9sbGFuZ3VhZ2VyZWxpZ2lvdXNldmVyeXRoaW5nZmxhZ2ltcHJvdmVjb29raWVzbWlsZXdlaWdodG5lcnZvdXNtYWNoaW5laGlnaA==";
    private static final int authenticationCodeValidityLengthValue = 5;
    private static final int jwtTokenValidityLengthValue = 15;
    private static Account accountNo1;
    private static Client clientLevelNo1;
    private static Admin adminLevelNo1;
    private final String exampleUUID = "a0fe1f00-cc87-4dfa-8102-a135bb6c0e31";

    @BeforeAll
    public static void init() throws Exception {
        accountNo1 = new Account("exampleLogin1", "examplePassword1", "exampleFirstName1", "exampleLastName1", "email1@example.com", "examplePhoneNumber1");
        clientLevelNo1 = new Client();
        adminLevelNo1 = new Admin();
        Field id = AbstractEntity.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(clientLevelNo1, UUID.randomUUID());
        id.set(adminLevelNo1, UUID.randomUUID());
        id.setAccessible(false);
        clientLevelNo1.setAccount(accountNo1);
        adminLevelNo1.setAccount(accountNo1);
        accountNo1.addUserLevel(clientLevelNo1);
        accountNo1.addUserLevel(adminLevelNo1);
    }

    @BeforeEach
    public void initializeTestData() {
        try {
            Field secretKey = JWTProvider.class.getDeclaredField("secretKey");
            secretKey.setAccessible(true);
            secretKey.set(jwtProvider, secretTokenKey);
            secretKey.setAccessible(false);

            Field authenticationCodeValidityLength = JWTProvider.class.getDeclaredField("authenticationCodeValidityLength");
            authenticationCodeValidityLength.setAccessible(true);
            authenticationCodeValidityLength.set(jwtProvider, authenticationCodeValidityLengthValue);
            authenticationCodeValidityLength.setAccessible(false);

            Field jwtTokenValidityLength = JWTProvider.class.getDeclaredField("accessTokenTTL");
            jwtTokenValidityLength.setAccessible(true);
            jwtTokenValidityLength.set(jwtProvider, jwtTokenValidityLengthValue);
            jwtTokenValidityLength.setAccessible(false);
        } catch (Exception exception) {
            log.error("Exception: {} occurred during initialization phase of the test class for JWTProvider. Cause: {}",
                    exception.getClass().getSimpleName(), exception.getMessage());
        }
    }

    @Test
    public void jwtProviderGenerateJWTTokenTestPositive() throws Exception {
        Field id = AbstractEntity.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(accountNo1, UUID.fromString(exampleUUID));
        id.setAccessible(false);

        String jwtToken = jwtProvider.generateJWTToken(accountNo1);

        assertNotNull(jwtToken);
        DecodedJWT decodedJWT = JWT.decode(jwtToken);
        assertEquals(exampleUUID, decodedJWT.getClaim(JWTConsts.ACCOUNT_ID).asString());
        assertEquals(accountNo1.getLogin(), decodedJWT.getSubject());
        assertEquals(JWTConsts.TOKEN_ISSUER, decodedJWT.getIssuer());
        assertTrue(decodedJWT.getExpiresAt().after(new Date()));
        assertTrue(decodedJWT.getIssuedAt().before(new Date()));
        List<String> listOfRoles = decodedJWT.getClaim("user_levels").asList(String.class);
        assertEquals(2, listOfRoles.size());
        assertTrue(listOfRoles.contains("ROLE_CLIENT"));
        assertTrue(listOfRoles.contains("ROLE_ADMIN"));
    }

    @Test
    public void jwtProviderGenerateActionTokenTestPositive() throws Exception {
        Field id = AbstractEntity.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(accountNo1, UUID.fromString(exampleUUID));
        id.setAccessible(false);

        int exampleTTL = 12;
        ChronoUnit exampleChronoUnit = ChronoUnit.HOURS;
        String actionToken = jwtProvider.generateActionToken(accountNo1, exampleTTL, exampleChronoUnit);

        assertNotNull(actionToken);
        DecodedJWT decodedJWT = JWT.decode(actionToken);
        assertEquals(exampleUUID, decodedJWT.getClaim(JWTConsts.ACCOUNT_ID).asString());
        assertEquals(accountNo1.getLogin(), decodedJWT.getSubject());
        assertEquals(JWTConsts.TOKEN_ISSUER, decodedJWT.getIssuer());
        assertTrue(decodedJWT.getExpiresAt().after(new Date()));
        assertTrue(decodedJWT.getIssuedAt().before(new Date()));
        assertEquals(Duration.of(exampleTTL, exampleChronoUnit), Duration.between(decodedJWT.getIssuedAt().toInstant(), decodedJWT.getExpiresAt().toInstant()));
    }

    @Test
    public void jwtProviderGenerateEmailTokenTestPositive() throws Exception {
        Field id = AbstractEntity.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(accountNo1, UUID.fromString(exampleUUID));
        id.setAccessible(false);

        int exampleTTL = 24;
        ChronoUnit exampleChronoUnit = ChronoUnit.HOURS;
        String exampleEmailAddress = "exampleEmail@example.com";
        String emailChangeToken = jwtProvider.generateEmailToken(accountNo1, exampleEmailAddress, exampleTTL);

        assertNotNull(emailChangeToken);
        DecodedJWT decodedJWT = JWT.decode(emailChangeToken);
        assertEquals(exampleUUID, decodedJWT.getClaim(JWTConsts.ACCOUNT_ID).asString());
        assertEquals(accountNo1.getLogin(), decodedJWT.getSubject());
        assertEquals(exampleEmailAddress, decodedJWT.getClaim(JWTConsts.EMAIL).asString());
        assertEquals(JWTConsts.TOKEN_ISSUER, decodedJWT.getIssuer());
        assertTrue(decodedJWT.getExpiresAt().after(new Date()));
        assertTrue(decodedJWT.getIssuedAt().before(new Date()));
        assertEquals(Duration.of(exampleTTL, exampleChronoUnit), Duration.between(decodedJWT.getIssuedAt().toInstant(), decodedJWT.getExpiresAt().toInstant()));
    }

    @Test
    public void jwtProviderExtractAccountIdTestPositive() throws Exception {
        Field id = AbstractEntity.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(accountNo1, UUID.fromString(exampleUUID));
        id.setAccessible(false);

        String jwtToken = jwtProvider.generateJWTToken(accountNo1);
        UUID userAccountId = jwtProvider.extractAccountId(jwtToken);

        assertNotNull(userAccountId);
        assertEquals(exampleUUID, userAccountId.toString());
    }

    @Test
    public void jwtProviderExtractAccountIdTestNegative() throws Exception {
        Field id = AbstractEntity.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(accountNo1, UUID.fromString(exampleUUID));
        id.setAccessible(false);

        List<String> listOfRoles = new LinkedList<>();
        accountNo1.getUserLevels().forEach(userLevel -> listOfRoles.add("ROLE_" + userLevel.getClass().getSimpleName().toUpperCase()));
        String jwtToken = JWT.create()
                .withSubject(accountNo1.getLogin())
                .withClaim(JWTConsts.ACCOUNT_ID, accountNo1.getId().toString())
                .withClaim(JWTConsts.USER_LEVELS, listOfRoles)
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(15, ChronoUnit.MINUTES))
                .withIssuer(JWTConsts.TOKEN_ISSUER)
                .sign(Algorithm.HMAC256(otherTokenKey));
        assertThrows(TokenDataExtractionException.class, () -> jwtProvider.extractAccountId(jwtToken));
    }

    @Test
    public void jwtProviderExtractAccountLoginTestPositive() throws Exception {
        Field id = AbstractEntity.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(accountNo1, UUID.fromString(exampleUUID));
        id.setAccessible(false);

        String jwtToken = jwtProvider.generateJWTToken(accountNo1);
        String userLogin = jwtProvider.extractUsername(jwtToken);

        assertNotNull(jwtToken);
        assertEquals(accountNo1.getLogin(), userLogin);
    }

    @Test
    public void jwtProviderExtractAccountLoginTestNegative() throws Exception {
        Field id = AbstractEntity.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(accountNo1, UUID.fromString(exampleUUID));
        id.setAccessible(false);

        List<String> listOfRoles = new LinkedList<>();
        accountNo1.getUserLevels().forEach(userLevel -> listOfRoles.add("ROLE_" + userLevel.getClass().getSimpleName().toUpperCase()));
        String jwtToken = JWT.create()
                .withSubject(accountNo1.getLogin())
                .withClaim(JWTConsts.ACCOUNT_ID, accountNo1.getId().toString())
                .withClaim(JWTConsts.USER_LEVELS, listOfRoles)
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(15, ChronoUnit.MINUTES))
                .withIssuer(JWTConsts.TOKEN_ISSUER)
                .sign(Algorithm.HMAC256(otherTokenKey));
        assertThrows(TokenDataExtractionException.class, () -> jwtProvider.extractUsername(jwtToken));
    }

    @Test
    public void jwtProviderExtractAccountEmailTestPositive() throws Exception {
        Field id = AbstractEntity.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(accountNo1, UUID.fromString(exampleUUID));
        id.setAccessible(false);

        int exampleTTL = 24;
        String exampleEmailAddress = "exampleEmail@example.com";
        String emailChangeToken = jwtProvider.generateEmailToken(accountNo1, exampleEmailAddress, exampleTTL);
        String userEmail = jwtProvider.extractEmail(emailChangeToken);

        assertNotNull(userEmail);
        assertEquals(exampleEmailAddress, userEmail);
    }

    @Test
    public void jwtProviderExtractAccountEmailTestNegative() throws Exception {
        Field id = AbstractEntity.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(accountNo1, UUID.fromString(exampleUUID));
        id.setAccessible(false);

        int exampleTTL = 24;
        String exampleEmailAddress = "exampleEmail@example.com";
        String emailChangeToken = JWT.create()
                .withSubject(accountNo1.getLogin())
                .withClaim(JWTConsts.ACCOUNT_ID, accountNo1.getId().toString())
                .withClaim(JWTConsts.EMAIL, exampleEmailAddress)
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(exampleTTL, ChronoUnit.HOURS))
                .withIssuer(JWTConsts.TOKEN_ISSUER)
                .sign(Algorithm.HMAC256(otherTokenKey));
        assertThrows(TokenDataExtractionException.class, () -> jwtProvider.extractEmail(emailChangeToken));
    }

    @Test
    public void jwtProviderIsTokenValidTestPositive() throws Exception {
        Field id = AbstractEntity.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(accountNo1, UUID.fromString(exampleUUID));
        id.setAccessible(false);

        String jwtToken = jwtProvider.generateJWTToken(accountNo1);

        assertNotNull(jwtToken);
        assertTrue(jwtProvider.isTokenValid(jwtToken, accountNo1));
    }

    @Test
    public void jwtProviderIsTokenValidTestNegative() throws Exception {
        Field id = AbstractEntity.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(accountNo1, UUID.fromString(exampleUUID));
        id.setAccessible(false);

        List<String> listOfRoles = new LinkedList<>();
        accountNo1.getUserLevels().forEach(userLevel -> listOfRoles.add("ROLE_" + userLevel.getClass().getSimpleName().toUpperCase()));
        String jwtToken = JWT.create()
                .withSubject(accountNo1.getLogin())
                .withClaim(JWTConsts.ACCOUNT_ID, accountNo1.getId().toString())
                .withClaim(JWTConsts.USER_LEVELS, listOfRoles)
                .withIssuedAt(Instant.now().minus(30, ChronoUnit.MINUTES))
                .withExpiresAt(Instant.now().minus(15, ChronoUnit.MINUTES))
                .withIssuer(JWTConsts.TOKEN_ISSUER)
                .sign(Algorithm.HMAC256(secretTokenKey));

        assertNotNull(jwtToken);
        assertFalse(jwtProvider.isTokenValid(jwtToken, accountNo1));
    }

    @Test
    public void jwtProviderGetSignInKeyTestPositive() throws Exception {
        Method getSignInKey = JWTProvider.class.getDeclaredMethod("getSignInKey", (Class[]) null);
        getSignInKey.setAccessible(true);
        String secretKey = (String) getSignInKey.invoke(jwtProvider);

        assertNotNull(secretKey);
        assertFalse(secretKey.isEmpty());
        assertFalse(secretKey.isBlank());
        assertEquals(256, secretKey.length());
    }

    @Test
    public void jwtProviderGenerateMultiFactorAuthTokenTestPositive() throws Exception {
        Field id = AbstractEntity.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(accountNo1, UUID.fromString(exampleUUID));
        id.setAccessible(false);

        String exampleCode = "exampleCode";
        String multiFactorAuthToken = jwtProvider.generateMultiFactorAuthToken(exampleCode);

        assertNotNull(multiFactorAuthToken);
        DecodedJWT decodedJWT = JWT.decode(multiFactorAuthToken);
        assertEquals(exampleCode, decodedJWT.getClaim(JWTConsts.CODE_VALUE).asString());
        assertEquals(JWTConsts.TOKEN_ISSUER, decodedJWT.getIssuer());
        assertTrue(decodedJWT.getExpiresAt().after(new Date()));
        assertTrue(decodedJWT.getIssuedAt().before(new Date()));
        assertEquals(Duration.of(authenticationCodeValidityLengthValue, ChronoUnit.MINUTES),
                Duration.between(decodedJWT.getIssuedAt().toInstant(), decodedJWT.getExpiresAt().toInstant()));
    }

    @Test
    public void jwtProviderExtractHashedAuthCodeFromMultiFactorAuthTokenTestPositive() throws Exception {
        Field id = AbstractEntity.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(accountNo1, UUID.fromString(exampleUUID));
        id.setAccessible(false);

        String exampleCode = "exampleCode";
        String multiFactorAuthToken = jwtProvider.generateMultiFactorAuthToken(exampleCode);
        String hashedAuthCode = jwtProvider.extractHashedCodeValueFromToken(multiFactorAuthToken);

        assertNotNull(hashedAuthCode);
        assertEquals(exampleCode, hashedAuthCode);
    }

    @Test
    public void jwtProviderExtractHashedAuthCodeFromMultiFactorAuthTokenTestNegativeSignatureInvalid() throws Exception {
        Field id = AbstractEntity.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(accountNo1, UUID.fromString(exampleUUID));
        id.setAccessible(false);

        String exampleCode = "exampleCode";
        String multiFactorAuthToken = JWT.create()
                .withClaim(JWTConsts.CODE_VALUE, exampleCode)
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(authenticationCodeValidityLengthValue, ChronoUnit.MINUTES))
                .withIssuer(JWTConsts.TOKEN_ISSUER)
                .sign(Algorithm.HMAC256(otherTokenKey));
        assertThrows(TokenDataExtractionException.class, () -> jwtProvider.extractHashedCodeValueFromToken(multiFactorAuthToken));
    }

    @Test
    public void jwtProviderExtractHashedAuthCodeFromMultiFactorAuthTokenTestNegativeTokenExpired() throws Exception {
        Field id = AbstractEntity.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(accountNo1, UUID.fromString(exampleUUID));
        id.setAccessible(false);

        String exampleCode = "exampleCode";
        String multiFactorAuthToken = JWT.create()
                .withClaim(JWTConsts.CODE_VALUE, exampleCode)
                .withIssuedAt(Instant.now().minus(authenticationCodeValidityLengthValue * 2, ChronoUnit.MINUTES))
                .withExpiresAt(Instant.now().minus(authenticationCodeValidityLengthValue, ChronoUnit.MINUTES))
                .withIssuer(JWTConsts.TOKEN_ISSUER)
                .sign(Algorithm.HMAC256(secretTokenKey));
        assertThrows(TokenDataExtractionException.class, () -> jwtProvider.extractHashedCodeValueFromToken(multiFactorAuthToken));
    }

    @Test
    public void jwtProviderIsMultiFactorAuthTokenValidTestPositive() throws Exception {
        Field id = AbstractEntity.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(accountNo1, UUID.fromString(exampleUUID));
        id.setAccessible(false);

        String exampleCode = "exampleCode";
        String multiFactorAuthToken = jwtProvider.generateMultiFactorAuthToken(exampleCode);

        assertNotNull(multiFactorAuthToken);
        assertTrue(jwtProvider.isMultiFactorAuthTokenValid(multiFactorAuthToken));
    }

    @Test
    public void jwtProviderIsMultiFactorAuthTokenValidTestNegative() throws Exception {
        Field id = AbstractEntity.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(accountNo1, UUID.fromString(exampleUUID));
        id.setAccessible(false);

        String exampleCode = "exampleCode";
        String multiFactorAuthToken = JWT.create()
                .withClaim(JWTConsts.CODE_VALUE, exampleCode)
                .withIssuedAt(Instant.now().minus(authenticationCodeValidityLengthValue * 2, ChronoUnit.MINUTES))
                .withExpiresAt(Instant.now().minus(authenticationCodeValidityLengthValue, ChronoUnit.MINUTES))
                .withIssuer(JWTConsts.TOKEN_ISSUER)
                .sign(Algorithm.HMAC256(otherTokenKey));

        assertNotNull(multiFactorAuthToken);
        assertFalse(jwtProvider.isMultiFactorAuthTokenValid(multiFactorAuthToken));
    }

    @SuppressWarnings("InstantiationOfUtilityClass")
    @Test
    public void jwtProviderGenerateObjectSignature() throws Exception {
        UserLevelMapper userLevelMapper = new UserLevelMapper();
        Set<UserLevelDTO> setOfUserLevelDTO = new HashSet<>();
        for (UserLevel userLevel : accountNo1.getUserLevels()) {
            setOfUserLevelDTO.add(UserLevelMapper.toUserLevelDTO(userLevel));
        }

        UUID exampleId = UUID.randomUUID();
        String exampleLogin = "exampleLogin";
        long exampleVersion = 10L;
        boolean exampleSuspended = true;
        boolean exampleActive = true;
        boolean exampleBlocked = false;
        boolean twoFactorAuth = true;
        LocalDateTime blockedTime = null;
        LocalDateTime creationTime = LocalDateTime.now();
        LocalDateTime lastSuccessfulLoginTime = LocalDateTime.now();
        LocalDateTime lastUnsuccessfulLoginTime = LocalDateTime.now();
        String accountLanguage = "exampleLanguage";
        String lastSuccessfulLoginIp = "exampleSuccessfulLoginIp";
        String lastUnsuccessfulLoginIp = "exampleUnsuccessfulLoginIp";
        String phoneNumber = "examplePhoneNumber";
        String lastname = "exampleLastName";
        String name = "exampleFirstName";
        String email = "exampleMail@example.com";
        AccountSignableDTO accountSignableDTO = new AccountOutputDTO(exampleLogin,
                exampleVersion,
                setOfUserLevelDTO,
                exampleId,
                exampleSuspended,
                exampleActive,
                exampleBlocked,
                twoFactorAuth,
                blockedTime,
                creationTime,
                lastSuccessfulLoginTime,
                lastUnsuccessfulLoginTime,
                accountLanguage,
                lastSuccessfulLoginIp,
                lastUnsuccessfulLoginIp,
                phoneNumber,
                lastname,
                name,
                email);

        String signature = jwtProvider.generateObjectSignature(accountSignableDTO);

        assertNotNull(signature);
        assertFalse(signature.isEmpty());
        assertFalse(signature.isBlank());
    }
}
