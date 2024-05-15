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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.TestcontainersConfigFull;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountLoginDTO;

import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public void getSelfInfoABoutAccountSuccessfulTest() throws JsonProcessingException {
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
                .body("email", Matchers.equalTo("testst@facais.com"))
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
    public void getInfoABoutAccountSuccessfulTest() throws JsonProcessingException {
        String loginToken = login("jerzybem", "P@ssw0rd!", "pl");
        List<String> list = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .get(BASE_URL + "/accounts/d20f860d-555a-479e-8783-67aee5b66692")
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
                .get(BASE_URL + "/accounts/d20f860d-005a-479e-8783-67aee5b86692")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());

    }

    @Test
    public void getInfoABoutAccountBadRequestTest() throws JsonProcessingException {
        String loginToken = login("jerzybem", "P@ssw0rd!", "pl");
        RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .get(BASE_URL + "/accounts/ssbdTest")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());

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
