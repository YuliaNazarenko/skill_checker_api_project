package tests;

import dto.ConfigurationReader;
import dto.CreateTestRequest;
import dto.CreateTestResponse;
import dto.LoginRequest;
import helpers.TestDataHelper;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import wrappers.TestsApi;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static specs.skillCheckSpecs.responseSpecification;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)


public class SkillCheckerTestsTest extends TestBase {

    static int savedId;
    static String skillCheckerCookies = "";

    @BeforeAll
    @DisplayName("Login and get cookies")
    @Story("Login and get cookies")
    public static void login() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(ConfigurationReader.get("email"));
        loginRequest.setPassword(ConfigurationReader.get("password"));

        skillCheckerCookies = given()
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
        TestsApi.getAllTests(skillCheckerCookies)
                .then()
                .statusCode(200)
                .time(lessThan(2000L))
                .body(matchesJsonSchemaInClasspath("config/schemas/test_schema.json"));
    }

    @Test
    @DisplayName("Create new test")
    @Story("Create a new test")
    @Order(1)
    void createNewTest() {

        CreateTestRequest createTestRequest = TestDataHelper.generateValidTest();
        CreateTestResponse createTestResponse =
                TestsApi.createNewTest(createTestRequest, skillCheckerCookies)
                        .then()
                        .statusCode(201)
                        .extract()
                        .as(CreateTestResponse.class);

        Assertions.assertEquals(createTestRequest.getName(), createTestResponse.getName());
        Assertions.assertEquals(createTestRequest.getDescription(), createTestResponse.getDescription());
        Assertions.assertEquals(createTestRequest.getTimeLimit(), createTestResponse.getTimeLimit());
        Assertions.assertEquals(createTestRequest.getPassingScore(), createTestResponse.getPassingScore());

        savedId = Integer.parseInt(createTestResponse.getId());
    }

    @Test
    @DisplayName("Create new test with invalid data")
    @Story("Create a new test with invalid data")
    void createNewTestNegative() {

        CreateTestRequest createTestRequest = TestDataHelper.generateInvalidTest();

        TestsApi.createNewTest(createTestRequest, skillCheckerCookies)
                .then()
                .statusCode(400)
                .body(containsString("Test name must not be empty or whitespace only"));
    }

    @Test
    @DisplayName("Get test by ID")
    @Story("Get a test by its ID")
    @Order(2)
    void getTestById() {
        TestsApi.getTestById(savedId, skillCheckerCookies)
                .then()
                .statusCode(200)
                .spec(responseSpecification)
                .body("id", equalTo(savedId))
                .body(matchesJsonSchemaInClasspath("config/schemas/test_schema_1.json"));
    }

    @Test
    @DisplayName("Get test by invalid ID")
    @Story("Get test by invalid ID")
    void getTestByIdNegative() {
        TestsApi.getTestById(999999, skillCheckerCookies)
                .then()
                .statusCode(404)
                .body(containsString("Test not found"));
    }

    @Test
    @DisplayName("Update test by ID")
    @Story("Update test by ID")
    @Order(3)
    void updateTestById() {

        CreateTestRequest createTestRequest = TestDataHelper.generateUpdatedTest(savedId);

        CreateTestResponse updateResponse =
                TestsApi.updateTest(savedId, createTestRequest, skillCheckerCookies)
                        .then()
                        .statusCode(200)
                        .body(matchesJsonSchemaInClasspath("config/schemas/test_schema_1.json"))
                        .spec(responseSpecification)
                        .extract()
                        .as(CreateTestResponse.class);

        Assertions.assertEquals(createTestRequest.getName(), updateResponse.getName());
        Assertions.assertEquals(createTestRequest.getDescription(), updateResponse.getDescription());
        Assertions.assertEquals(createTestRequest.getTimeLimit(), updateResponse.getTimeLimit());
        Assertions.assertEquals(createTestRequest.getPassingScore(), updateResponse.getPassingScore());
    }

    @Test
    @DisplayName("Update test by ID with invalid data")
    @Story("Update test by ID with invalid data")
    @Order(4)
    void updateTestByInvalidData() {

        CreateTestRequest createTestRequest = TestDataHelper.generateUpdatedTestWithInvalidData(savedId);
        TestsApi.updateTest(savedId, createTestRequest, skillCheckerCookies)
                .then()
                .statusCode(400)
                .body(containsString("Invalid test data"));
    }

    @Test
    @DisplayName("Update test by invalid ID")
    @Story("Update test by invalid ID")
    @Order(5)
    void updateTestByInvalidId() {

        CreateTestRequest createTestRequest = TestDataHelper.generateUpdatedTest(999999);

        TestsApi
                .updateTest(999999, createTestRequest, skillCheckerCookies)
                .then()
                .statusCode(404)
                .body(containsString("Test not found"));
    }

    @Test
    @DisplayName("Update test by invalid ID with 500 error")
    @Story("Update test by invalid ID with 500 error")
    @Order(6)
    void updateTestByInvalidIdWith500Error() {
        CreateTestRequest createTestRequest = TestDataHelper.generateUpdatedTest(112223445566L);

        TestsApi.updateTest(112223445566L, createTestRequest, skillCheckerCookies)
                .then()
                .statusCode(500)
                .body(containsString("Failed to update test"));
    }

    @Test
    @DisplayName("Delete test by ID")
    @Story("Delete test by ID")
    @Order(7)
    void deleteTestById() {
        TestsApi
                .deleteTest(savedId, skillCheckerCookies)
                .then()
                .statusCode(204);
        TestsApi
                .getTestById(savedId, skillCheckerCookies)
                .then()
                .statusCode(404)
                .body(containsString("Test not found"));
    }

    @Test
    @DisplayName("Delete test by invalid ID")
    @Story("Delete test by invalid ID")
    void deleteTestByInvalidId() {

        TestsApi
                .getTestById(99999, skillCheckerCookies)
                .then()
                .statusCode(404)
                .body(containsString("Test not found"));
    }
}


