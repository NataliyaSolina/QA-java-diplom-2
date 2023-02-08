import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.example.order.*;
import org.example.user.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;

public class CreateOrderTest{
    UserMethods methodUser = new UserMethods();
    OrderMethods methodOrder = new OrderMethods();
    UserGen generator = new UserGen();
    User user;
    Response response;
    String token;
    List<String> ingrediets;
    Order order;

    @Before
    public void setUp() {
        user = generator.def();

        response = methodUser.requestAuthUser(Credentials.from(user));  //попробовать залогинется и если ок удалить
        if (response.statusCode() == SC_OK) {
            token = methodUser.getTokenFromAuthUserOk(response);
            methodUser.requestDeleteUser(token);
        }

        response = methodUser.requestRegisterUser(user);
        token = methodUser.getTokenFromRegisterUser(response);
    }

    @After
    public void cleanUp() {
        response = methodUser.requestAuthUser(Credentials.from(user));  //попробовать залогинется и если ок удалить
        if (response.statusCode() == SC_OK) {
            token = methodUser.getTokenFromAuthUserOk(response);
            methodUser.requestDeleteUser(token);
        }
    }

    @Test
    @Description("Создание заказа с ингредиентами с авторизацией")
    public void createOrderValidDataWithAuthRezultOk() {
        ingrediets = List.of(Ingredients.BULKA_FLUORESCENT.getHeshId(), Ingredients.MEAT_SHELLFISH.getHeshId(), Ingredients.RINGS_MINERAL.getHeshId(), Ingredients.CHEESE_MOLD.getHeshId(), Ingredients.SAUSE_SPICY.getHeshId());
        order = new Order(ingrediets);
        response = methodOrder.requestCreateOrder(order, token);
        methodOrder.responseCreateOrderOk(response);
    }

    @Test
    @Description("Создание заказа с ингредиентами без авторизации")
    public void createOrderValidDataWithoutAuthRezultOk() {
        token = null;
        ingrediets = List.of(Ingredients.BULKA_FLUORESCENT.getHeshId(), Ingredients.MEAT_SHELLFISH.getHeshId(), Ingredients.RINGS_MINERAL.getHeshId(), Ingredients.CHEESE_MOLD.getHeshId(), Ingredients.SAUSE_SPICY.getHeshId());
        order = new Order(ingrediets);
        response = methodOrder.requestCreateOrder(order, token);
        methodOrder.responseCreateOrderOk(response);
    }

    @Test
    @Description("Создание заказа без ингредиентов")
    public void createOrderWithoutDataRezultMissingError() {
        order = new Order();
        response = methodOrder.requestCreateOrder(order, token);
        methodOrder.responseCreateOrderWithoutData(response);
    }

    @Test
    @Description("Создание заказа с неверным хешем ингредиентов")
    public void createOrderInalidDataRezultError() {
        String invalidHesh = "invalidhashf82001bdaaa6d";
        ingrediets = List.of(Ingredients.BULKA_FLUORESCENT.getHeshId(), invalidHesh, Ingredients.RINGS_MINERAL.getHeshId(), Ingredients.CHEESE_MOLD.getHeshId(), Ingredients.SAUSE_SPICY.getHeshId());
        order = new Order(ingrediets);
        response = methodOrder.requestCreateOrder(order, token);
        methodOrder.responseCreateOrderInvalidData(response);
    }
}
