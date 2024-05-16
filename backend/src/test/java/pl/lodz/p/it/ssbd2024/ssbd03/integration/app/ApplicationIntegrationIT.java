package pl.lodz.p.it.ssbd2024.ssbd03.integration.app;

import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.TestcontainersConfigFull;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountLoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountModifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.AccountOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApplicationIntegrationIT extends TestcontainersConfigFull {

    private static final String CONTENT_TYPE_JSON = MediaType.APPLICATION_JSON_VALUE;

    private static final String BASE_URL = "http://localhost:8181/api/v1";

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    public static void setup() {
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig()
                        .enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL)
                        .enablePrettyPrinting(true));
        // Enable global request and response logging filters
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    public void loginEndpointTest() throws Exception {
        AccountLoginDTO accountLoginDTO = new AccountLoginDTO("jerzybem", "P@ssw0rd!", "pl");

        RequestSpecification request = RestAssured.given();
        request.contentType(CONTENT_TYPE_JSON);
        request.body(mapper.writeValueAsString(accountLoginDTO));

        Response response = request.post(BASE_URL + "/auth/login-credentials");

        assertEquals(200, response.getStatusCode());
        assertEquals("jerzybem", decodeJwtTokenAndExtractValue(response.asString(), "sub"));
    }

    @Test
    public void getSelfTest() throws JsonProcessingException {
        AccountLoginDTO accountLoginDTO = new AccountLoginDTO("jerzybem", "P@ssw0rd!", "pl");

        RequestSpecification request = RestAssured.given();
        request.contentType(CONTENT_TYPE_JSON);
        request.body(mapper.writeValueAsString(accountLoginDTO));

        Response response = request.post(BASE_URL + "/auth/login-credentials");

        assertEquals(200, response.getStatusCode());
        assertEquals("jerzybem", decodeJwtTokenAndExtractValue(response.asString(), "sub"));

        request = RestAssured.given();
        request.contentType(CONTENT_TYPE_JSON);

        response = request.header("Authorization", "Bearer " + response.asString()).get(BASE_URL + "/accounts/self");

        assertEquals(200, response.getStatusCode());
    }

    // Block and unblock

    @Test
    public void blockAndUnblockAccountByAdminTestSuccessful() throws IOException {
        String loginToken = this.login("jerzybem", "P@ssw0rd!", "pl");
        RequestSpecification requestSpec = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken);
        String userId = "e0bf979b-6b42-432d-8462-544d88b1ab5f";

        // Check before blocking
        RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(BASE_URL + String.format("/accounts/%s", userId))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "id", Matchers.equalTo(userId),
                        "blocked", Matchers.equalTo(false),
                        "blockedTime", Matchers.equalTo(null)
                );

        // Block account
        RestAssured.given()
                .spec(requestSpec)
                .when()
                .post(BASE_URL + String.format("/accounts/%s/block", userId))
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        // Check after blocking
        RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(BASE_URL + String.format("/accounts/%s", userId))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "id", Matchers.equalTo(userId),
                        "blocked", Matchers.equalTo(true),
                        "blockedTime", Matchers.equalTo(null)
                );

        // Unblock account
        RestAssured.given()
                .spec(requestSpec)
                .when()
                .post(BASE_URL + String.format("/accounts/%s/unblock", userId))
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        // Check after unblocking
        RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(BASE_URL + String.format("/accounts/%s", userId))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "id", Matchers.equalTo(userId),
                        "blocked", Matchers.equalTo(false),
                        "blockedTime", Matchers.equalTo(null)
                );
    }

    // Negative blocking

    @Test
    public void blockAccountByAdminTestFailedNoLogin() throws IOException {
        RequestSpecification request = RestAssured.given();
        String userId = "e0bf979b-6b42-432d-8462-544d88b1ab5f";

        // Try to block account
        String response = request
                .when()
                .post(BASE_URL + String.format("/accounts/%s/block", userId))
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .extract()
                .asString();

        assertEquals(I18n.ACCESS_DENIED_EXCEPTION, response);
    }

    @ParameterizedTest
    @MethodSource("provideNoAdminLevelAccountsParameters")
    public void blockAccountByAdminTestFailedNoAdminRole(String login) throws IOException {
        String loginToken = this.login(login, "P@ssw0rd!", "pl");
        RequestSpecification request = RestAssured.given();
        request.header("Authorization", "Bearer " + loginToken);
        String userId = "e0bf979b-6b42-432d-8462-544d88b1ab5f";

        // Try to block account
        String response = request
                .when()
                .post(BASE_URL + String.format("/accounts/%s/block", userId))
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .extract()
                .asString();

        assertEquals(I18n.ACCESS_DENIED_EXCEPTION, response);
    }

    @Test
    public void blockAccountByAdminTestFailedAccountNotFound() throws IOException {
        String loginToken = this.login("jerzybem", "P@ssw0rd!", "pl");
        RequestSpecification request = RestAssured.given();
        request.header("Authorization", "Bearer " + loginToken);
        String userId = UUID.randomUUID().toString();

        // Try to block account
        String response = request
                .when()
                .post(BASE_URL + String.format("/accounts/%s/block", userId))
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .asString();

        assertEquals(I18n.ACCOUNT_NOT_FOUND_EXCEPTION, response);
    }

    @Test
    public void blockAccountByAdminTestFailedTryToBlockOwnAccount() throws IOException {
        String loginToken = this.login("jerzybem", "P@ssw0rd!", "pl");
        RequestSpecification request = RestAssured.given();
        request.header("Authorization", "Bearer " + loginToken);
        String userId = "b3b8c2ac-21ff-434b-b490-aa6d717447c0";

        // Try to block account
        String response = request
                .when()
                .post(BASE_URL + String.format("/accounts/%s/block", userId))
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .extract()
                .asString();

        assertEquals(I18n.ACCOUNT_TRY_TO_BLOCK_OWN_EXCEPTION, response);
    }

    @Test
    public void blockAccountByAdminTestFailedTryToBlockBlockedAccount() throws IOException {
        String loginToken = this.login("jerzybem", "P@ssw0rd!", "pl");
        RequestSpecification requestSpec = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken);
        String userId = "e0bf979b-6b42-432d-8462-544d88b1ab5f";

        // Block account
        RestAssured.given()
                .spec(requestSpec)
                .when()
                .post(BASE_URL + String.format("/accounts/%s/block", userId))
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        // Try to block account second time
        String response = RestAssured.given()
                .spec(requestSpec)
                .when()
                .post(BASE_URL + String.format("/accounts/%s/block", userId))
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .extract()
                .asString();

        assertEquals(I18n.ACCOUNT_ALREADY_BLOCKED, response);

        //------------------------------------------------------------------------------------//

        // Unblock account
        RestAssured.given()
                .spec(requestSpec)
                .when()
                .post(BASE_URL + String.format("/accounts/%s/unblock", userId))
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        // Check after unblocking
        RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(BASE_URL + String.format("/accounts/%s", userId))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "id", Matchers.equalTo(userId),
                        "blocked", Matchers.equalTo(false),
                        "blockedTime", Matchers.equalTo(null)
                );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUUIDParameters")
    public void blockAccountByAdminTestFailedInvalidUUID(String userId) throws IOException {
        String loginToken = this.login("jerzybem", "P@ssw0rd!", "pl");
        RequestSpecification request = RestAssured.given();
        request.header("Authorization", "Bearer " + loginToken);

        // Try to block account
        String response = request
                .when()
                .post(BASE_URL + String.format("/accounts/%s/block", userId))
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .asString();

        assertEquals(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION, response);
    }

    // Negative unblocking

    @Test
    public void unblockAccountByAdminTestFailedNoLogin() {
        RequestSpecification request = RestAssured.given();
        String userId = "e0bf979b-6b42-432d-8462-544d88b1ab5f";

        // Try to unblock account
        String response = request
                .when()
                .post(BASE_URL + String.format("/accounts/%s/unblock", userId))
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .extract()
                .asString();

        assertEquals(I18n.ACCESS_DENIED_EXCEPTION, response);
    }

    @ParameterizedTest
    @MethodSource("provideNoAdminLevelAccountsParameters")
    public void unblockAccountByAdminTestFailedNoAdminRole(String login) throws IOException {
        String loginToken = this.login(login, "P@ssw0rd!", "pl");
        RequestSpecification request = RestAssured.given();
        request.header("Authorization", "Bearer " + loginToken);
        String userId = "e0bf979b-6b42-432d-8462-544d88b1ab5f";

        // Try to block account
        String response = request
                .when()
                .post(BASE_URL + String.format("/accounts/%s/unblock", userId))
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .extract()
                .asString();

        assertEquals(I18n.ACCESS_DENIED_EXCEPTION, response);
    }

    @Test
    public void unblockAccountByAdminTestFailedAccountNotFound() throws IOException {
        String loginToken = this.login("jerzybem", "P@ssw0rd!", "pl");
        RequestSpecification request = RestAssured.given();
        request.header("Authorization", "Bearer " + loginToken);
        String userId = UUID.randomUUID().toString();

        // Try to block account
        String response = request
                .when()
                .post(BASE_URL + String.format("/accounts/%s/unblock", userId))
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .asString();

        assertEquals(I18n.ACCOUNT_NOT_FOUND_EXCEPTION, response);
    }

    @Test
    public void unblockAccountByAdminTestFailedTryToUnblockUnblockedAccount() throws IOException {
        String loginToken = this.login("jerzybem", "P@ssw0rd!", "pl");
        RequestSpecification request = RestAssured.given();
        request.header("Authorization", "Bearer " + loginToken);
        String userId = "e0bf979b-6b42-432d-8462-544d88b1ab5f";

        // Unblock account
        String response = request
                .when()
                .post(BASE_URL + String.format("/accounts/%s/unblock", userId))
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .extract()
                .asString();

        assertEquals(I18n.ACCOUNT_ALREADY_UNBLOCKED, response);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUUIDParameters")
    public void unblockAccountByAdminTestFailedInvalidUUID(String userId) throws IOException {
        String loginToken = this.login("jerzybem", "P@ssw0rd!", "pl");
        RequestSpecification request = RestAssured.given();
        request.header("Authorization", "Bearer " + loginToken);

        // Try to un block account
        String response = request
                .when()
                .post(BASE_URL + String.format("/accounts/%s/unblock", userId))
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .asString();

        assertEquals(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION, response);
    }

    // Modify self account

    @ParameterizedTest
    @MethodSource("provideAllLevelAccountsParameters")
    public void modifyAccountSelfTestSuccessful(String login) throws IOException {
        String loginToken = this.login(login, "P@ssw0rd!", "pl");
        RequestSpecification requestSpec = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken);

        // Check before modifying
        Response responseBefore = RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(BASE_URL + "/accounts/self")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "login", Matchers.equalTo(login),
                        "name", Matchers.not("Ebenezer"),
                        "phoneNumber", Matchers.not("133111222")
                )
                .extract()
                .response();

        AccountOutputDTO accountOutputDTO = responseBefore.as(AccountOutputDTO.class);

        AccountModifyDTO accountModifyDTO = toAccountModifyDTO(accountOutputDTO);
        accountModifyDTO.setName("Ebenezer");
        accountModifyDTO.setPhoneNumber("133111222");

        // Modify account
        RestAssured.given()
                .spec(requestSpec)
                .when()
                .header("If-Match", responseBefore.getHeader("ETag").replace("\"", ""))
                .contentType(CONTENT_TYPE_JSON)
                .body(accountModifyDTO)
                .put(BASE_URL + "/accounts/self")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "login", Matchers.equalTo(login),
                        "name", Matchers.equalTo("Ebenezer"),
                        "phoneNumber", Matchers.equalTo("133111222")
                );

        // Check after modifying
        RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(BASE_URL + "/accounts/self")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "login", Matchers.equalTo(login),
                        "name", Matchers.equalTo("Ebenezer"),
                        "phoneNumber", Matchers.equalTo("133111222")
                );
    }

    // Negative modify self account

    @Test
    public void modifyAccountSelfTestFailedNoLogin() throws IOException {
        String loginToken = this.login("jerzybem", "P@ssw0rd!", "pl");

        // Check before modifying
        Response responseBefore = RestAssured.given()
                .when()
                .header("Authorization", "Bearer " + loginToken)
                .get(BASE_URL + "/accounts/self")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "login", Matchers.equalTo("jerzybem")
                )
                .extract()
                .response();

        AccountOutputDTO accountOutputDTO = responseBefore.as(AccountOutputDTO.class);
        AccountModifyDTO accountModifyDTO = toAccountModifyDTO(accountOutputDTO);
        accountModifyDTO.setName("Adam");

        // Try to modify account
        String responseModify = RestAssured.given()
                .when()
                .header("If-Match", responseBefore.getHeader("ETag").replace("\"", ""))
                .contentType(CONTENT_TYPE_JSON)
                .body(accountModifyDTO)
                .put(BASE_URL + "/accounts/self")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .extract()
                .asString();

        assertEquals(I18n.ACCESS_DENIED_EXCEPTION, responseModify);
    }

    @ParameterizedTest
    @MethodSource("provideAllLevelAccountsParameters")
    public void modifyAccountSelfTestFailedDataIntegrityCompromised(String login) throws IOException {
        String loginToken = this.login(login, "P@ssw0rd!", "pl");
        RequestSpecification requestSpec = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken);

        // Check before modifying
        Response responseBefore = RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(BASE_URL + "/accounts/self")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "login", Matchers.equalTo(login),
                        "name", Matchers.not("Alalalala")
                )
                .extract()
                .response();

        AccountOutputDTO accountOutputDTO = responseBefore.as(AccountOutputDTO.class);

        AccountModifyDTO accountModifyDTO = toAccountModifyDTO(accountOutputDTO);

        accountModifyDTO.setLogin("newLogin");
        accountModifyDTO.setName("Alalalala");

        // Modify account
        String responseModify = RestAssured.given()
                .spec(requestSpec)
                .when()
                .header("If-Match", responseBefore.getHeader("ETag").replace("\"", ""))
                .contentType(CONTENT_TYPE_JSON)
                .body(accountModifyDTO)
                .put(BASE_URL + "/accounts/self")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .asString();

        assertEquals(I18n.DATA_INTEGRITY_COMPROMISED, responseModify);

        // Check after modifying
        RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(BASE_URL + "/accounts/self")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "login", Matchers.equalTo(login),
                        "name", Matchers.not("Alalalala")
                );
    }

    @ParameterizedTest
    @MethodSource("provideAllLevelAccountsParametersAndNotValidIfMatch")
    public void modifyAccountSelfTestFailedInvalidIfMatch(String login, String ifMatch) throws IOException {
        String loginToken = this.login(login, "P@ssw0rd!", "pl");
        RequestSpecification requestSpec = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken);

        // Check before modifying
        Response responseBefore = RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(BASE_URL + "/accounts/self")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "login", Matchers.equalTo(login),
                        "name", Matchers.not("Alalalala")
                )
                .extract()
                .response();

        AccountOutputDTO accountOutputDTO = responseBefore.as(AccountOutputDTO.class);

        AccountModifyDTO accountModifyDTO = toAccountModifyDTO(accountOutputDTO);
        accountModifyDTO.setName("Alalalala");

        // Modify account
        String responseModify = RestAssured.given()
                .spec(requestSpec)
                .when()
                .header("If-Match", ifMatch)
                .contentType(CONTENT_TYPE_JSON)
                .body(accountModifyDTO)
                .put(BASE_URL + "/accounts/self")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .asString();

        assertEquals(I18n.MISSING_HEADER_IF_MATCH, responseModify);
    }

    @ParameterizedTest
    @MethodSource("provideAllLevelAccountsParameters")
    public void modifyAccountSelfTestFailedOptimisticLock(String login) throws IOException {
        String loginToken = this.login(login, "P@ssw0rd!", "pl");
        RequestSpecification requestSpec = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken);

        // Get before modifying v1
        Response responseBefore_V1 = RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(BASE_URL + "/accounts/self")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(CONTENT_TYPE_JSON)
                .body(
                        "login", Matchers.equalTo(login),
                        "lastname", Matchers.not(Matchers.equalTo("Bbbbbb"))
                )
                .extract()
                .response();

        AccountOutputDTO accountOutputDTO_V1 = responseBefore_V1.as(AccountOutputDTO.class);

        AccountModifyDTO accountModifyDTO_V1 = toAccountModifyDTO(accountOutputDTO_V1);
        accountModifyDTO_V1.setLastname("Bbbbbb");

        // Get before modifying v2
        Response responseBefore_V2 = RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(BASE_URL + "/accounts/self")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "login", Matchers.equalTo(login),
                        "lastname", Matchers.not(Matchers.equalTo("Bbbbbb"))
                )
                .extract()
                .response();

        AccountOutputDTO accountOutputDTO_V2 = responseBefore_V2.as(AccountOutputDTO.class);

        AccountModifyDTO accountModifyDTO_2 = toAccountModifyDTO(accountOutputDTO_V2);
        accountModifyDTO_2.setLastname("Ccccc");

        // Modify account v1
        RestAssured.given()
                .spec(requestSpec)
                .when()
                .header("If-Match", responseBefore_V1.getHeader("ETag").replace("\"", ""))
                .contentType(CONTENT_TYPE_JSON)
                .body(accountModifyDTO_V1)
                .put(BASE_URL + "/accounts/self")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "login", Matchers.equalTo(login),
                        "lastname", Matchers.equalTo("Bbbbbb")
                );

        // Modify account v2
        String responseModify_V2 = RestAssured.given()
                .spec(requestSpec)
                .when()
                .header("If-Match", responseBefore_V1.getHeader("ETag").replace("\"", ""))
                .contentType(CONTENT_TYPE_JSON)
                .body(accountModifyDTO_V1)
                .put(BASE_URL + "/accounts/self")
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .extract()
                .asString();

        assertEquals(I18n.OPTIMISTIC_LOCK_EXCEPTION, responseModify_V2);

        // Check after modifying
        RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(BASE_URL + "/accounts/self")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "login", Matchers.equalTo(login),
                        "lastname", (Matchers.equalTo("Bbbbbb")
                        ));
    }

    @ParameterizedTest
    @MethodSource("provideAllLevelAccountsParameters")
    public void modifyAccountSelfTestFailedConstraintViolation(String login) throws IOException {
        String loginToken = this.login(login, "P@ssw0rd!", "pl");
        RequestSpecification requestSpec = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken);

        // Check before modifying
        Response responseBefore = RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(BASE_URL + "/accounts/self")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "login", Matchers.equalTo(login),
                        "name", Matchers.any(String.class)
                )
                .extract()
                .response();

        AccountOutputDTO accountOutputDTO = responseBefore.as(AccountOutputDTO.class);
        String currentName = accountOutputDTO.getName();

        AccountModifyDTO accountModifyDTO = toAccountModifyDTO(accountOutputDTO);
        accountModifyDTO.setName("A".repeat(100));

        // Modify account
        RestAssured.given()
                .spec(requestSpec)
                .when()
                .header("If-Match", responseBefore.getHeader("ETag").replace("\"", ""))
                .contentType(CONTENT_TYPE_JSON)
                .body(accountModifyDTO)
                .put(BASE_URL + "/accounts/self")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(
                        "message", Matchers.equalTo("account.constraint.violation.exception"),
                        "violations", Matchers.hasSize(2),
                        "violations", Matchers.containsInAnyOrder(
                                Matchers.equalTo("bean.validation.account.first.name.too.long"),
                                Matchers.equalTo("bean.validation.account.first.name.regex.not.met")

                        )
                );

        // Check after modifying
        RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(BASE_URL + "/accounts/self")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "login", Matchers.equalTo(login),
                        "name", Matchers.equalTo(currentName)
                );
    }

    // Modify other user

    @Test
    public void modifyUserAccountTestSuccessful() throws IOException {
        String loginToken = this.login("jerzybem", "P@ssw0rd!", "pl");
        RequestSpecification requestSpec = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken);
        String userId = "02b0d9d7-a472-48d0-95e0-13a3e6a11d00";

        // Check before modifying
        Response responseBefore = RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(BASE_URL + String.format("/accounts/%s", userId))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "login", Matchers.equalTo("piotrnowak"),
                        "name", Matchers.not("Ebenezer"),
                        "phoneNumber", Matchers.not("133111222")
                )
                .extract()
                .response();

        AccountOutputDTO accountOutputDTO = responseBefore.as(AccountOutputDTO.class);

        AccountModifyDTO accountModifyDTO = toAccountModifyDTO(accountOutputDTO);
        accountModifyDTO.setName("Ebenezer");
        accountModifyDTO.setPhoneNumber("133111222");

        // Modify account
        RestAssured.given()
                .spec(requestSpec)
                .when()
                .header("If-Match", responseBefore.getHeader("ETag").replace("\"", ""))
                .contentType(CONTENT_TYPE_JSON)
                .body(accountModifyDTO)
                .put(BASE_URL + "/accounts")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "login", Matchers.equalTo("piotrnowak"),
                        "name", Matchers.equalTo("Ebenezer"),
                        "phoneNumber", Matchers.equalTo("133111222")
                );

        // Check after modifying
        RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(BASE_URL + String.format("/accounts/%s", userId))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "login", Matchers.equalTo("piotrnowak"),
                        "name", Matchers.equalTo("Ebenezer"),
                        "phoneNumber", Matchers.equalTo("133111222")
                );
    }

    // Negative modify other user

    @Test
    public void modifyUserAccountTestFailedNoLogin() throws IOException {
        String loginToken = this.login("jerzybem", "P@ssw0rd!", "pl");
        String userId = "02b0d9d7-a472-48d0-95e0-13a3e6a11d00";

        // Check before modifying as Admin
        Response responseBefore = RestAssured.given()
                .when()
                .header("Authorization", "Bearer " + loginToken)
                .get(BASE_URL + String.format("/accounts/%s", userId))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "login", Matchers.equalTo("piotrnowak")
                )
                .extract()
                .response();

        AccountOutputDTO accountOutputDTO = responseBefore.as(AccountOutputDTO.class);
        AccountModifyDTO accountModifyDTO = toAccountModifyDTO(accountOutputDTO);
        accountModifyDTO.setName("Adam");

        // Try to modify account
        String responseModify = RestAssured.given()
                .when()
                .header("If-Match", responseBefore.getHeader("ETag").replace("\"", ""))
                .contentType(CONTENT_TYPE_JSON)
                .body(accountModifyDTO)
                .put(BASE_URL + "/accounts")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .extract()
                .asString();

        assertEquals(I18n.ACCESS_DENIED_EXCEPTION, responseModify);
    }

    @ParameterizedTest
    @MethodSource("provideNoAdminLevelAccountsParameters")
    public void modifyUserAccountTestFailedNoAdminRole(String login) throws IOException {
        String loginTokenAdmin = this.login("jerzybem", "P@ssw0rd!", "pl");
        String userId = "02b0d9d7-a472-48d0-95e0-13a3e6a11d00";

        // Check before modifying as Admin
        Response responseBefore = RestAssured.given()
                .when()
                .header("Authorization", "Bearer " + loginTokenAdmin)
                .get(BASE_URL + String.format("/accounts/%s", userId))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "login", Matchers.equalTo("piotrnowak")
                )
                .extract()
                .response();

        AccountOutputDTO accountOutputDTO = responseBefore.as(AccountOutputDTO.class);
        AccountModifyDTO accountModifyDTO = toAccountModifyDTO(accountOutputDTO);
        accountModifyDTO.setName("Adam");


        String loginTokenNoAdmin = this.login(login, "P@ssw0rd!", "pl");
        // Try to modify account
        String responseModify = RestAssured.given()
                .when()
                .header("Authorization", "Bearer " + loginTokenNoAdmin)
                .header("If-Match", responseBefore.getHeader("ETag").replace("\"", ""))
                .contentType(CONTENT_TYPE_JSON)
                .body(accountModifyDTO)
                .put(BASE_URL + "/accounts")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .extract()
                .asString();

        assertEquals(I18n.ACCESS_DENIED_EXCEPTION, responseModify);
    }

    @Test
    public void modifyUserAccountTestFailedDataIntegrityCompromised() throws IOException {
        String loginToken = this.login("jerzybem", "P@ssw0rd!", "pl");
        RequestSpecification requestSpec = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken);
        String userId = "02b0d9d7-a472-48d0-95e0-13a3e6a11d00";

        // Check before modifying
        Response responseBefore = RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(BASE_URL + String.format("/accounts/%s", userId))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "login", Matchers.equalTo("piotrnowak"),
                        "name", Matchers.not("Alalalala")
                )
                .extract()
                .response();

        AccountOutputDTO accountOutputDTO = responseBefore.as(AccountOutputDTO.class);

        AccountModifyDTO accountModifyDTO = toAccountModifyDTO(accountOutputDTO);

        accountModifyDTO.setLogin("newLogin");
        accountModifyDTO.setName("Alalalala");

        // Modify account
        String responseModify = RestAssured.given()
                .spec(requestSpec)
                .when()
                .header("If-Match", responseBefore.getHeader("ETag").replace("\"", ""))
                .contentType(CONTENT_TYPE_JSON)
                .body(accountModifyDTO)
                .put(BASE_URL + "/accounts")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .asString();

        assertEquals(I18n.DATA_INTEGRITY_COMPROMISED, responseModify);

        // Check after modifying
        RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(BASE_URL + String.format("/accounts/%s", userId))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "login", Matchers.equalTo("piotrnowak"),
                        "name", Matchers.not("Alalalala")
                );
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  "})
    public void modifyUserAccountTestFailedInvalidIfMatch(String ifMatch) throws IOException {
        String loginToken = this.login("jerzybem", "P@ssw0rd!", "pl");
        RequestSpecification requestSpec = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken);
        String userId = "02b0d9d7-a472-48d0-95e0-13a3e6a11d00";

        // Check before modifying
        Response responseBefore = RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(BASE_URL + String.format("/accounts/%s", userId))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "login", Matchers.equalTo("piotrnowak"),
                        "name", Matchers.not("Alalalala")
                )
                .extract()
                .response();

        AccountOutputDTO accountOutputDTO = responseBefore.as(AccountOutputDTO.class);

        AccountModifyDTO accountModifyDTO = toAccountModifyDTO(accountOutputDTO);
        accountModifyDTO.setName("Alalalala");

        // Modify account
        String responseModify = RestAssured.given()
                .spec(requestSpec)
                .when()
                .header("If-Match", ifMatch)
                .contentType(CONTENT_TYPE_JSON)
                .body(accountModifyDTO)
                .put(BASE_URL + "/accounts")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .asString();

        assertEquals(I18n.MISSING_HEADER_IF_MATCH, responseModify);
    }

    @Test
    public void modifyUserAccountTestFailedOptimisticLock() throws IOException {
        String loginToken = this.login("jerzybem", "P@ssw0rd!", "pl");
        RequestSpecification requestSpec = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken);
        String userId = "02b0d9d7-a472-48d0-95e0-13a3e6a11d00";

        // Get before modifying v1
        Response responseBefore_V1 = RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(BASE_URL + String.format("/accounts/%s", userId))
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(CONTENT_TYPE_JSON)
                .body(
                        "login", Matchers.equalTo("piotrnowak"),
                        "lastname", Matchers.not(Matchers.equalTo("Bbbbbb"))
                )
                .extract()
                .response();

        AccountOutputDTO accountOutputDTO_V1 = responseBefore_V1.as(AccountOutputDTO.class);

        AccountModifyDTO accountModifyDTO_V1 = toAccountModifyDTO(accountOutputDTO_V1);
        accountModifyDTO_V1.setLastname("Bbbbbb");

        // Get before modifying v2
        Response responseBefore_V2 = RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(BASE_URL + String.format("/accounts/%s", userId))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "login", Matchers.equalTo("piotrnowak"),
                        "lastname", Matchers.not(Matchers.equalTo("Bbbbbb"))
                )
                .extract()
                .response();

        AccountOutputDTO accountOutputDTO_V2 = responseBefore_V2.as(AccountOutputDTO.class);

        AccountModifyDTO accountModifyDTO_2 = toAccountModifyDTO(accountOutputDTO_V2);
        accountModifyDTO_2.setLastname("Ccccc");

        // Modify account v1
        RestAssured.given()
                .spec(requestSpec)
                .when()
                .header("If-Match", responseBefore_V1.getHeader("ETag").replace("\"", ""))
                .contentType(CONTENT_TYPE_JSON)
                .body(accountModifyDTO_V1)
                .put(BASE_URL + "/accounts")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "login", Matchers.equalTo("piotrnowak"),
                        "lastname", Matchers.equalTo("Bbbbbb")
                );

        // Modify account v2
        String responseModify_V2 = RestAssured.given()
                .spec(requestSpec)
                .when()
                .header("If-Match", responseBefore_V1.getHeader("ETag").replace("\"", ""))
                .contentType(CONTENT_TYPE_JSON)
                .body(accountModifyDTO_V1)
                .put(BASE_URL + "/accounts")
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .extract()
                .asString();

        assertEquals(I18n.OPTIMISTIC_LOCK_EXCEPTION, responseModify_V2);

        // Check after modifying
        RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(BASE_URL + String.format("/accounts/%s", userId))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "login", Matchers.equalTo("piotrnowak"),
                        "lastname", (Matchers.equalTo("Bbbbbb")
                        ));
    }

    @Test
    public void modifyUserAccountTestFailedConstraintViolation() throws IOException {
        String loginToken = this.login("jerzybem", "P@ssw0rd!", "pl");
        RequestSpecification requestSpec = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken);
        String userId = "02b0d9d7-a472-48d0-95e0-13a3e6a11d00";

        // Check before modifying
        Response responseBefore = RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(BASE_URL + String.format("/accounts/%s", userId))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "login", Matchers.equalTo("piotrnowak"),
                        "name", Matchers.any(String.class)
                )
                .extract()
                .response();

        AccountOutputDTO accountOutputDTO = responseBefore.as(AccountOutputDTO.class);
        String currentName = accountOutputDTO.getName();

        AccountModifyDTO accountModifyDTO = toAccountModifyDTO(accountOutputDTO);
        accountModifyDTO.setName("A".repeat(100));

        // Modify account
        RestAssured.given()
                .spec(requestSpec)
                .when()
                .header("If-Match", responseBefore.getHeader("ETag").replace("\"", ""))
                .contentType(CONTENT_TYPE_JSON)
                .body(accountModifyDTO)
                .put(BASE_URL + "/accounts")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(
                        "message", Matchers.equalTo("account.constraint.violation.exception"),
                        "violations", Matchers.hasSize(2),
                        "violations", Matchers.containsInAnyOrder(
                                Matchers.equalTo("bean.validation.account.first.name.too.long"),
                                Matchers.equalTo("bean.validation.account.first.name.regex.not.met")

                        )
                );

        // Check after modifying
        RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(BASE_URL + String.format("/accounts/%s", userId))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "login", Matchers.equalTo("piotrnowak"),
                        "name", Matchers.equalTo(currentName)
                );
    }

    /*----------------------------------------------------------------------------------------------------------------*/

    private String login(String login, String password, String language) throws JsonProcessingException {
        AccountLoginDTO accountLoginDTO = new AccountLoginDTO(login, password, language);

        RequestSpecification request = RestAssured.given();
        request.contentType(CONTENT_TYPE_JSON);
        request.body(mapper.writeValueAsString(accountLoginDTO));

        Response response = request.post(BASE_URL + "/auth/login-credentials");
        return response.asString();
    }

    private static Stream<Arguments> provideNoAdminLevelAccountsParameters() {
        return Stream.of(
                Arguments.of("tonyhalik"),          // tonyhalik staff
                Arguments.of("adamn")               // adamn client
        );
    }

    private static Stream<Arguments> provideAllLevelAccountsParameters() {
        return Stream.of(
                Arguments.of("tonyhalik"),          // tonyhalik staff
                Arguments.of("adamn"),              // adamn client
                Arguments.of("jerzybem")            // jerzybem admin
        );
    }

    private static Stream<Arguments> provideAllLevelAccountsParametersAndNotValidIfMatch() {
        return Stream.of(
                Arguments.of("tonyhalik", ""),      // tonyhalik staff
                Arguments.of("tonyhalik", "  "),    // tonyhalik staff
                Arguments.of("adamn", ""),          // adamn client
                Arguments.of("adamn", "  "),        // adamn client
                Arguments.of("jerzybem", ""),       // jerzybem admin
                Arguments.of("jerzybem", "  ")      // jerzybem admin
        );
    }

    private static Stream<Arguments> provideInvalidUUIDParameters() {
        return Stream.of(
                Arguments.of("  "),     // blank
                Arguments.of("db85e820-69a0-469c-bdb2-2fa38ae6e1c0bdb2"),   // too long
                Arguments.of("db85e820-69a0-469c-bdb2"),   // too short
                Arguments.of("db85e820-69a0-469c-bdb2-2fa38ae6e1X0")   // too invalid character
        );
    }

    private static AccountModifyDTO toAccountModifyDTO(AccountOutputDTO account) {
        return new AccountModifyDTO(
                account.getLogin(),
                account.getVersion(),
                account.getUserLevelsDto(),
                account.getName(),
                account.getLastname(),
                account.getPhoneNumber(),
                account.isTwoFactorAuth()
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
