package pl.lodz.p.it.ssbd2024.ssbd03.integration.app;

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
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.TestcontainersConfigFull;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountLoginDTO;

import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


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

        Response response = request.post("http://localhost:8181/api/v1/auth/login-credentials");

        assertEquals(200, response.getStatusCode());
        assertEquals("jerzybem", decodeJwtTokenAndExtractValue(response.asString(), "sub"));
    }

    @Test
    public void getSelfTest() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        AccountLoginDTO accountLoginDTO = new AccountLoginDTO("jerzybem", "P@ssw0rd!", "pl");

        RequestSpecification request = RestAssured.given();
        request.contentType(CONTENT_TYPE);
        request.body(mapper.writeValueAsString(accountLoginDTO));

        Response response = request.post("http://localhost:8181/api/v1/auth/login-credentials");

        assertEquals(200, response.getStatusCode());
        assertEquals("jerzybem", decodeJwtTokenAndExtractValue(response.asString(), "sub"));

        request = RestAssured.given();
        request.contentType(CONTENT_TYPE);

        response = request.header("Authorization", "Bearer " + response.asString()).get("http://localhost:8181/api/v1/accounts/self");

        assertEquals(200, response.getStatusCode());
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

        //RequestSpecification request = RestAssured.given();
        //request.contentType(CONTENT_TYPE);
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
        assertEquals(list2.get(2), "jerzybem");
        assertEquals(list2.get(3), "kamilslimak");
        assertEquals(list2.get(4), "michalkowal");
        assertEquals(list2.get(5), "piotrnowak");
        assertEquals(list2.get(6), "tonyhalik");


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
