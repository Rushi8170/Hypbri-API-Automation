package com.framework.tests.api;

import com.framework.api.client.RestClient;
import com.framework.api.endpoints.APIEndpoints;
import com.framework.api.models.UserPayload;
import com.framework.api.validators.ResponseValidator;
import com.framework.utils.ExtentLogger;
import com.framework.utils.TestDataUtil;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class UserAPITest extends BaseAPITest {
    private final RestClient client = new RestClient();

    @Test(groups = {"smoke","api"}, description = "GET all users returns 200")
    public void testGetAllUsers() {
        Response r = client.get(APIEndpoints.USERS);
        ResponseValidator.assertStatusCode(r, 200);
        ResponseValidator.assertBodyContainsKey(r, "data");
        ResponseValidator.assertResponseTimeBelow(r, 5000);
        ExtentLogger.pass("GET /users OK — total: " + r.jsonPath().getInt("total"));
    }

    @Test(groups = {"smoke","api"}, description = "GET single user returns 200")
    public void testGetUserById() {
        Response r = client.get(APIEndpoints.USERS + "/2");
        ResponseValidator.assertStatusCode(r, 200);
        ResponseValidator.assertFieldValue(r, "data.id", 2);
        ExtentLogger.pass("GET /users/2 OK");
    }

    @Test(groups = {"api"}, description = "GET unknown user returns 404")
    public void testGetNonExistingUser() {
        Response r = client.get(APIEndpoints.USERS + "/9999");
        ResponseValidator.assertStatusCode(r, 404);
        ExtentLogger.pass("404 returned correctly");
    }

    @Test(groups = {"smoke","api"}, description = "POST create user returns 201")
    public void testCreateUser() {
        String name = TestDataUtil.randomFirstName();
        UserPayload p = new UserPayload(name, "QA Engineer");
        Response r = client.post(APIEndpoints.USERS, p);
        ResponseValidator.assertStatusCode(r, 201);
        ResponseValidator.assertBodyContainsKey(r, "id");
        ResponseValidator.assertFieldValue(r, "name", name);
        ExtentLogger.pass("User created ID: " + r.jsonPath().getString("id"));
    }

    @Test(groups = {"api"}, description = "PUT update user returns 200")
    public void testUpdateUser() {
        UserPayload p = new UserPayload(TestDataUtil.randomFirstName(), "Senior QA");
        Response r = client.put(APIEndpoints.USERS + "/2", p);
        ResponseValidator.assertStatusCode(r, 200);
        ResponseValidator.assertBodyContainsKey(r, "updatedAt");
        ExtentLogger.pass("User updated OK");
    }

    @Test(groups = {"api"}, description = "PATCH user returns 200")
    public void testPatchUser() {
        UserPayload p = new UserPayload();
        p.setJob("Lead QA");
        Response r = client.patch(APIEndpoints.USERS + "/2", p);
        ResponseValidator.assertStatusCode(r, 200);
        ExtentLogger.pass("User patched OK");
    }

    @Test(groups = {"api"}, description = "DELETE user returns 204")
    public void testDeleteUser() {
        Response r = client.delete(APIEndpoints.USERS + "/2");
        ResponseValidator.assertStatusCode(r, 204);
        ExtentLogger.pass("User deleted OK");
    }

    @Test(groups = {"api"}, description = "GET users page 2 returns 200")
    public void testGetUsersPage2() {
        Response r = client.get(APIEndpoints.USERS + "?page=2");
        ResponseValidator.assertStatusCode(r, 200);
        ResponseValidator.assertFieldValue(r, "page", 2);
        ExtentLogger.pass("Page 2 users returned OK");
    }
}
