import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.example.user.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;

public class RegisterUserTest {
    UserMethods method = new UserMethods();
    UserGen generator = new UserGen();
    User user;
    Response response;
    String token;

    @Before
    public void setUp() {
        user = generator.random();

        response = method.requestAuthUser(Credentials.from(user));  //попробовать залогинется и если ок удалить
        if (response.statusCode() == SC_OK) {
            token = method.getTokenFromAuthUserOk(response);
            method.requestDeleteUser(token);
        }
    }

    @After
    public void cleanUp() {
        response = method.requestAuthUser(Credentials.from(user));  //попробовать залогинется и если ок удалить
        if (response.statusCode() == SC_OK) {
            token = method.getTokenFromAuthUserOk(response);
            method.requestDeleteUser(token);
        }
    }

    @Test
    @Description("создать уникального пользователя")
    public void registerUserValidDataRezultOk() {
        response = method.requestRegisterUser(user);
        method.responseRegisterUserOk(response, user);
        token = method.getTokenFromRegisterUser(response);
    }

    @Test
    @Description("создать пользователя, который уже зарегистрирован")
    public void registerUserNotUniqueDataRezultExistsError() {
        method.requestRegisterUser(user);

        response = method.requestRegisterUser(user);
        method.responseRegisterUserNotUniqueData(response);
    }
}
