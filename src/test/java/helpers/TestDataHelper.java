package helpers;

import dto.ConfigurationReader;
import dto.CreateTestRequest;

import java.util.UUID;

public class TestDataHelper {
    public static CreateTestRequest generateValidTest() {
        CreateTestRequest createTestRequest = new CreateTestRequest();
        createTestRequest.setName("Test " + UUID.randomUUID());
        createTestRequest.setDescription(ConfigurationReader.get("description"));
        createTestRequest.setCategory(ConfigurationReader.get("category"));
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
        createTestRequest.setName("Updated Test " + UUID.randomUUID());
        createTestRequest.setDescription(ConfigurationReader.get("updatedDescription"));
        createTestRequest.setTimeLimit(20);
        createTestRequest.setPassingScore(30);
        return createTestRequest;
    }

    public static CreateTestRequest generateUpdatedTestWithInvalidData(int id) {
        CreateTestRequest createTestRequest = new CreateTestRequest();
        createTestRequest.setIntName(12345);
        createTestRequest.setDescription(ConfigurationReader.get("updatedDescription"));
        createTestRequest.setTimeLimit(20);
        createTestRequest.setPassingScore(30);
        return createTestRequest;
    }
}

