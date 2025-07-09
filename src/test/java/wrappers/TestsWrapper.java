package wrappers;

import dto.CreateTestRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static specs.SkillCheckSpecs.defaultSpec;

public class TestsWrapper {
    private static final String BASE_PATH = "/tests";

    public static Response getAllTests(String cookie) {
        return given(defaultSpec)
                .cookie("connect.sid", cookie)
                .when()
                .get(BASE_PATH);
    }


    public static Response createNewTest(CreateTestRequest body, String cookie) {
        return given(defaultSpec)
                .cookie("connect.sid", cookie)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(BASE_PATH);
    }

    public static Response createNewTestWithInvalidData(String cookie) {
        String body = """
                {
                  "name": 123456,
                  "description": "Invalid test",
                  "category": "Some Category",
                  "timeLimit": 150000,
                  "passingScore": 200000
                }
                """;

        return given(defaultSpec)
                .cookie("connect.sid", cookie)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(BASE_PATH);
    }

    public static Response getTestById(int id, String cookie) {
        return given(defaultSpec)
                .cookie("connect.sid", cookie)
                .pathParam("id", id)
                .when()
                .get(BASE_PATH + "/{id}");
    }

    public static Response updateTest(long id, CreateTestRequest body, String cookie) {
        return given(defaultSpec)
                .cookie("connect.sid", cookie)
                .pathParam("id", id)
                .contentType("application/json")
                .body(body)
                .when()
                .patch(BASE_PATH + "/{id}");
    }

    public static Response updateTestWithInvalidData(long id, String cookie) {
        String body = """
                {
                  "name": 123456,
                  "description": "Invalid test",
                  "timeLimit": 150000,
                  "passingScore": 200000
                }
                """;
        return given(defaultSpec)
                .cookie("connect.sid", cookie)
                .pathParam("id", id)
                .contentType("application/json")
                .body(body)
                .when()
                .patch(BASE_PATH + "/{id}");
    }

    public static Response deleteTest(int id, String cookie) {
        return given(defaultSpec)
                .cookie("connect.sid", cookie)
                .pathParam("id", id)
                .when()
                .delete(BASE_PATH + "/{id}");
    }
}
