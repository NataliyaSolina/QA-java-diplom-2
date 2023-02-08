package org.example.order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.SettingsRequest;

import static io.restassured.RestAssured.given;
import static java.util.Objects.nonNull;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.*;

public class OrderMethods extends SettingsRequest {

    @Step("Send POST request to /api/orders")
    public Response requestCreateOrder(Order order, String token) {
        if (!nonNull(token)) {
            token = "";
        }
        Response response =
                given()
                        .spec(getSpec())
                        .and()
                        .header("Authorization", token)
                        .and()
                        .body(order)
                        .when()
                        .post("/orders");
        System.out.println("requestCreateOrder " + response.body().asString());
        return response;
    }

    @Step("Receive POST response Ok to /api/orders")
    public void responseCreateOrderOk(Response response) {
        response
                .then()
                .assertThat()
                .body("success", is(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Step("Receive POST response WithoutData to /api/orders")
    public void responseCreateOrderWithoutData(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("success", is(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Step("Receive POST response InvalidData to /api/orders")
    public void responseCreateOrderInvalidData(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Step("Send GET request to /api/orders")
    public Response requestGetUserOrders(String token) {
        if (!nonNull(token)) {
            token = "";
        }
        Response response =
                given()
                        .spec(getSpec())
                        .and()
                        .header("Authorization", token)
                        .when()
                        .get("/orders");
        System.out.println("requestGetUserOrders " + response.body().asString());
        return response;
    }

    @Step("Receive GET response Ok to /api/orders")
    public void responseGetUserOrdersOk(Response response, Order order) {
        response
                .then()
                .assertThat()
                .body("success", is(true))
                .body("orders", notNullValue())
                .body("orders.ingredients[0]", notNullValue())
                .body("orders._id[0]", notNullValue())
                .body("orders.status[0]", notNullValue())
                .body("orders.number[0]", greaterThan(0))
                .body("orders.createdAt[0]", notNullValue())
                .body("orders.updatedAt[0]", notNullValue())
                .body("total", greaterThan(0))
                .body("totalToday", greaterThan(0));
        for (int i = 0; i < order.getIngredients().size(); i++) {
            response
                    .then()
                    .assertThat()
                    .body("orders.ingredients[0][" + i + "]", equalTo(order.getIngredients().get(i)));
        }
    }

    @Step("Receive GET response WithoutAuth to /api/orders")
    public void responseGetUserOrdersWithoutAuth(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .body("message", equalTo("You should be authorised"));
    }
}