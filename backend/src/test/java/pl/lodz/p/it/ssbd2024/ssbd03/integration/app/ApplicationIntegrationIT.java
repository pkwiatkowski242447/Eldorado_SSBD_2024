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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.hamcrest.Matchers;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.TestcontainersConfigFull;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountLoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountRegisterDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mok.AccountMessages;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.AccountOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.exception.AccountConstraintViolationExceptionDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.utils.JWTConsts;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;

import java.util.Date;

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

        assertEquals(list2.get(0), "aandrus");
        assertEquals(list2.get(1), "adamn");
        assertEquals(list2.get(2), "jakubkoza");
        assertEquals(list2.get(3), "jchrystus");
        assertEquals(list2.get(4), "jerzybem");
        assertEquals(list2.get(5), "juleswinnfield");
        assertEquals(list2.get(6), "kamilslimak");
        assertEquals(list2.get(7), "kwotyla");
        assertEquals(list2.get(8), "loginsuccadmin");
        assertEquals(list2.get(9), "loginsuccclient");

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

    @Test
    public void getSelfInfoAboutAccountSuccessfulTest() throws JsonProcessingException {
        String loginToken = login("jerzybem", "P@ssw0rd!", "pl");
        List<String> list = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .get(BASE_URL + "/accounts/self")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("login", Matchers.equalTo("jerzybem"))
                .body("name", Matchers.equalTo("Jerzy"))
                .body("lastname", Matchers.equalTo("Bem"))
                .body("email", Matchers.equalTo("jbem@example.com"))
                .body("phoneNumber", Matchers.equalTo("111111111"))
                .body("verified", Matchers.equalTo(true))
                .body("active", Matchers.equalTo(true))
                .body("blocked", Matchers.equalTo(false))
                .body("accountLanguage", Matchers.equalTo("pl"))
                .extract()
                .jsonPath().getList("userLevelsDto.roleName");

        assertTrue(list.contains("ADMIN"));
        assertTrue(list.contains("STAFF"));
    }

    @Test
    public void getInfoAboutAccountSuccessfulTest() throws JsonProcessingException {
        String loginToken = login("jerzybem", "P@ssw0rd!", "pl");
        List<String> list = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .pathParam("id", "d20f860d-555a-479e-8783-67aee5b66692")
                .get(BASE_URL + "/accounts/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("login", Matchers.equalTo("adamn"))
                .body("name", Matchers.equalTo("Adam"))
                .body("lastname", Matchers.equalTo("Niezgodka"))
                .body("email", Matchers.equalTo("adamn@example.com"))
                .body("phoneNumber", Matchers.equalTo("200000000"))
                .body("verified", Matchers.equalTo(false))
                .body("active", Matchers.equalTo(true))
                .body("blocked", Matchers.equalTo(false))
                .body("accountLanguage", Matchers.equalTo("PL"))
                .extract()
                .jsonPath().getList("userLevelsDto.roleName");

        assertTrue(list.contains("STAFF"));
    }

    @Test
    public void getInfoABoutAccountNotFoundTest() throws JsonProcessingException {
        String loginToken = login("jerzybem", "P@ssw0rd!", "pl");
        RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .pathParam("id", "d20f900d-555a-479e-8783-67aee0b66692")
                .get(BASE_URL + "/accounts/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());

    }

    @Test
    public void getInfoAboutAccountBadRequestTest() throws JsonProcessingException {
        String loginToken = login("jerzybem", "P@ssw0rd!", "pl");
        RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .pathParam("id", "ssbdtest")
                .get(BASE_URL + "/accounts/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    public void invalidPathUnauthorized() {
        RestAssured.given()
                .when()
                .post(BASE_URL + "/not/a/real/path")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void invalidPathAuthorized() throws JsonProcessingException {
        String loginToken = login("jerzybem", "P@ssw0rd!", "pl");
        RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .post(BASE_URL + "/not/a/real/path")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void getSelfTest() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        AccountLoginDTO accountLoginDTO = new AccountLoginDTO("jerzybem", "P@ssw0rd!", "pl");

        RequestSpecification request = RestAssured.given();
        request.contentType(CONTENT_TYPE);
        request.body(mapper.writeValueAsString(accountLoginDTO));

        Response response = request.post(BASE_URL + "/auth/login-credentials");

        assertEquals(200, response.getStatusCode());
        assertEquals("jerzybem", decodeJwtTokenAndExtractValue(response.asString(), "sub"));

        request = RestAssured.given();
        request.contentType(CONTENT_TYPE);

        response = request.header("Authorization", "Bearer " + response.asString()).get(BASE_URL + "/accounts/self");

        assertEquals(200, response.getStatusCode());
    }

    @ParameterizedTest
    @MethodSource("provideNewUserLevelForAccountParameters")
    public void addAndRemoveUserLevelTestSuccessful(String id, String newUserLevel) throws JsonProcessingException {
        String loginToken = login("jerzybem", "P@ssw0rd!", "pl");
        RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .post(BASE_URL + "/accounts/{id}/add-level-{level}", id, newUserLevel)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());

        List<String> userLevels = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .get(BASE_URL + "/accounts/{id}", id)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList("userLevelsDto.roleName");
        assertTrue(userLevels.contains(newUserLevel.toUpperCase()));


        RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .post(BASE_URL + "/accounts/{id}/remove-level-{level}", id, newUserLevel)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());

        userLevels = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .get(BASE_URL + "/accounts/{id}", id)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList("userLevelsDto.roleName");

        assertFalse(userLevels.contains(newUserLevel.toUpperCase()));
    }

    @ParameterizedTest
    @MethodSource("provideOldUserLevelForAccountParameters")
    public void addUserLevelTestAccountHasUserLevel(String id, String oldUserLevel) throws JsonProcessingException {
        String loginToken = login("jerzybem", "P@ssw0rd!", "pl");
        String response = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .post(BASE_URL + "/accounts/{id}/add-level-{level}", id, oldUserLevel)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .asString();
        assertEquals(I18n.USER_LEVEL_DUPLICATED, response);
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "staff", "admin"})
    public void addUserLevelTestInvalidId(String userLevel) throws JsonProcessingException {
        String loginToken = login("jerzybem", "P@ssw0rd!", "pl");
        String response = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .post(BASE_URL + "/accounts/invalid-id/add-level-{level}", userLevel)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .asString();
        assertEquals(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION, response);
    }

    @ParameterizedTest
    @MethodSource("provideNewUserLevelForAccountParameters")
    public void addUserLevelTestUnauthorized(String id, String newUserLevel) {
        String response = RestAssured.given()
                .when()
                .post(BASE_URL + "/accounts/{id}/add-level-{level}", id, newUserLevel)
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .extract()
                .asString();
        assertEquals(I18n.ACCESS_DENIED_EXCEPTION, response);
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "staff", "admin"})
    public void addUserLevelTestAccountNotFound(String userLevel) throws JsonProcessingException {
        String loginToken = login("jerzybem", "P@ssw0rd!", "pl");
        String response = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .post(BASE_URL + "/accounts/159cf8d2-4c75-4f7f-868d-adfaa6a842c0/add-level-{level}", userLevel)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .asString();
        assertEquals(I18n.ACCOUNT_NOT_FOUND_EXCEPTION, response);
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "staff", "admin"})
    public void addUserLevelTestNotAdmin(String userLevel) throws JsonProcessingException {
        String loginToken = login("jakubkoza", "P@ssw0rd!", "pl");
        String response = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .post(BASE_URL + "/accounts/f512c0b6-40b2-4bcb-8541-46077ac02101/add-level-{level}", userLevel)
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .extract()
                .asString();
        assertEquals(I18n.ACCESS_DENIED_EXCEPTION, response);
    }

    @ParameterizedTest
    @MethodSource("provideOldUserLevelForAccountParameters")
    public void removeUserLevelTestAccountHasOneUserLevel(String id, String oldUserLevel) throws JsonProcessingException {
        String loginToken = login("jerzybem", "P@ssw0rd!", "pl");
        String response = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .post(BASE_URL + "/accounts/{id}/remove-level-{level}", id, oldUserLevel)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .asString();
        assertEquals(I18n.ONE_USER_LEVEL, response);
    }

    @ParameterizedTest
    @MethodSource("provideNewUserLevelForAccountParameters")
    public void removeUserLevelTestAccountNoSuchUserLevel(String id, String newUserLevel) throws JsonProcessingException {
        String loginToken = login("jerzybem", "P@ssw0rd!", "pl");
        String response = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .post(BASE_URL + "/accounts/{id}/remove-level-{level}", id, newUserLevel)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .asString();
        assertEquals(I18n.NO_SUCH_USER_LEVEL_EXCEPTION, response);
    }

    @Test
    public void removeUserLevelTestAdminRemovingOwnAdminUserLevel() throws JsonProcessingException {
        String loginToken = login("jerzybem", "P@ssw0rd!", "pl");
        String response = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .post(BASE_URL + "/accounts/b3b8c2ac-21ff-434b-b490-aa6d717447c0/remove-level-admin")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .asString();
        assertEquals(I18n.ADMIN_ACCOUNT_REMOVE_OWN_ADMIN_USER_LEVEL_EXCEPTION, response);
    }

    @ParameterizedTest
    @MethodSource("provideOldUserLevelForAccountParameters")
    public void removeUserLevelTestUnauthorized(String id, String oldUserLevel) {
        String response = RestAssured.given()
                .when()
                .post(BASE_URL + "/accounts/{id}/remove-level-{level}", id, oldUserLevel)
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .extract()
                .asString();
        assertEquals(I18n.ACCESS_DENIED_EXCEPTION, response);
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "staff", "admin"})
    public void removeUserLevelTestInvalidId(String userLevel) throws JsonProcessingException {
        String loginToken = login("jerzybem", "P@ssw0rd!", "pl");
        String response = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .post(BASE_URL + "/accounts/invalid-id/remove-level-{level}", userLevel)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .asString();
        assertEquals(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION, response);
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "staff", "admin"})
    public void removeUserLevelTestAccountNotFound(String userLevel) throws JsonProcessingException {
        String loginToken = login("jerzybem", "P@ssw0rd!", "pl");
        String response = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .post(BASE_URL + "/accounts/159cf8d2-4c75-4f7f-868d-adfaa6a842c0/remove-level-{level}", userLevel)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .asString();
        assertEquals(I18n.ACCOUNT_NOT_FOUND_EXCEPTION, response);
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "staff", "admin"})
    public void removeUserLevelTestNotAdmin(String userLevel) throws JsonProcessingException {
        String loginToken = login("jakubkoza", "P@ssw0rd!", "pl");
        String response = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .post(BASE_URL + "/accounts/f512c0b6-40b2-4bcb-8541-46077ac02101/remove-level-{level}", userLevel)
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .extract()
                .asString();
        assertEquals(I18n.ACCESS_DENIED_EXCEPTION, response);
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "staff", "admin"})
    public void registerByAdminTestSuccessful(String userLevel) throws JsonProcessingException {
        String loginToken = login("jerzybem", "P@ssw0rd!", "pl");
        String username = "loginsucc" + userLevel;
        String name = userLevel + "namesucc";
        String lastname = userLevel + "lastnamesucc";
        String email = userLevel + "succ@email.com";
        AccountRegisterDTO registerDTO = new AccountRegisterDTO(username, "P@ssw0rd!", name, lastname,
                email, "111111111", "pl");
        RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .contentType(CONTENT_TYPE)
                .body(registerDTO)
                .post(BASE_URL + "/register/{user_level}", userLevel)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());

        RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .param("pageNumber", 0)
                .param("pageSize", 1)
                .param("login", username)
                .param("firstName", name)
                .param("lastName", lastname)
                .param("active", false)
                .get(BASE_URL + "/accounts/match-login-firstname-and-lastname")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("[0].id", anything())
                .body("[0].login", equalTo(username))
                .body("[0].name", equalTo(name))
                .body("[0].active", equalTo(false))
                .body("[0].blocked", equalTo(false))
                .body("[0].verified", equalTo(false))
                .body("[0].userLevels[0]", equalTo(userLevel.substring(0, 1).toUpperCase() + userLevel.substring(1)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "staff", "admin"})
    public void registerByAdminTestConstraintViolation(String userLevel) throws JsonProcessingException {
        String loginToken = login("jerzybem", "P@ssw0rd!", "pl");
        String username = "login" + userLevel;
        String name = userLevel + "name";
        String lastname = userLevel + "lastname";
        String email = userLevel + ".nobueno";
        AccountRegisterDTO registerDTO = new AccountRegisterDTO(username, "P@ssw0rd!", name, lastname,
                email, "111111111", "pl");
        RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .contentType(CONTENT_TYPE)
                .body(registerDTO)
                .post(BASE_URL + "/register/{user_level}", userLevel)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo(I18n.ACCOUNT_CONSTRAINT_VIOLATION))
                .body("violations[0]", equalTo(AccountMessages.EMAIL_CONSTRAINT_NOT_MET));
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "staff", "admin"})
    public void registerByAdminTestLoginConflict(String userLevel) throws JsonProcessingException {
        String loginToken = login("jerzybem", "P@ssw0rd!", "pl");
        String username = "jerzybem";
        String name = userLevel + "name";
        String lastname = userLevel + "lastname";
        String email = userLevel + "@email.com";
        AccountRegisterDTO registerDTO = new AccountRegisterDTO(username, "P@ssw0rd!", name, lastname,
                email, "111111111", "pl");
        String response = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .contentType(CONTENT_TYPE)
                .body(registerDTO)
                .post(BASE_URL + "/register/{user_level}", userLevel)
                .then()
                .assertThat()
                .statusCode(HttpStatus.CONFLICT.value())
                .extract()
                .asString();
        assertEquals(I18n.ACCOUNT_LOGIN_ALREADY_TAKEN, response);
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "staff", "admin"})
    public void registerByAdminTestEmailConflict(String userLevel) throws JsonProcessingException {
        String loginToken = login("jerzybem", "P@ssw0rd!", "pl");
        String username = "login" + userLevel;
        String name = userLevel + "name";
        String lastname = userLevel + "lastname";
        String email = "pnowak@example.com";
        AccountRegisterDTO registerDTO = new AccountRegisterDTO(username, "P@ssw0rd!", name, lastname,
                email, "111111111", "pl");
        String response = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .contentType(CONTENT_TYPE)
                .body(registerDTO)
                .post(BASE_URL + "/register/{user_level}", userLevel)
                .then()
                .assertThat()
                .statusCode(HttpStatus.CONFLICT.value())
                .extract()
                .asString();
        assertEquals(I18n.ACCOUNT_EMAIL_ALREADY_TAKEN, response);
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "staff", "admin"})
    public void registerByUnauthorizedUserTest(String userLevel) throws JsonProcessingException {
        String loginToken = login("jakubkoza", "P@ssw0rd!", "pl");
        String username = "login" + userLevel;
        String name = userLevel + "name";
        String lastname = userLevel + "lastname";
        String email = userLevel + "@email.com";
        AccountRegisterDTO registerDTO = new AccountRegisterDTO(username, "P@ssw0rd!", name, lastname,
                email, "111111111", "pl");
        String response = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .contentType(CONTENT_TYPE)
                .body(registerDTO)
                .post(BASE_URL + "/register/{user_level}", userLevel)
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .extract()
                .asString();
        assertEquals(I18n.ACCESS_DENIED_EXCEPTION, response);

    }

    @Test
    public void registerClientByAnonymousTestSuccessful() throws JsonProcessingException {
        String loginToken = login("jerzybem", "P@ssw0rd!", "pl");
        String username = "veryUniqueLogin";
        String name = "VeryQoolName";
        String lastname = "VeryQoolLastname";
        String email = "veryunique@email.com";
        AccountRegisterDTO registerDTO = new AccountRegisterDTO(username, "P@ssw0rd!", name, lastname,
                email, "111111111", "pl");
        RestAssured.given()
                .when()
                .contentType(CONTENT_TYPE)
                .body(registerDTO)
                .post(BASE_URL + "/register/client")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());

        RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .param("pageNumber", 0)
                .param("pageSize", 1)
                .param("login", username)
                .param("firstName", name)
                .param("lastName", lastname)
                .param("active", false)
                .get(BASE_URL + "/accounts/match-login-firstname-and-lastname")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("[0].id", anything())
                .body("[0].login", equalTo(username))
                .body("[0].name", equalTo(name))
                .body("[0].active", equalTo(false))
                .body("[0].blocked", equalTo(false))
                .body("[0].verified", equalTo(false))
                .body("[0].userLevels[0]", equalTo("Client"));
    }

    @Test
    public void registerByAnonymousTestConstraintViolation() {
        String username = "veryUniqueLogin";
        String name = "VeryQoolName";
        String lastname = "VeryQoolLastname";
        String email = "veryuniqueemail.com";
        AccountRegisterDTO registerDTO = new AccountRegisterDTO(username, "P@ssw0rd!", name, lastname,
                email, "111111111", "pl");
        RestAssured.given()
                .when()
                .contentType(CONTENT_TYPE)
                .body(registerDTO)
                .post(BASE_URL + "/register/client")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo(I18n.ACCOUNT_CONSTRAINT_VIOLATION))
                .body("violations[0]", equalTo(AccountMessages.EMAIL_CONSTRAINT_NOT_MET));
    }

    @Test
    public void registerByAnonymousTestLoginConflict() {
        String username = "jerzybem";
        String name = "VeryQoolName";
        String lastname = "VeryQoolLastname";
        String email = "veryunique@email.com";
        AccountRegisterDTO registerDTO = new AccountRegisterDTO(username, "P@ssw0rd!", name, lastname,
                email, "111111111", "pl");
        String response = RestAssured.given()
                .when()
                .contentType(CONTENT_TYPE)
                .body(registerDTO)
                .post(BASE_URL + "/register/client")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CONFLICT.value())
                .extract()
                .asString();
        assertEquals(I18n.ACCOUNT_LOGIN_ALREADY_TAKEN, response);
    }

    @Test
    public void registerByAnonymousTestEmailConflict() {
        String username = "veryUniqueLogin";
        String name = "VeryQoolName";
        String lastname = "VeryQoolLastname";
        String email = "pnowak@example.com";
        AccountRegisterDTO registerDTO = new AccountRegisterDTO(username, "P@ssw0rd!", name, lastname,
                email, "111111111", "pl");
        String response = RestAssured.given()
                .header("Authorization", "Bearer ")
                .when()
                .contentType(CONTENT_TYPE)
                .body(registerDTO)
                .post(BASE_URL + "/register/client")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CONFLICT.value())
                .extract()
                .asString();
        assertEquals(I18n.ACCOUNT_EMAIL_ALREADY_TAKEN, response);
    }

    private static Stream<Arguments> provideNewUserLevelForAccountParameters() {
        return Stream.of(
                Arguments.of("9a333f13-5ccc-4109-bce3-0ad629843edf", "staff"), //aandrus
                Arguments.of("f512c0b6-40b2-4bcb-8541-46077ac02101", "client"), //tkarol
                Arguments.of("f14ac5b1-16f3-42ff-8df3-dd95de69c368", "admin") //kwotyla
        );
    }

    private static Stream<Arguments> provideOldUserLevelForAccountParameters() {
        return Stream.of(
                Arguments.of("9a333f13-5ccc-4109-bce3-0ad629843edf", "admin"), //aandrus
                Arguments.of("f512c0b6-40b2-4bcb-8541-46077ac02101", "staff"), //tkarol
                Arguments.of("f14ac5b1-16f3-42ff-8df3-dd95de69c368", "client") //kwotyla
        );
    }

    private String login(String login, String password, String language) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        AccountLoginDTO accountLoginDTO = new AccountLoginDTO(login, password, language);

        RequestSpecification request = RestAssured.given();
        request.contentType(CONTENT_TYPE);
        request.body(mapper.writeValueAsString(accountLoginDTO));

        Response response = request.post(BASE_URL + "/auth/login-credentials");
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
