import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.example.user.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.example.user.UserGen.*;

public class ChangeUserTest {
    UserMethods method = new UserMethods();
    UserGen generator = new UserGen();
    User user;
    Response response;
    String token;
    String changes;

    @Before
    public void setUp() {
        user = new User(EMAIL, PASSWORD, NAME);                                     //удалить юзера с емаилом на который менять
        response = method.requestAuthUser(Credentials.from(user));
        if (response.statusCode() == SC_OK) {
            token = method.getTokenFromAuthUserOk(response);
            method.requestDeleteUser(token);
        }

        user = generator.random();

        method.requestRegisterUser(user);                           //если уже есть то токен тут не получить, нужно авторизоваться
        response = method.requestAuthUser(Credentials.from(user));
        token = method.getTokenFromAuthUserOk(response);
    }

    @After
    public void cleanUp() {
        response = method.requestAuthUser(Credentials.from(user));
        if (response.statusCode() == SC_OK) {
            token = method.getTokenFromAuthUserOk(response);
            method.requestDeleteUser(token);
        }
    }

    @Ignore
    @Test
    @Description("Изменение данных пользователя без авторизациии")
    public void changeUserNotAuthRezultAuthError() {
        changes = String.format("{\"email\": \"%s\",\n\"password\": \"%s\",\n\"name\": \"%s\"}", EMAIL, PASSWORD, NAME);
        token = null;
        response = method.requestChangeUser(changes, user, token);
        method.responseChangeUserNotAuth(response);
    }
}
