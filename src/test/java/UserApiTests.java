import client.ApiClient;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class UserApiTests {

    private ApiClient apiClient;
    private ObjectMapper objectMapper;

    @BeforeClass
    public void setup() {
        apiClient = new ApiClient();
        objectMapper = new ObjectMapper();
    }

    private String mapToJson(Map<String, Object> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert map to JSON", e);
        }
    }

    private Integer createTestUser(String name, String email) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);

        String reqBody = mapToJson(user);
        Response response = apiClient.createUser(reqBody);
        Assert.assertEquals(response.statusCode(), 201, "User creation failed");

        return response.jsonPath().getInt("id");
    }

    // Test Cases_01
    
    @Test(description = "Create User - Positive Scenario")
    public void testCreateUser() {
        String name = "John Doe";
        String email = "john@example.com";
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);

        Response response = apiClient.createUser(mapToJson(user));

        Assert.assertEquals(response.statusCode(), 201, "Unexpected status code for create");
        Assert.assertNotNull(response.jsonPath().getInt("id"), "User id should not be null");
        Assert.assertEquals(response.jsonPath().getString("name"), name);
        Assert.assertEquals(response.jsonPath().getString("email"), email);
    }

    @Test(description = "Get User - Positive Scenario")
    public void testGetUser() {
        Integer userId = createTestUser("Alice", "alice@example.com");

        Response response = apiClient.getUser(userId);
        Assert.assertEquals(response.statusCode(), 200, "Failed to fetch user");

        Assert.assertEquals(response.jsonPath().getInt("id"), userId);
        Assert.assertEquals(response.jsonPath().getString("name"), "Alice");
        Assert.assertEquals(response.jsonPath().getString("email"), "alice@example.com");
    }

    @Test(description = "Update User - Positive Scenario")
    public void testUpdateUser() {
        Integer userId = createTestUser("Bob", "bob@example.com");

        Map<String, Object> updatedUser = new HashMap<>();
        updatedUser.put("name", "Bobby");
        updatedUser.put("email", "bobby@example.com");

        Response updateResp = apiClient.updateUser(userId, mapToJson(updatedUser));
        Assert.assertEquals(updateResp.statusCode(), 200, "Update user failed");

        Assert.assertEquals(updateResp.jsonPath().getString("name"), "Bobby");
        Assert.assertEquals(updateResp.jsonPath().getString("email"), "bobby@example.com");
    }

    @Test(description = "Delete User - Positive Scenario")
    public void testDeleteUser() {
        Integer userId = createTestUser("Delete Me", "delete@example.com");

        Response deleteResp = apiClient.deleteUser(userId);
        Assert.assertEquals(deleteResp.statusCode(), 204, "Delete user failed");

        Response getResp = apiClient.getUser(userId);
        Assert.assertEquals(getResp.statusCode(), 404, "Deleted user should not be found");
    }

    @Test(description = "List Users - Positive Scenario")
    public void testListUsers() {
        Response response = apiClient.listUsers();
        Assert.assertEquals(response.statusCode(), 200, "Failed to list users");
        Assert.assertTrue(response.jsonPath().getList("$").size() >= 0, "User list is empty or null");
    }

    @Test(description = "Create User - Negative Scenario: Missing Fields")
    public void testCreateUserMissingFields() {
        String invalidJson = "{}";
        Response response = apiClient.createUser(invalidJson);
        Assert.assertEquals(response.statusCode(), 400, "Expected 400 for missing fields");
    }

    @Test(description = "Get User - Negative Scenario: Nonexistent User")
    public void testGetNonexistentUser() {
        int invalidUserId = 99999;
        Response response = apiClient.getUser(invalidUserId);
        Assert.assertEquals(response.statusCode(), 404, "Expected 404 for nonexistent user");
    }

    @Test(description = "Update User - Negative Scenario: Nonexistent User")
    public void testUpdateNonexistentUser() {
        int invalidUserId = 99999;
        Map<String, Object> user = new HashMap<>();
        user.put("name", "Ghost");
        user.put("email", "ghost@example.com");

        Response response = apiClient.updateUser(invalidUserId, mapToJson(user));
        Assert.assertEquals(response.statusCode(), 404, "Expected 404 for updating nonexistent user");
    }

    @Test(description = "Delete User - Negative Scenario: Nonexistent User")
    public void testDeleteNonexistentUser() {
        int invalidUserId = 99999;
        Response response = apiClient.deleteUser(invalidUserId);
        Assert.assertEquals(response.statusCode(), 404, "Expected 404 for deleting nonexistent user");
    }

    @Test(description = "Request Chaining: Create -> Update -> Get -> Delete User")
    public void testRequestChaining() {
        // Create
        Integer userId = createTestUser("Chain User", "chain@example.com");

        // Update
        Map<String, Object> updated = new HashMap<>();
        updated.put("name", "Chained Updated");
        updated.put("email", "updated@example.com");

        Response updateResp = apiClient.updateUser(userId, mapToJson(updated));
        Assert.assertEquals(updateResp.statusCode(), 200);

        // Get
        Response getResp = apiClient.getUser(userId);
        Assert.assertEquals(getResp.statusCode(), 200);
        Assert.assertEquals(getResp.jsonPath().getString("name"), updated.get("name"));
        Assert.assertEquals(getResp.jsonPath().getString("email"), updated.get("email"));

        // Delete
        Response deleteResp = apiClient.deleteUser(userId);
        Assert.assertEquals(deleteResp.statusCode(), 204);
    }
}
