package tests;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;


public class CreateUserTest extends TestBase {

    RequestSpecification requestSpecification = given().contentType(ContentType.JSON);

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

        skillCheckerCookies = given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "email": "admin@skillchecker.tech",
                          "password": "admin123"
                        }
                        """)
                .when()
                .post("/login")
                .then()
                .extract()
                .cookie("connect.sid");
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
