package com.framework.stepdefs;

import com.framework.api.client.RestClient;
import com.framework.api.models.UserPayload;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.testng.Assert;

public class APIStepDefs {

    private final RestClient client = new RestClient();
    private Response response;
    private UserPayload userPayload;
    private UserPayload loginPayload;

    // ── Given ────────────────────────────────────────────────────

    @Given("I have user payload with name {string} and job {string}")
    public void iHaveUserPayload(String name, String job) {
        userPayload = new UserPayload(name, job);
    }

    @Given("I have login payload with email {string} and password {string}")
    public void iHaveLoginPayload(String email, String password) {
        loginPayload = new UserPayload(email, password, true);
    }

    @Given("I have login payload with email {string} and no password")
    public void iHaveLoginPayloadNoPassword(String email) {
        loginPayload = new UserPayload();
        loginPayload.setEmail(email);
    }

    // ── When ─────────────────────────────────────────────────────

    @When("I send GET request to {string}")
    public void iSendGetRequest(String endpoint) {
        response = client.get(endpoint);
    }

    @When("I send POST request to {string}")
    public void iSendPostRequest(String endpoint) {
        Object body = (userPayload != null) ? userPayload : loginPayload;
        response = client.post(endpoint, body);
        userPayload  = null;
        loginPayload = null;
    }

    @When("I send PUT request to {string}")
    public void iSendPutRequest(String endpoint) {
        response = client.put(endpoint, userPayload);
        userPayload = null;
    }

    @When("I send DELETE request to {string}")
    public void iSendDeleteRequest(String endpoint) {
        response = client.delete(endpoint);
    }

    // ── Then ─────────────────────────────────────────────────────

    @Then("response status code should be {int}")
    public void responseStatusCodeShouldBe(int statusCode) {
        Assert.assertEquals(response.getStatusCode(), statusCode,
                "Status mismatch. Body: " + response.getBody().asString());
    }

    @Then("response should contain {string}")
    public void responseShouldContain(String key) {
        Object value = response.jsonPath().get(key);
        Assert.assertNotNull(value, "Key '" + key + "' not found in response");
    }

    @Then("response field {string} should be {string}")
    public void responseFieldShouldBe(String jsonPath, String expected) {
        Object actual = response.jsonPath().get(jsonPath);
        Assert.assertEquals(String.valueOf(actual), expected,
                "Field '" + jsonPath + "' mismatch");
    }
}
