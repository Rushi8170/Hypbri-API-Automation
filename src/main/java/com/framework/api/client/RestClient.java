package com.framework.api.client;

import com.framework.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * RestClient — base wrapper over REST Assured for all API tests.
 *
 * Provides pre-configured request/response specs.
 * All API endpoint classes use this client.
 */
public class RestClient {

    private static final Logger log = LogManager.getLogger(RestClient.class);
    private static final ConfigManager config = ConfigManager.getInstance();

    private static RequestSpecification requestSpec;
    private static ResponseSpecification responseSpec;

    static {
        RestAssured.baseURI = config.get("api.base.url");

        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();

        responseSpec = new ResponseSpecBuilder()
                .build();
    }

    protected RequestSpecification getRequestSpec() {
        return RestAssured.given().spec(requestSpec);
    }

    protected RequestSpecification getRequestSpec(Map<String, String> headers) {
        return RestAssured.given().spec(requestSpec).headers(headers);
    }

    protected RequestSpecification getAuthRequestSpec(String token) {
        return RestAssured.given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + token);
    }

    // ── HTTP Methods ─────────────────────────────────────────────

    public Response get(String endpoint) {
        log.info("GET {}{}", config.get("api.base.url"), endpoint);
        return getRequestSpec().when().get(endpoint).then().extract().response();
    }

    public Response get(String endpoint, Map<String, String> queryParams) {
        log.info("GET {}{} params={}", config.get("api.base.url"), endpoint, queryParams);
        return getRequestSpec().queryParams(queryParams).when().get(endpoint).then().extract().response();
    }

    public Response get(String endpoint, String token) {
        log.info("GET (auth) {}{}", config.get("api.base.url"), endpoint);
        return getAuthRequestSpec(token).when().get(endpoint).then().extract().response();
    }

    public Response post(String endpoint, Object body) {
        log.info("POST {}{}", config.get("api.base.url"), endpoint);
        return getRequestSpec().body(body).when().post(endpoint).then().extract().response();
    }

    public Response post(String endpoint, Object body, String token) {
        log.info("POST (auth) {}{}", config.get("api.base.url"), endpoint);
        return getAuthRequestSpec(token).body(body).when().post(endpoint).then().extract().response();
    }

    public Response put(String endpoint, Object body) {
        log.info("PUT {}{}", config.get("api.base.url"), endpoint);
        return getRequestSpec().body(body).when().put(endpoint).then().extract().response();
    }

    public Response put(String endpoint, Object body, String token) {
        log.info("PUT (auth) {}{}", config.get("api.base.url"), endpoint);
        return getAuthRequestSpec(token).body(body).when().put(endpoint).then().extract().response();
    }

    public Response patch(String endpoint, Object body) {
        log.info("PATCH {}{}", config.get("api.base.url"), endpoint);
        return getRequestSpec().body(body).when().patch(endpoint).then().extract().response();
    }

    public Response delete(String endpoint) {
        log.info("DELETE {}{}", config.get("api.base.url"), endpoint);
        return getRequestSpec().when().delete(endpoint).then().extract().response();
    }

    public Response delete(String endpoint, String token) {
        log.info("DELETE (auth) {}{}", config.get("api.base.url"), endpoint);
        return getAuthRequestSpec(token).when().delete(endpoint).then().extract().response();
    }
}
