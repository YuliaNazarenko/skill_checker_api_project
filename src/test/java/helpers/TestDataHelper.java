package helpers;

import com.github.javafaker.Faker;
import dto.CreateTestRequest;

public class TestDataHelper {

    static Faker faker = new Faker();

    public static CreateTestRequest generateValidTest() {
        CreateTestRequest createTestRequest = new CreateTestRequest();
        createTestRequest.setName(faker.name().fullName());
        createTestRequest.setDescription(faker.lorem().sentence());
        createTestRequest.setCategory(faker.book().genre());
        createTestRequest.setTimeLimit(15);
        createTestRequest.setPassingScore(20);
        return createTestRequest;
    }

    public static CreateTestRequest generateInvalidTest() {
        CreateTestRequest createTestRequest = new CreateTestRequest();
        createTestRequest.setIntName(123456);
        createTestRequest.setDescription("Invalid test");
        createTestRequest.setCategory("Some Category");
        createTestRequest.setTimeLimit(150000);
        createTestRequest.setPassingScore(200000);
        return createTestRequest;
    }

    public static CreateTestRequest generateUpdatedTest(long id) {
        CreateTestRequest createTestRequest = new CreateTestRequest();
        createTestRequest.setName(faker.name().fullName());
        createTestRequest.setDescription(faker.lorem().sentence());
        createTestRequest.setTimeLimit(faker.number().numberBetween(10, 50));
        createTestRequest.setPassingScore(faker.number().numberBetween(10, 100));
        return createTestRequest;
    }

    public static CreateTestRequest generateUpdatedTestWithInvalidData(int id) {
        CreateTestRequest createTestRequest = new CreateTestRequest();
        createTestRequest.setIntName(12345);
        createTestRequest.setDescription("updatedDescription");
        createTestRequest.setTimeLimit(20);
        createTestRequest.setPassingScore(30);
        return createTestRequest;
    }
}

