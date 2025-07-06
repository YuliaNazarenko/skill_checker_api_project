package tests;

import dto.ConfigurationReader;
import dto.CreateTestRequest;
import dto.CreateTestResponse;
import dto.LoginRequest;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)


public class CreateUserTest extends TestBase {

    ResponseSpecification responseSpecification = expect()
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
    static int savedId;

    @BeforeAll
    static void beforeAll() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(ConfigurationReader.get("email"));
        loginRequest.setPassword(ConfigurationReader.get("password"));

        skillCheckerCookies = given()
                .spec(requestSpecification)
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
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

    @Test
    @Order(1)
    void  createNewTest () {
        CreateTestRequest createTestRequest= new CreateTestRequest();
        createTestRequest.setName(ConfigurationReader.get("name"));
        createTestRequest.setDescription(ConfigurationReader.get("description"));
        createTestRequest.setCategory(ConfigurationReader.get("category"));
        createTestRequest.setTimeLimit(15);
        createTestRequest.setPassingScore(20);

        CreateTestResponse createTestResponse = given()
                .cookie("connect.sid", skillCheckerCookies)
                .spec(requestSpecification)
                .contentType(ContentType.JSON)
                .body(createTestRequest)
                .when()
                .post("/tests")
                .then()
                .statusCode(201)
                .spec(responseSpecification)
                .extract()
                .as(CreateTestResponse.class);

        Assertions.assertEquals(ConfigurationReader.get("name"), createTestResponse.getName());
        Assertions.assertEquals(ConfigurationReader.get("description"), createTestResponse.getDescription());
        
        savedId = Integer.parseInt(createTestResponse.getId());

    }

    @Test
    void  createNewTestNegative () {

        CreateTestRequest createTestRequest= new CreateTestRequest();

        createTestRequest.setIntName(123456);
        createTestRequest.setDescription(ConfigurationReader.get("description"));
        createTestRequest.setCategory(ConfigurationReader.get("category"));
        createTestRequest.setTimeLimit(15);
        createTestRequest.setPassingScore(20);

       given()
                .cookie("connect.sid", skillCheckerCookies)
                .spec(requestSpecification)
                .contentType(ContentType.JSON)
                .body(createTestRequest)
                .when()
                .post("/tests")
                .then()
                .statusCode(400)
                .body(containsString("Invalid test data"));

    }

    @Test
    @Order(2)
    void getTestById() {

        given()
                .cookie("connect.sid", skillCheckerCookies)
                .spec(requestSpecification)
                .pathParam("savedId", savedId)
                .when()
                .get("/tests/{savedId}")
                .then()
                .statusCode(200)
                .spec(responseSpecification)
                .body("id", equalTo(savedId));

    }

    @Test
    void getTestByIdNegative() {

        given()
                .cookie("connect.sid", skillCheckerCookies)
                .spec(requestSpecification)
                .pathParam("invalidId", 999999)
                .when()
                .get("/tests/{invalidId}")
                .then()
                .statusCode(404)
                .body(containsString("Test not found"));

    }

    @Test
    @Order(3)
    void updateTestById() {

        CreateTestRequest createTestRequest= new CreateTestRequest();
        createTestRequest.setName(ConfigurationReader.get("updatedName"));
        createTestRequest.setDescription(ConfigurationReader.get("updatedDescription"));
        createTestRequest.setTimeLimit(20);
        createTestRequest.setPassingScore(25);

        CreateTestResponse updateResponse = given()
                .cookie("connect.sid", skillCheckerCookies)
                .spec(requestSpecification)
                .pathParam("savedId", savedId)
                .contentType(ContentType.JSON)
                .body(createTestRequest)
                .when()
                .patch("/tests/{savedId}")
                .then()
                .statusCode(200)
                .spec(responseSpecification)
                .extract()
                .as(CreateTestResponse.class);

        Assertions.assertEquals(ConfigurationReader.get("updatedName"), updateResponse.getName());
        Assertions.assertEquals(ConfigurationReader.get("updatedDescription"), updateResponse.getDescription());
    }

    @Test
    @Order(4)
    void updateTestByInvalidData() {

        CreateTestRequest createTestRequest= new CreateTestRequest();
        createTestRequest.setIntName(123456);
        createTestRequest.setDescription(ConfigurationReader.get("updatedDescription"));
        createTestRequest.setTimeLimit(20);
        createTestRequest.setPassingScore(25);

        given()
                .cookie("connect.sid", skillCheckerCookies)
                .spec(requestSpecification)
                .pathParam("savedId", savedId)
                .contentType(ContentType.JSON)
                .body(createTestRequest)
                .when()
                .patch("/tests/{savedId}")
                .then()
                .statusCode(400)
                .body(containsString("Invalid test data"));
    }

    @Test
    @Order(5)
    void updateTestByInvalidId() {

        CreateTestRequest createTestRequest= new CreateTestRequest();
        createTestRequest.setName(ConfigurationReader.get("updatedName"));
        createTestRequest.setDescription(ConfigurationReader.get("updatedDescription"));
        createTestRequest.setTimeLimit(20);
        createTestRequest.setPassingScore(25);

        given()
                .cookie("connect.sid", skillCheckerCookies)
                .spec(requestSpecification)
                .pathParam("invalidId", 999999)
                .contentType(ContentType.JSON)
                .body(createTestRequest)
                .when()
                .patch("/tests/{invalidId}")
                .then()
                .statusCode(404)
                .body(containsString("Test not found"));
    }

    @Test
    @Order(6)
    void updateTestByInvalidIdWith500Error() {
        CreateTestRequest createTestRequest= new CreateTestRequest();
        createTestRequest.setName(ConfigurationReader.get("updatedName"));
        createTestRequest.setDescription(ConfigurationReader.get("updatedDescription"));
        createTestRequest.setTimeLimit(20);
        createTestRequest.setPassingScore(25);

        given()
                .cookie("connect.sid", skillCheckerCookies)
                .spec(requestSpecification)
                .pathParam("invalidId", 99999999999L)
                .contentType(ContentType.JSON)
                .body(createTestRequest)
                .when()
                .patch("/tests/{invalidId}")
                .then()
                .statusCode(500)
                .body(containsString("Failed to update test"));
    }

    @Test
    @Order(7)
    void deleteTestById() {

        given()
                .cookie("connect.sid", skillCheckerCookies)
                .spec(requestSpecification)
                .pathParam("savedId", savedId)
                .when()
                .delete("/tests/{savedId}")
                .then()
                .statusCode(204);

        given()
                .cookie("connect.sid", skillCheckerCookies)
                .spec(requestSpecification)
                .pathParam("savedId", savedId)
                .when()
                .get("/tests/{savedId}")
                .then()
                .statusCode(404)
                .body(containsString("Test not found"));
    }

    @Test
    void deleteTestByInvalidId() {

        given()
                .cookie("connect.sid", skillCheckerCookies)
                .spec(requestSpecification)
                .pathParam("invalidId", 999999)
                .when()
                .delete("/tests/{invalidId}")
                .then()
                .statusCode(404)
                .body(containsString("Test not found"));
    }

    @Test
    void deleteTestByInvalidIdWith500Error() {

        given()
                .cookie("connect.sid", skillCheckerCookies)
                .spec(requestSpecification)
                .pathParam("invalidId", 99999999999L)
                .when()
                .delete("/tests/{invalidId}")
                .then()
                .statusCode(500)
                .body(containsString("Failed to delete test"));
    }
}


