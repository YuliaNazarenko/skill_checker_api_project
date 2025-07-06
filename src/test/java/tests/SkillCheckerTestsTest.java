package tests;

import dto.ConfigurationReader;
import dto.CreateTestRequest;
import dto.CreateTestResponse;
import dto.LoginRequest;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)


public class SkillCheckerTestsTest extends TestBase {

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
    @DisplayName("Login and get cookies")
    @Story("Login and get cookies")
    static void beforeAll() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(ConfigurationReader.get("email"));
        loginRequest.setPassword(ConfigurationReader.get("password"));

        skillCheckerCookies = given()
                .filter(new AllureRestAssured())
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
    @DisplayName("Get all tests")
    @Story("Get all tests")
    void getAllTests() {
        given()
                .filter(new AllureRestAssured())
                .cookie("connect.sid", skillCheckerCookies)
                .when()
                .get("/tests")
                .then()
                .statusCode(200)
                .time(lessThan(2000L))
                .body(matchesJsonSchemaInClasspath("test_schema.json"));

    }

    @Test
    @DisplayName("Create new test")
    @Story("Create a new test")
    @Order(1)
    void createNewTest() {
        CreateTestRequest createTestRequest = new CreateTestRequest();
        createTestRequest.setName(ConfigurationReader.get("name"));
        createTestRequest.setDescription(ConfigurationReader.get("description"));
        createTestRequest.setCategory(ConfigurationReader.get("category"));
        createTestRequest.setTimeLimit(15);
        createTestRequest.setPassingScore(20);

        CreateTestResponse createTestResponse = given()
                .filter(new AllureRestAssured())
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
    @DisplayName("Create new test with invalid data")
    @Story("Create a new test with invalid data")
    void createNewTestNegative() {

        CreateTestRequest createTestRequest = new CreateTestRequest();

        createTestRequest.setIntName(123456);
        createTestRequest.setDescription(ConfigurationReader.get("description"));
        createTestRequest.setCategory(ConfigurationReader.get("category"));
        createTestRequest.setTimeLimit(15);
        createTestRequest.setPassingScore(20);

        given()
                .filter(new AllureRestAssured())
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
    @DisplayName("Get test by ID")
    @Story("Get a test by its ID")
    @Order(2)
    void getTestById() {

        given()
                .filter(new AllureRestAssured())
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
    @DisplayName("Get test by invalid ID")
    @Story("Get test by invalid ID")
    void getTestByIdNegative() {

        given()
                .filter(new AllureRestAssured())
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
    @DisplayName("Update test by ID")
    @Story("Update test by ID")
    @Order(3)
    void updateTestById() {

        CreateTestRequest createTestRequest = new CreateTestRequest();
        createTestRequest.setName(ConfigurationReader.get("updatedName"));
        createTestRequest.setDescription(ConfigurationReader.get("updatedDescription"));
        createTestRequest.setTimeLimit(20);
        createTestRequest.setPassingScore(25);

        CreateTestResponse updateResponse = given()
                .filter(new AllureRestAssured())
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
    @DisplayName("Update test by ID with invalid data")
    @Story("Update test by ID with invalid data")
    @Order(4)
    void updateTestByInvalidData() {

        CreateTestRequest createTestRequest = new CreateTestRequest();
        createTestRequest.setIntName(123456);
        createTestRequest.setDescription(ConfigurationReader.get("updatedDescription"));
        createTestRequest.setTimeLimit(20);
        createTestRequest.setPassingScore(25);

        given()
                .filter(new AllureRestAssured())
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
    @DisplayName("Update test by invalid ID")
    @Story("Update test by invalid ID")
    @Order(5)
    void updateTestByInvalidId() {

        CreateTestRequest createTestRequest = new CreateTestRequest();
        createTestRequest.setName(ConfigurationReader.get("updatedName"));
        createTestRequest.setDescription(ConfigurationReader.get("updatedDescription"));
        createTestRequest.setTimeLimit(20);
        createTestRequest.setPassingScore(25);

        given()
                .filter(new AllureRestAssured())
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
    @DisplayName("Update test by invalid ID with 500 error")
    @Story("Update test by invalid ID with 500 error")
    @Order(6)
    void updateTestByInvalidIdWith500Error() {
        CreateTestRequest createTestRequest = new CreateTestRequest();
        createTestRequest.setName(ConfigurationReader.get("updatedName"));
        createTestRequest.setDescription(ConfigurationReader.get("updatedDescription"));
        createTestRequest.setTimeLimit(20);
        createTestRequest.setPassingScore(25);

        given()
                .filter(new AllureRestAssured())
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
    @DisplayName("Delete test by ID")
    @Story("Delete test by ID")
    @Order(7)
    void deleteTestById() {

        given()
                .filter(new AllureRestAssured())
                .cookie("connect.sid", skillCheckerCookies)
                .spec(requestSpecification)
                .pathParam("savedId", savedId)
                .when()
                .delete("/tests/{savedId}")
                .then()
                .statusCode(204);

        given()
                .filter(new AllureRestAssured())
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
    @DisplayName("Delete test by invalid ID")
    @Story("Delete test by invalid ID")
    @Disabled
    void deleteTestByInvalidId() {

        given()
                .filter(new AllureRestAssured())
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
    @DisplayName("Delete test by invalid ID with 500 error")
    @Story("Delete test by invalid ID with 500 error")
    @Disabled
    void deleteTestByInvalidIdWith500Error() {

        given()
                .filter(new AllureRestAssured())
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


