package tests;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

public class TestBase {
    static String url = "https://skillchecker.tech";

    @BeforeAll
    public static void setup(){

        RestAssured.baseURI =url;
        RestAssured.basePath = "api";
        RestAssured.requestSpecification = given().accept(ContentType.ANY);
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }
}
