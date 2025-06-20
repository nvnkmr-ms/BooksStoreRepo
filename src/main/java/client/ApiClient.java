package client;

import config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.RestAssured.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class ApiClient {

    private static final String BASE_URL = ConfigManager.getBaseUrl();

    public ApiClient() {
        RestAssured.baseURI = BASE_URL;
    }

    // Create User (POST /users)
    public Response createUser(String jsonBody) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post("/users")
                .then()
                .extract()
                .response();
    }

    // Get User by ID (GET /users/{id})
    public Response getUser(int userId) {
        return RestAssured.given()
                .when()
                .get("/users/" + userId)
                .then()
                .extract()
                .response();
    }

    // Update User by ID (PUT /users/{id})
    public Response updateUser(int userId, String jsonBody) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .put("/users/" + userId)
                .then()
                .extract()
                .response();
    }

    // Delete User by ID (DELETE /users/{id})
    public Response deleteUser(int userId) {
        return RestAssured.given()
                .when()
                .delete("/users/" + userId)
                .then()
                .extract()
                .response();
    }

    // List all Users (GET /users)
    public Response listUsers() {
        return RestAssured.given()
                .when()
                .get("/users")
                .then()
                .extract()
                .response();
    }
}
