package tests;

import dto.ConfigurationReader;
import dto.CreateUserRequest;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class CreateUserTest extends TestBase {

    ResponseSpecification responseSpecification = expect().statusCode(201)
            .contentType(ContentType.JSON)
            .body("id", notNullValue())
            .body("organizationId", notNullValue())
            .body("name", notNullValue())
            .body("description", notNullValue())
            .body("createdBy", notNullValue())
            .body("timeLimit", notNullValue())
            .body("isActive", notNullValue())
            .body("passingScore", notNullValue());

    static String skillCheckerCookies = "";

    @BeforeAll
    static void beforeAll() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail(ConfigurationReader.get("email"));
        createUserRequest.setPassword(ConfigurationReader.get("password"));

        skillCheckerCookies = given().spec(requestSpecification)
                .contentType(ContentType.JSON)
                .body(createUserRequest)
                .when()
                .post("/login")
                .then()
                .log().all()
                .extract()
                .cookie("connect.sid");

        assertNotNull(skillCheckerCookies, "Login failed: connect.sid cookie is null");
    }

    @Test
    void getAllTests() {
        given()
                .cookie("connect.sid", skillCheckerCookies)
                .when()
                .get("/tests")
                .then()
                .statusCode(200)
                .time(lessThan(2000L))
                .body(matchesJsonSchemaInClasspath("test_schema.json"));
    }

}
