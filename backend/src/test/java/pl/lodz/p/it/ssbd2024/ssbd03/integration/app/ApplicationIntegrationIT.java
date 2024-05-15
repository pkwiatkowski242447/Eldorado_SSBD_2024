package pl.lodz.p.it.ssbd2024.ssbd03.integration.app;

import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
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
import java.util.UUID;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class ApplicationIntegrationIT extends TestcontainersConfigFull {

    private static final String CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;

    private static final String BASE_URL = "http://localhost:8181/api/v1";

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
    public void loginEndpointTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AccountLoginDTO accountLoginDTO = new AccountLoginDTO("jerzybem", "P@ssw0rd!", "pl");

        RequestSpecification request = RestAssured.given();
        request.contentType(CONTENT_TYPE);
        request.body(mapper.writeValueAsString(accountLoginDTO));

        Response response = request.post(BASE_URL + "/auth/login-credentials");

        assertEquals(200, response.getStatusCode());
        assertEquals("jerzybem", decodeJwtTokenAndExtractValue(response.asString(), "sub"));
    }

    @Test
    public void invalidPathUnauthorized() {
        RestAssured.given()
                .when()
                .post(BASE_URL+"/not/a/real/path")
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
                .post(BASE_URL+"/not/a/real/path")
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

        Response response = request.post(BASE_URL +"/auth/login-credentials");

        assertEquals(200, response.getStatusCode());
        assertEquals("jerzybem", decodeJwtTokenAndExtractValue(response.asString(), "sub"));

        request = RestAssured.given();
        request.contentType(CONTENT_TYPE);

        response = request.header("Authorization", "Bearer " + response.asString()).get(BASE_URL+"/accounts/self");

        assertEquals(200, response.getStatusCode());
    }

    @ParameterizedTest
    @MethodSource("provideNewUserLevelForAccountParameters")
    public void addAndRemoveUserLevelTestSuccessful(String id, String newUserLevel) throws JsonProcessingException {
       String loginToken = login("jerzybem", "P@ssw0rd!", "pl");
        RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .post(BASE_URL+"/accounts/{id}/add-level-{level}", id, newUserLevel)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());

        List<String> userLevels = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .get(BASE_URL+"/accounts/{id}", id)
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
                .post(BASE_URL+"/accounts/{id}/remove-level-{level}", id, newUserLevel)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());

        userLevels = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .get(BASE_URL+"/accounts/{id}", id)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList("userLevelsDto.roleName");;

        assertFalse(userLevels.contains(newUserLevel.toUpperCase()));
    }

    @ParameterizedTest
    @MethodSource("provideOldUserLevelForAccountParameters")
    public void addUserLevelTestAccountHasUserLevel(String id, String oldUserLevel) throws JsonProcessingException {
        String loginToken = login("jerzybem", "P@ssw0rd!", "pl");
        String response = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .post(BASE_URL+"/accounts/{id}/add-level-{level}", id, oldUserLevel)
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
                .post(BASE_URL+"/accounts/invalid-id/add-level-{level}", userLevel)
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
                .post(BASE_URL+"/accounts/{id}/add-level-{level}", id, newUserLevel)
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
                .post(BASE_URL+"/accounts/159cf8d2-4c75-4f7f-868d-adfaa6a842c0/add-level-{level}", userLevel)
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
                .post(BASE_URL+"/accounts/f512c0b6-40b2-4bcb-8541-46077ac02101/add-level-{level}", userLevel)
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
                .post(BASE_URL+"/accounts/{id}/remove-level-{level}", id, oldUserLevel)
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
                .post(BASE_URL+"/accounts/{id}/remove-level-{level}", id, newUserLevel)
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
                .post(BASE_URL+"/accounts/b3b8c2ac-21ff-434b-b490-aa6d717447c0/remove-level-admin")
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
                .post(BASE_URL+"/accounts/{id}/remove-level-{level}", id, oldUserLevel)
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
                .post(BASE_URL+"/accounts/invalid-id/remove-level-{level}", userLevel)
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
                .post(BASE_URL+"/accounts/159cf8d2-4c75-4f7f-868d-adfaa6a842c0/remove-level-{level}", userLevel)
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
                .post(BASE_URL+"/accounts/f512c0b6-40b2-4bcb-8541-46077ac02101/remove-level-{level}", userLevel)
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
        String username = "login" + userLevel;
        String name = userLevel + "name";
        String lastname = userLevel+"lastname";
        String email = userLevel+"@email.com";
        AccountRegisterDTO registerDTO = new AccountRegisterDTO(username, "P@ssw0rd!", name, lastname,
                email, "111111111", "pl");
        RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .contentType(CONTENT_TYPE)
                .body(registerDTO)
                .post(BASE_URL+"/register/{user_level}", userLevel)
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
                .get(BASE_URL+"/accounts/match-login-firstname-and-lastname")
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
        String lastname = userLevel+"lastname";
        String email = userLevel+".nobueno";
        AccountRegisterDTO registerDTO = new AccountRegisterDTO(username, "P@ssw0rd!", name, lastname,
                email, "111111111", "pl");
        RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .contentType(CONTENT_TYPE)
                .body(registerDTO)
                .post(BASE_URL+"/register/{user_level}", userLevel)
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
        String lastname = userLevel+"lastname";
        String email = userLevel+"@email.com";
        AccountRegisterDTO registerDTO = new AccountRegisterDTO(username, "P@ssw0rd!", name, lastname,
                email, "111111111", "pl");
        String response = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .contentType(CONTENT_TYPE)
                .body(registerDTO)
                .post(BASE_URL+"/register/{user_level}", userLevel)
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
        String lastname = userLevel+"lastname";
        String email = "pnowak@example.com";
        AccountRegisterDTO registerDTO = new AccountRegisterDTO(username, "P@ssw0rd!", name, lastname,
                email, "111111111", "pl");
        String response = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .contentType(CONTENT_TYPE)
                .body(registerDTO)
                .post(BASE_URL+"/register/{user_level}", userLevel)
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
        String lastname = userLevel+"lastname";
        String email = userLevel+"@email.com";
        AccountRegisterDTO registerDTO = new AccountRegisterDTO(username, "P@ssw0rd!", name, lastname,
                email, "111111111", "pl");
        String response = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .contentType(CONTENT_TYPE)
                .body(registerDTO)
                .post(BASE_URL+"/register/{user_level}", userLevel)
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .extract()
                .asString();
        assertEquals(I18n.ACCESS_DENIED_EXCEPTION, response);
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
