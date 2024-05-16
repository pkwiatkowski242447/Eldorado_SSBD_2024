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

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ApplicationIntegrationIT extends TestcontainersConfigFull {

    private static final String CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;
    private static final String BASE_URL = "http://localhost:8181/api/v1";
    private static final ObjectMapper mapper = new ObjectMapper();


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

    @Test
    public void logoutTestSuccessfulLogoutAfterLogin() throws JsonProcessingException {
        String loginToken = login("jerzybem", "P@ssw0rd!", "pl");
        RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .post(BASE_URL + "/auth/logout")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void logoutAsUnauthenticatedUser() {
        RestAssured.given().when().post(BASE_URL + "/auth/logout").then().assertThat().statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void logoutAfterSuccessfulLogout() throws JsonProcessingException {
        String loginToken = login("jerzybem", "P@ssw0rd!", "pl");
        RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .post(BASE_URL + "/auth/logout");

        RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .post(BASE_URL + "/auth/logout")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void getAllUsersReturnListAndOKStatusCode() throws JsonProcessingException {
        String loginToken = login("jerzybem", "P@ssw0rd!", "pl");

        List<String> list1 = RestAssured
                .given()
                .header("Authorization", "Bearer " + loginToken)
                .param("pageNumber", 0)
                .param("pageSize", 3)
                .get(BASE_URL+"/accounts")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList("id");

        assertEquals(3, list1.size());

        List<String> list2 = RestAssured
                .given()
                .header("Authorization", "Bearer " + loginToken)
                .param("pageNumber", 0)
                .param("pageSize", 10)
                .get(BASE_URL+"/accounts")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList("login");

        assertEquals(list2.get(0), "adamn");
        assertEquals(list2.get(1), "jakubkoza");
        assertEquals(list2.get(2), "jchrystus");
        assertEquals(list2.get(3), "jerzybem");
        assertEquals(list2.get(4), "juleswinnfield");
        assertEquals(list2.get(5), "kamilslimak");
        assertEquals(list2.get(6), "michalkowal");
        assertEquals(list2.get(7), "piotrnowak");
        assertEquals(list2.get(8), "tonyhalik");
        assertEquals(list2.get(9), "vincentvega");


        List<String> list3 = RestAssured
                .given()
                .header("Authorization", "Bearer " + loginToken)
                .param("pageNumber", 1)
                .param("pageSize", 3)
                .get(BASE_URL+"/accounts")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList("id");

        assertEquals(3, list3.size());
    }

    @Test
    public void getAllUsersReturnNoContent() throws JsonProcessingException {
        String loginToken = login("jerzybem", "P@ssw0rd!", "pl");

        RestAssured
                .given()
                .header("Authorization", "Bearer " + loginToken)
                .param("pageNumber", 4)
                .param("pageSize", 5)
                .get(BASE_URL+"/accounts")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void getAllUsersAsUnauthenticatedUserForbidden() {
        RestAssured
                .given()
                .param("pageNumber", 0)
                .param("pageSize", 5)
                .get(BASE_URL + "/accounts")
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void getAllUsersAsUnauthorizedUserForbidden() throws JsonProcessingException {
        String loginToken = login("jakubkoza", "P@ssw0rd!", "pl");

        RestAssured
                .given()
                .header("Authorization", "Bearer " + loginToken)
                .param("pageNumber", 0)
                .param("pageSize", 5)
                .get(BASE_URL + "/accounts")
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    private String login(String login, String password, String language) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        AccountLoginDTO accountLoginDTO = new AccountLoginDTO(login, password, language);

        RequestSpecification request = RestAssured.given();
        request.contentType(CONTENT_TYPE);
        request.body(mapper.writeValueAsString(accountLoginDTO));

        Response response = request.post(BASE_URL+"/auth/login-credentials");
        return response.asString();
    }
}
