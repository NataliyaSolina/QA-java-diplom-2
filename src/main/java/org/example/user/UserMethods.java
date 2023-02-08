package org.example.user;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.SettingsRequest;
import org.hamcrest.CoreMatchers;

import static io.restassured.RestAssured.given;
import static java.util.Objects.nonNull;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class UserMethods extends SettingsRequest {
    @Step("Send POST request to /api/auth/register")
    public Response requestRegisterUser(User user) {
        Response response =
                given()
                        .spec(getSpec())
                        .and()
                        .body(user)
                        .when()
                        .post("/auth/register");
        System.out.println("requestRegisterUser " + response.body().asString());
        return response;
    }

    @Step("Receive POST response Ok to /api/auth/register")
    public void responseRegisterUserOk(Response response, User user) {
        response
                .then()
                .assertThat()
//                .statusCode(SC_CREATED)
//                .and()
                .body("success", is(true))
                .body("user.email", equalTo(user.getEmail().toLowerCase()))
                .body("user.name", equalTo(user.getName()))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Step("Get token from POST response Ok to /api/auth/register")
    public String getTokenFromRegisterUser(Response response) {
        return response
                .path("accessToken");
    }

    @Step("Receive POST response NotUnique to /api/auth/register")
    public void responseRegisterUserNotUniqueData(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", is(false))
                .body("message", CoreMatchers.equalTo("User already exists"));
    }

    @Step("Receive POST response MissingField to /api/auth/register")
    public void responseRegisterUserMissingField(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", is(false))
                .body("message", CoreMatchers.equalTo("Email, password and name are required fields"));
    }

    @Step("Send POST request to /api/auth/login")
    public Response requestAuthUser(Credentials cred) {
        Response response =
                given()
                        .spec(getSpec())
                        .and()
                        .body(cred)
                        .when()
                        .post("/auth/login");
        System.out.println("requestAuthUser " + response.body().asString());
        return response;
    }

    @Step("Receive POST response Ok to /api/auth/login")
    public void responseAuthUserOk(Response response, User user) {
        response
                .then()
                .assertThat()
                .body("success", is(true))
                .body("user.email", equalTo(user.getEmail().toLowerCase()))
                .body("user.name", equalTo(user.getName()))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Step("Get token from POST response Ok to /api/auth/login")
    public String getTokenFromAuthUserOk(Response response) {
        return response
                .path("accessToken");
    }

    @Step("Receive POST response InvalideData to /api/auth/login")
    public void responseAuthInvalideData(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .body("message", CoreMatchers.equalTo("email or password are incorrect"));
    }

    @Step("Send DELETE request to /api/auth/user")
    public Response requestDeleteUser(String token) {
        if (!nonNull(token)) {
            token = "";
        }
        Response response =
                given()
                        .spec(getSpec())
                        .and()
                        .header("Authorization", token)
                        .when()
                        .delete("/auth/user");
        System.out.println("requestDeleteUser " + response.body().asString());
        return response;
    }

    @Step("Receive DELETE response Ok to /api/auth/user")
    public void responseDeleteUserOk(Response response) {
        response
                .then()
                .assertThat()
                .body("success", is(true))
                .body("message", notNullValue());
    }

    @Step("Send PATCH request to /api/auth/user")
    public Response requestChangeUser(String changes, User user, String token) {
        if (!nonNull(token)) {
            token = "";
        }
        Response response =
                given()
                        .spec(getSpec())
                        .and()
                        .header("Authorization", token)
                        .and()
                        .body(changes)
                        .when()
                        .patch("/auth/user");

        if (response.statusCode() == SC_OK) {
            String SEARCH_EMAIL = "\"email\":";
            String SEARCH_PASSWORD = "\"password\":";
            String SEARCH_NAME = "\"name\":";
            int from;
            int to;
            if (changes.indexOf(SEARCH_EMAIL) > 0) {
                from = changes.indexOf("\"", (changes.indexOf(SEARCH_EMAIL) + SEARCH_EMAIL.length()));
                to = changes.indexOf("\"", (from + 1));
                user.setEmail(changes.substring((from + 1), to));
                System.out.println(changes + "   " + from + "   " + to + "    " + changes.substring((from + 1), to));
            }
            if (changes.indexOf(SEARCH_PASSWORD) > 0) {
                from = changes.indexOf("\"", (changes.indexOf(SEARCH_PASSWORD) + SEARCH_PASSWORD.length()));
                to = changes.indexOf("\"", (from + 1));
                user.setPassword(changes.substring((from + 1), to));
                System.out.println(changes + "   " + from + "   " + to + "    " + changes.substring((from + 1), to));
            }
            if (changes.indexOf(SEARCH_NAME) > 0) {
                from = changes.indexOf("\"", (changes.indexOf(SEARCH_NAME) + SEARCH_NAME.length()));
                to = changes.indexOf("\"", (from + 1));
                user.setName(changes.substring((from + 1), to));
                System.out.println(changes + "   " + from + "   " + to + "    " + changes.substring((from + 1), to));
            }
        }
        System.out.println("requestChangeUser " + response.body().asString());
        return response;
    }

    @Step("Receive PATCH response Ok to /api/auth/user")
    public void responseChangeUserOk(Response response, User user) {
        response
                .then()
                .assertThat()
                .body("success", is(true))
                .body("user.email", equalTo(user.getEmail().toLowerCase()))
                .body("user.name", equalTo(user.getName()));
    }

    @Step("Receive PATCH response NotAuth to /api/auth/user")
    public void responseChangeUserNotAuth(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Step("Receive PATCH response NotUnique to /api/auth/user")
    public void responseChangeUserNotUniqueData(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", is(false))
                .body("message", equalTo("\"User with such email already exists"));
    }
}
