package com.framework.api.validators;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

/**
 * ResponseValidator — reusable assertions for REST Assured responses.
 *
 * Usage:
 *   ResponseValidator.assertStatusCode(response, 200);
 *   ResponseValidator.assertBodyContains(response, "name", "John");
 */
public final class ResponseValidator {

    private static final Logger log = LogManager.getLogger(ResponseValidator.class);

    private ResponseValidator() {}

    public static void assertStatusCode(Response response, int expectedCode) {
        int actual = response.getStatusCode();
        log.info("Validating status code: expected={}, actual={}", expectedCode, actual);
        Assert.assertEquals(actual, expectedCode,
                "Status code mismatch. Body: " + response.getBody().asString());
    }

    public static void assertBodyContainsKey(Response response, String jsonPath) {
        Object value = response.jsonPath().get(jsonPath);
        Assert.assertNotNull(value, "JSON path '" + jsonPath + "' not found in response");
        log.info("Key '{}' found with value: {}", jsonPath, value);
    }

    public static void assertFieldValue(Response response, String jsonPath, Object expectedValue) {
        Object actual = response.jsonPath().get(jsonPath);
        log.info("Validating '{}': expected='{}', actual='{}'", jsonPath, expectedValue, actual);
        Assert.assertEquals(String.valueOf(actual), String.valueOf(expectedValue),
                "Field '" + jsonPath + "' value mismatch");
    }

    public static void assertResponseTimeBelow(Response response, long maxMillis) {
        long actual = response.getTime();
        log.info("Response time: {}ms (max allowed: {}ms)", actual, maxMillis);
        Assert.assertTrue(actual <= maxMillis,
                "Response time " + actual + "ms exceeded limit of " + maxMillis + "ms");
    }

    public static void assertContentType(Response response, String expectedContentType) {
        String actual = response.getContentType();
        Assert.assertTrue(actual.contains(expectedContentType),
                "Content-Type mismatch. Expected: " + expectedContentType + " but got: " + actual);
    }

    public static void assertBodyNotEmpty(Response response) {
        String body = response.getBody().asString();
        Assert.assertNotNull(body, "Response body is null");
        Assert.assertFalse(body.trim().isEmpty(), "Response body is empty");
    }

    public static <T> T extractField(Response response, String jsonPath, Class<T> clazz) {
        T value = response.jsonPath().getObject(jsonPath, clazz);
        Assert.assertNotNull(value, "Could not extract '" + jsonPath + "' from response");
        return value;
    }
}
