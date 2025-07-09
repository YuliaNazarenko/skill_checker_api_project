package helpers;

import com.github.javafaker.Faker;
import dto.CreateUsersRequest;

public class UsersDataHelper {
    static Faker faker = new Faker();

    public static CreateUsersRequest generateUserPassword() {
        CreateUsersRequest createUserRequest = new CreateUsersRequest();
        createUserRequest.setNewPassword(faker.internet().password(8, 16));
        return createUserRequest;
    }

    public static CreateUsersRequest generateUserShortPassword() {
        CreateUsersRequest createUserRequest = new CreateUsersRequest();
        createUserRequest.setNewPassword(faker.internet().password(6, 7));
        return createUserRequest;
    }

    public static CreateUsersRequest generateCurrentUserPassword() {
        CreateUsersRequest createUserRequest = new CreateUsersRequest();
        createUserRequest.setCurrentPassword(faker.internet().password(8, 16));
        createUserRequest.setNewPassword(faker.internet().password(8, 16));
        createUserRequest.setOldPassword(faker.internet().password(8, 16));
        return createUserRequest;
    }


}
