package helpers;

import dto.LoginRequest;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class AuthHelper {

    public static LoginRequest generateAuthLoginRequest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(ConfigurationReader.get("email"));
        loginRequest.setPassword(ConfigurationReader.get("password"));
        return loginRequest;
    }


    public static String loginAndGetCookie() {
        LoginRequest loginRequest = generateAuthLoginRequest();

        return given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .extract()
                .cookie("connect.sid");
    }
}
