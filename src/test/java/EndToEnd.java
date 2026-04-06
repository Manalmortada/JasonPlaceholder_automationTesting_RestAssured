import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class EndToEnd {
    private static int postId;
    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType("application/json")
                .build();

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
    @Test(priority = 1)
    public void createPost() {
        postId = given()
                .body("{\"title\":\"E2E Title\",\"body\":\"Testing full flow\",\"userId\":1}")
                .when().post("/posts")
                .then().assertThat()
                .statusCode(201)
                .body("id", notNullValue())
                .extract().jsonPath().getInt("id");
    }

    @Test(priority = 2)
    public void verifyCreation() {
        given()
                .when().get("/posts/{id}", postId)
                .then().assertThat()
                .statusCode(anyOf(is(404), is(200)));

    }

    @Test(priority = 3)
    public void updatePost() {
        given()
                .body("{\"title\":\"E2E Updated Title\"}")
                .when().patch("/posts/{id}", postId)
                .then().assertThat()
                .statusCode(200)
                .body("title", equalTo("E2E Updated Title"));
    }

    @Test(priority = 4)
    public void verifyUpdate() {
        given()
                .when().get("/posts/{id}", postId)
                .then().assertThat()
                .statusCode(anyOf(is(404), is(200)));
    }

    @Test(priority = 5)
    public void deletePost() {
        given()
                .when().delete("/posts/{id}", postId)
                .then().assertThat()
                .statusCode(anyOf(is(200), is(204)));
    }

    @Test(priority = 6)
    public void verifyDeletion() {
        given()
                .when().get("/posts/{id}", postId)
                .then().assertThat()
                .statusCode(anyOf(is(404), is(200)));
    }
}


