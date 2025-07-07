package specs;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.notNullValue;

public class skillCheckSpecs {
    public static RequestSpecification defaultSpec = with()
            .filter(withCustomTemplates())
            .contentType(ContentType.JSON)
            .log()
            .all();

    public static ResponseSpecification responseSpecification = expect()
            .contentType(ContentType.JSON)
            .body("id", notNullValue())
            .body("organizationId", notNullValue())
            .body("name", notNullValue())
            .body("description", notNullValue())
            .body("createdBy", notNullValue())
            .body("timeLimit", notNullValue())
            .body("isActive", notNullValue())
            .body("passingScore", notNullValue());
}
