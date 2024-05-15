package pl.lodz.p.it.ssbd2024.ssbd03.integration.app;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.TestcontainersConfigFull;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountLoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.AccountOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.exception.AccountConstraintViolationExceptionDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.utils.JWTConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mok.AccountMessages;

import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationIntegrationIT extends TestcontainersConfigFull {

    private static final String CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;

    private static final String BASE_URL = "http://localhost:8181/api/v1";

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(ApplicationIntegrationIT.class);

    @BeforeAll
    public static void setup(){
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig()
                        .enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL)
                        .enablePrettyPrinting(true));
        // Enable global request and response logging filters
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    public void loginUsingCredentialsEndpointTestPositive() throws Exception {
        RequestSpecification request = RestAssured.given();

        AccountLoginDTO accountLoginDTO = new AccountLoginDTO("jerzybem", "P@ssw0rd!", "pl");

        String adminToken = this.login(accountLoginDTO.getLogin(), accountLoginDTO.getPassword(), accountLoginDTO.getLanguage());

        AccountOutputDTO accountOutputDTOBefore = request
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .when()
                .get(BASE_URL + "/accounts/self")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(AccountOutputDTO.class);

        assertNotNull(accountOutputDTOBefore);

        request
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .when()
                .post(BASE_URL + "/auth/logout")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract()
                .asString();

        adminToken = this.login(accountLoginDTO.getLogin(), accountLoginDTO.getPassword(), accountLoginDTO.getLanguage());

        AccountOutputDTO accountOutputDTOAfter = request
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .when()
                .get(BASE_URL + "/accounts/self")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(AccountOutputDTO.class);

        assertNotNull(accountOutputDTOAfter);

        assertNotNull(adminToken);

        DecodedJWT decodedJWT = JWT.decode(adminToken);

        assertEquals(accountLoginDTO.getLogin(), decodedJWT.getSubject());
        assertEquals(JWTConsts.TOKEN_ISSUER, decodedJWT.getIssuer());
        assertTrue(decodedJWT.getIssuedAt().before(new Date()));
        assertTrue(decodedJWT.getExpiresAt().after(new Date()));

        assertNotEquals(accountOutputDTOBefore.getLastSuccessfulLoginTime(), accountOutputDTOAfter.getLastUnsuccessfulLoginTime());
    }

    @Test
    public void loginUsingCredentialsEndpointTestNegativeWhenLoginIsInvalid() throws Exception {
        AccountLoginDTO accountLoginDTO = new AccountLoginDTO("iosif_wissarionowicz", "P@ssw0rd!", "pl");
        RequestSpecification request = RestAssured.given();

        String forbiddenKey = request
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mapper.writeValueAsString(accountLoginDTO))
                .when()
                .post(BASE_URL + "/auth/login-credentials")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract()
                .asString();

        assertNotNull(forbiddenKey);
        assertEquals(I18n.INVALID_LOGIN_ATTEMPT_EXCEPTION, forbiddenKey);
    }

    @Test
    public void loginUsingCredentialsEndpointTestNegativeWhenPasswordIsInvalid() throws Exception {
        AccountLoginDTO accountLoginDTO = new AccountLoginDTO("jerzybem", "P@ssw0rd!1", "pl");
        RequestSpecification request = RestAssured.given();

        String forbiddenKey = request
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mapper.writeValueAsString(accountLoginDTO))
                .when()
                .post(BASE_URL + "/auth/login-credentials")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract()
                .asString();

        assertNotNull(forbiddenKey);
        assertEquals(I18n.INVALID_LOGIN_ATTEMPT_EXCEPTION, forbiddenKey);
    }

    @Test
    public void loginUsingCredentialsEndpointTestNegativeWhenUserIsAlreadyLoggedIn() throws Exception {
        AccountLoginDTO accountLoginDTO = new AccountLoginDTO("jerzybem", "P@ssw0rd!", "pl");
        String previousToken = this.login(accountLoginDTO.getLogin(), accountLoginDTO.getPassword(), accountLoginDTO.getLanguage());
        RequestSpecification request = RestAssured.given();

        String forbiddenKey = request
                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(previousToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mapper.writeValueAsString(accountLoginDTO))
                .when()
                .post(BASE_URL + "/auth/login-credentials")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .extract()
                .asString();

        assertNotNull(forbiddenKey);
        assertEquals(I18n.ACCESS_DENIED_EXCEPTION, forbiddenKey);
    }

    @Test
    public void loginUsingCredentialsEndpointTestNegativeWhenUserAccountIsNotActive() throws Exception {
        AccountLoginDTO accountLoginDTO = new AccountLoginDTO("jchrystus", "P@ssw0rd!", "pl");
        RequestSpecification request = RestAssured.given();

        String accountBlockedByAdminKey = request
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mapper.writeValueAsString(accountLoginDTO))
                .when()
                .post(BASE_URL + "/auth/login-credentials")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .asString();

        assertNotNull(accountBlockedByAdminKey);
        assertEquals(I18n.ACCOUNT_INACTIVE_EXCEPTION, accountBlockedByAdminKey);
    }

    @Test
    public void loginUsingCredentialsEndpointTestNegativeWhenUserIsBlockedByAdmin() throws Exception {
        AccountLoginDTO accountLoginDTO = new AccountLoginDTO("juleswinnfield", "P@ssw0rd!", "pl");
        RequestSpecification request = RestAssured.given();

        String accountBlockedByAdminKey = request
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mapper.writeValueAsString(accountLoginDTO))
                .when()
                .post(BASE_URL + "/auth/login-credentials")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .asString();

        assertNotNull(accountBlockedByAdminKey);
        assertEquals(I18n.ACCOUNT_BLOCKED_BY_ADMIN, accountBlockedByAdminKey);
    }

    @Test
    public void loginUsingCredentialsEndpointTestNegativeWhenUserBlockedAccountByLoggingIncorrectlyTooManyTimes() throws Exception {
        AccountLoginDTO accountLoginDTO = new AccountLoginDTO("vincentvega", "P@ssw0rd!", "pl");
        RequestSpecification request = RestAssured.given();

        String accountBlockedByAdminKey = request
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mapper.writeValueAsString(accountLoginDTO))
                .when()
                .post(BASE_URL + "/auth/login-credentials")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .asString();

        assertNotNull(accountBlockedByAdminKey);
        assertEquals(I18n.ACCOUNT_BLOCKED_BY_FAILED_LOGIN_ATTEMPTS, accountBlockedByAdminKey);
    }

    @Test
    public void loginUsingCredentialsEndpointTestNegativeWhenLanguageIsInvalid() throws Exception {
        AccountLoginDTO accountLoginDTO = new AccountLoginDTO("jerzybem", "P@ssw0rd!", "SomeStringNotFollowingConstrains");
        RequestSpecification request = RestAssured.given();

        AccountConstraintViolationExceptionDTO accountConstraintViolationExceptionDTO = request
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mapper.writeValueAsString(accountLoginDTO))
                .when()
                .post(BASE_URL + "/auth/login-credentials")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(AccountConstraintViolationExceptionDTO.class);

        assertNotNull(accountConstraintViolationExceptionDTO);
        assertEquals(I18n.ACCOUNT_CONSTRAINT_VIOLATION, accountConstraintViolationExceptionDTO.getMessage());
        assertTrue(accountConstraintViolationExceptionDTO.getViolations().contains(AccountMessages.LANGUAGE_REGEX_NOT_MET));
        assertEquals(1, accountConstraintViolationExceptionDTO.getViolations().size());
    }

    private String login(String login, String password, String language) throws JsonProcessingException {
        AccountLoginDTO accountLoginDTO = new AccountLoginDTO(login, password, language);

        RequestSpecification request = RestAssured.given();
        request.contentType(CONTENT_TYPE);
        request.body(mapper.writeValueAsString(accountLoginDTO));

        Response response = request.post(BASE_URL+"/auth/login-credentials");
        return response.asString();
    }

    private String decodeJwtTokenAndExtractValue(String payload, String key) {
        String[] parts = payload.split("\\.");
        for (String part : parts) {
            byte[] dec = Base64.getDecoder().decode(part);
            String str = new String(dec);

            if (str.contains(key)) {
                // In JWT token key and value pair comes in "key":"value",
                // so the first letter of value is equal to the length of key plus 3 characters.
                str = str.substring(str.indexOf(key) + key.length() + 3);
                return str.substring(0, str.indexOf("\","));
            }
        }

        return null;
    }
}
