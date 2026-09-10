package com.framework.tests.api;

import com.framework.api.client.RestClient;
import com.framework.api.endpoints.APIEndpoints;
import com.framework.api.models.UserPayload;
import com.framework.api.validators.ResponseValidator;
import com.framework.utils.ExtentLogger;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthAPITest extends BaseAPITest {
    private final RestClient client = new RestClient();

    @Test(groups = {"smoke","api"}, description = "Valid login returns token")
    public void testSuccessfulLogin() {
        UserPayload p = new UserPayload("eve.holt@reqres.in", "cityslicka", true);
        Response r = client.post(APIEndpoints.LOGIN, p);
        ResponseValidator.assertStatusCode(r, 200);
        ResponseValidator.assertBodyContainsKey(r, "token");
        String token = r.jsonPath().getString("token");
        Assert.assertNotNull(token, "Token must not be null");
        ExtentLogger.pass("Login OK — token: " + token);
    }

    @Test(groups = {"api"}, description = "Login without password returns 400")
    public void testLoginMissingPassword() {
        UserPayload p = new UserPayload();
        p.setEmail("eve.holt@reqres.in");
        Response r = client.post(APIEndpoints.LOGIN, p);
        ResponseValidator.assertStatusCode(r, 400);
        ExtentLogger.pass("400 returned for missing password");
    }

    @Test(groups = {"smoke","api"}, description = "Register with valid data returns token")
    public void testSuccessfulRegister() {
        UserPayload p = new UserPayload("eve.holt@reqres.in", "pistol", true);
        Response r = client.post(APIEndpoints.REGISTER, p);
        ResponseValidator.assertStatusCode(r, 200);
        ResponseValidator.assertBodyContainsKey(r, "token");
        ResponseValidator.assertBodyContainsKey(r, "id");
        ExtentLogger.pass("Register OK");
    }

    @Test(groups = {"api"}, description = "Register without password returns 400")
    public void testRegisterMissingPassword() {
        UserPayload p = new UserPayload();
        p.setEmail("sydney@fife.com");
        Response r = client.post(APIEndpoints.REGISTER, p);
        ResponseValidator.assertStatusCode(r, 400);
        ResponseValidator.assertFieldValue(r, "error", "Missing password");
        ExtentLogger.pass("400 returned for missing password on register");
    }
}
