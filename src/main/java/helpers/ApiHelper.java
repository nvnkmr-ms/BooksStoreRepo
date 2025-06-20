package helpers;

import config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * ApiHelper class wraps REST calls with RestAssured,
 * using the base URL from ConfigManager.
 */
public class ApiHelper {

    private static final String BASE_URL = ConfigManager.getBaseUrl();

    public ApiHelper() {
        RestAssured.baseURI = BASE_URL;
    }

    /**
     * POST request to create a resource at the given path with JSON body.
     *
     * @param endpoint e.g. "/users"
     * @param jsonBody JSON formatted string request body
     * @return Response from the server
     */
    public Response post(String endpoint, String jsonBody) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post(endpoint)
                .then()
                .extract().response();
    }

    /**
     * GET request to retrieve a resource at given endpoint.
     *
     * @param endpoint e.g. "/users/1"
     * @return Response from server
     */
    public Response get(String endpoint) {
        return RestAssured.given()
                .when()
                .get(endpoint)
                .then()
                .extract().response();
    }

    /**
     * PUT request to update a resource at given endpoint with JSON body.
     *
     * @param endpoint e.g. "/users/1"
     * @param jsonBody JSON formatted request body
     * @return Response
     */
    public Response put(String endpoint, String jsonBody) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .put(endpoint)
                .then()
                .extract().response();
    }

    /**
     * DELETE request to delete resource at the given endpoint.
     *
     * @param endpoint e.g. "/users/1"
     * @return Response
     */
    public Response delete(String endpoint) {
        return RestAssured.given()
                .when()
                .delete(endpoint)
                .then()
                .extract().response();
    }

    /**
     * GET request to list resources.
     *
     * @param endpoint e.g. "/users"
     * @return Response
     */
    public Response list(String endpoint) {
        return get(endpoint);
    }
}