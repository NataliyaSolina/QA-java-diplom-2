import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.example.user.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_OK;
import static org.example.user.UserGen.*;

@RunWith(Parameterized.class)
public class ChangeUserParametrizedTest {
    UserMethods method = new UserMethods();
    UserGen generator = new UserGen();
    User user;
    Response response;
    String token;
    private final String changes;

    public ChangeUserParametrizedTest(String changes) {
        this.changes = changes;
    }

    @Parameterized.Parameters(name = "creds - {0}")
    public static Object[][] getTextData() {
        return new Object[][]{                          //TODO сделать рандомом? String.format
                {String.format("{\"email\": \"%s\"}", EMAIL)},
                {String.format("{\"password\": \"%s\"}", PASSWORD)},
                {String.format("{\"name\": \"%s\"}", NAME)},
                {String.format("{\"email\": \"%s\",\n\"password\": \"%s\",\n\"name\": \"%s\"}", EMAIL, PASSWORD, NAME)},
                {String.format("{\"name\": \"%s\",\n\"password\": \"%s\"}", NAME, PASSWORD)}
        };
    }

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

    @Test
    @Description("Изменение данных пользователя с авторизацией")
    public void changeUserValidDataRezultOk() {
        response = method.requestChangeUser(changes, user, token);
        method.responseChangeUserOk(response, user);
    }

    @Test
    @Description("Изменение данных пользователя без авторизациии")
    public void changeUserNotAuthRezultAuthError() {
        token = null;
        response = method.requestChangeUser(changes, user, token);
        method.responseChangeUserNotAuth(response);
    }
}
