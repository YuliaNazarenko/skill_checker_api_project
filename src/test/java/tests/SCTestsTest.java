package tests;

import dto.CreateTestRequest;
import dto.CreateTestResponse;
import helpers.AuthHelper;
import helpers.TestDataHelper;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import wrappers.TestsWrapper;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.SkillCheckSpecs.responseSpecification;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

@DisplayName("SkillChecker Test management API Tests")
public class SCTestsTest extends TestBase {

    static int savedId;
    static String skillCheckerCookies = "";

    @BeforeAll
    public static void login() {
        skillCheckerCookies = AuthHelper.loginAndGetCookie();
    }


    @Test
    @DisplayName("Get all tests")
    @Story("Get all tests")
    void getAllTests() {
        TestsWrapper.getAllTests(skillCheckerCookies)
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
                TestsWrapper.createNewTest(createTestRequest, skillCheckerCookies)
                        .then()
                        .statusCode(201)
                        .extract()
                        .as(CreateTestResponse.class);

        assertEquals(createTestRequest.getName(), createTestResponse.getName());
        assertEquals(createTestRequest.getDescription(), createTestResponse.getDescription());
        assertEquals(createTestRequest.getTimeLimit(), createTestResponse.getTimeLimit());
        assertEquals(createTestRequest.getPassingScore(), createTestResponse.getPassingScore());

        savedId = Integer.parseInt(createTestResponse.getId());
    }

    @Test
    @DisplayName("Create new test with invalid data")
    @Story("Create a new test with invalid data")
    void createNewTestNegative() {

        CreateTestRequest createTestRequest = TestDataHelper.generateInvalidTest();

        TestsWrapper.createNewTest(createTestRequest, skillCheckerCookies)
                .then()
                .statusCode(400)
                .body(containsString("Test name must not be empty or whitespace only"));
    }

    @Test
    @DisplayName("Get test by ID")
    @Story("Get a test by its ID")
    @Order(2)
    void getTestById() {
        TestsWrapper.getTestById(savedId, skillCheckerCookies)
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
        TestsWrapper.getTestById(999999, skillCheckerCookies)
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
                TestsWrapper.updateTest(savedId, createTestRequest, skillCheckerCookies)
                        .then()
                        .statusCode(200)
                        .body(matchesJsonSchemaInClasspath("config/schemas/test_schema_1.json"))
                        .spec(responseSpecification)
                        .extract()
                        .as(CreateTestResponse.class);

        assertEquals(createTestRequest.getName(), updateResponse.getName());
        assertEquals(createTestRequest.getDescription(), updateResponse.getDescription());
        assertEquals(createTestRequest.getTimeLimit(), updateResponse.getTimeLimit());
        assertEquals(createTestRequest.getPassingScore(), updateResponse.getPassingScore());
    }

    @Test
    @DisplayName("Update test by ID with invalid data")
    @Story("Update test by ID with invalid data")
    @Order(4)
    void updateTestByInvalidData() {

        CreateTestRequest createTestRequest = TestDataHelper.generateUpdatedTestWithInvalidData(savedId);
        TestsWrapper.updateTest(savedId, createTestRequest, skillCheckerCookies)
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

        TestsWrapper
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

        TestsWrapper.updateTest(112223445566L, createTestRequest, skillCheckerCookies)
                .then()
                .statusCode(500)
                .body(containsString("Failed to update test"));
    }

    @Test
    @DisplayName("Delete test by ID")
    @Story("Delete test by ID")
    @Order(7)
    void deleteTestById() {
        TestsWrapper
                .deleteTest(savedId, skillCheckerCookies)
                .then()
                .statusCode(204);
        TestsWrapper
                .getTestById(savedId, skillCheckerCookies)
                .then()
                .statusCode(404)
                .body(containsString("Test not found"));
    }

    @Test
    @DisplayName("Delete test by invalid ID")
    @Story("Delete test by invalid ID")
    void deleteTestByInvalidId() {

        TestsWrapper
                .getTestById(99999, skillCheckerCookies)
                .then()
                .statusCode(404)
                .body(containsString("Test not found"));
    }
}


