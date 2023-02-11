import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.example.order.*;
import org.example.user.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;

public class GetOrderForUserTest {
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

        methodUser.requestRegisterUser(user);                           //если уже есть то токен тут не получить, нужно авторизоваться
        response = methodUser.requestAuthUser(Credentials.from(user));
        token = methodUser.getTokenFromAuthUserOk(response);

        ingrediets = List.of(Ingredients.BULKA_FLUORESCENT.getHeshId(), Ingredients.MEAT_SHELLFISH.getHeshId(), Ingredients.RINGS_MINERAL.getHeshId(), Ingredients.CHEESE_MOLD.getHeshId(), Ingredients.SAUSE_SPICY.getHeshId());
        order = new Order(ingrediets);
        response = methodOrder.requestCreateOrder(order, token);
        methodOrder.responseCreateOrderOk(response);
    }

    @After
    public void cleanUp() {
        response = methodUser.requestAuthUser(Credentials.from(user));
        if (response.statusCode() == SC_OK) {
            token = methodUser.getTokenFromAuthUserOk(response);
            methodUser.requestDeleteUser(token);
        }
    }

    @Test
    @Description("Получение заказов конкретного пользователя с авторизацией")
    public void getUserOrdersValidDataWithAuthResultOk() {
        response = methodOrder.requestGetUserOrders(token);
        methodOrder.responseGetUserOrdersOk(response, order);
    }

    @Test
    @Description("Получение заказов конкретного пользователя без авторизации")
    public void getUserOrdersWithoutAuthResultAuthError() {
        token = null;
        response = methodOrder.requestGetUserOrders(token);
        methodOrder.responseGetUserOrdersWithoutAuth(response);
    }

}
