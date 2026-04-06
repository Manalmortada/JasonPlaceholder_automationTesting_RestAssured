import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertTrue;


public class FullJASONPlaceholderTests {
    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType("application/json")
                .build();

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
    @Test
    public void testGetAllPosts() {
        when().get("/posts")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    public void testGetPostValid() {
        when().get("/posts/1").then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("title", not(isEmptyOrNullString()));
    }

    @Test
    public void testGetPostInvalid() {
        when().get("/posts/9999").then()
                .statusCode(anyOf(equalTo(404), equalTo(200)));
    }

    @Test
    public void testFilterByUserId() {
        when().get("/posts?userId=1").then()
                .statusCode(200)
                .body("userId", everyItem(equalTo(1)));
    }

    @Test
    public void testNestedComments() {
        when().get("/posts/1/comments").then()
                .statusCode(200)
                .body("postId", everyItem(equalTo(1)));
    }

    @Test
    public void testCreatePost() {
        String json = "{\"title\":\"foo\",\"body\":\"bar\",\"userId\":1}";
        given().body(json).when().post("/posts").then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", equalTo("foo"));
    }

    @Test
    public void testPutUpdate() {
        String json = "{\"id\":1,\"title\":\"updated\",\"body\":\"updated\",\"userId\":1}";
        given().body(json).when().put("/posts/1").then()
                .statusCode(200)
                .body("title", equalTo("updated"));
    }

    @Test
    public void testPatchUpdate() {
        String json = "{\"title\":\"patchedTitle\"}";
        given().body(json).when().patch("/posts/1").then()
                .statusCode(200)
                .body("title", equalTo("patchedTitle"));
    }

    @Test
    public void testDeletePost() {
        when().delete("/posts/1").then()
                .statusCode(anyOf(equalTo(200), equalTo(204)));
    }
    @Test
    public void getPostsForUser() {
        given()
                .when().get("/users/1/posts")
                .then().statusCode(200)
                .body("userId", everyItem(equalTo(1)));
    }

    @Test
    public void getPhotosForAlbum() {
        given()
                .when().get("/albums/1/photos")
                .then().statusCode(200)
                .body("albumId", everyItem(equalTo(1)));
    }

    @Test
    public void getTodosForUser() {
        given()
                .when().get("/users/1/todos")
                .then().statusCode(200)
                .body("userId", everyItem(equalTo(1)));
    }
    
}

