package wrappers;

import dto.CreateUsersRequest;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static specs.SkillCheckSpecs.defaultSpec;

public class UsersWrapper {
    private static final String BASE_PATH = "/users";

    public static Response getAllUsers(String cookie) {
        return given(defaultSpec)
                .cookie("connect.sid", cookie)
                .when()
                .get(BASE_PATH);
    }

    public static Response resetUserPasswordById(CreateUsersRequest body, String cookie, int userId) {
        return given(defaultSpec)
                .cookie("connect.sid", cookie)
                .pathParam("id", userId)
                .body(body)
                .when()
                .post(BASE_PATH + "/{id}/reset-password");
    }

    public static Response changeCurrentUserPassword(CreateUsersRequest body, String cookie) {
        return given(defaultSpec)
                .cookie("connect.sid", cookie)
                .body(body)
                .when()
                .post("/change-password");
    }
}
