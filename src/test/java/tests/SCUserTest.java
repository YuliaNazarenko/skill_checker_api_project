package tests;

import dto.CreateUsersRequest;
import helpers.AuthHelper;
import helpers.UsersDataHelper;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wrappers.UsersWrapper;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("SkillChecker User management API Tests")
public class SCUserTest extends TestBase {

    static String skillCheckerCookies = "";

    @BeforeAll
    public static void login() {
        skillCheckerCookies = AuthHelper.loginAndGetCookie();
    }

    @Test
    @DisplayName("Get all users")
    @Story("Get all users")
    void getAllUsers() {

        UsersWrapper.getAllUsers(skillCheckerCookies)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("config/schemas/user_schema.json"));

    }

    @Test
    @DisplayName("Reset user password by ID")
    @Story("Reset user password by ID (valid password)")
    void resetUserPasswordById() {

        CreateUsersRequest createUsersRequest = UsersDataHelper.generateUserPassword();
        UsersWrapper.resetUserPasswordById(createUsersRequest, skillCheckerCookies, 3)
                .then()
                .statusCode(200)
                .assertThat()
                .body("message", equalTo("Password reset successfully"));
    }

    @Test
    @DisplayName("Reset user password by ID")
    @Story("Reset user password by ID (short password)")
    void resetShortUserPasswordById() {

        CreateUsersRequest createUsersRequest = UsersDataHelper.generateUserShortPassword();
        UsersWrapper.resetUserPasswordById(createUsersRequest, skillCheckerCookies, 3)
                .then()
                .statusCode(400)
                .assertThat()
                .body("message", equalTo("Password must be at least 8 characters"));

    }

    @Test
    @DisplayName("Change password for current user")
    @Story("Change password for current user")
    void changePasswordForCurrentUser() {

        CreateUsersRequest createUsersRequest = UsersDataHelper.generateCurrentUserPassword();
        UsersWrapper.changeCurrentUserPassword(createUsersRequest, skillCheckerCookies)
                .then()
                .statusCode(400)
                .assertThat()
                .body("message", containsString("Current password is incorrect"));

    }
}
