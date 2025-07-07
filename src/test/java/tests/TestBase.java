package tests;

import dto.ConfigurationReader;
import dto.LoginRequest;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static specs.skillCheckSpecs.defaultSpec;

public class TestBase {
    static String url = "https://skillchecker.tech";

    @BeforeAll
    public static void setup() {

        RestAssured.baseURI = url;
        RestAssured.basePath = "api";
    }


    }

