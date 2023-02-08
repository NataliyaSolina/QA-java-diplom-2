import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.user.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_OK;

@RunWith(Parameterized.class)
public class RegisterUserParametrizedTest {
    UserMethods method = new UserMethods();
    User user;
    Response response;
    String token;
    private final String email;
    private final String password;
    private final String name;

    public RegisterUserParametrizedTest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Parameterized.Parameters(name = "creds - {0}, {1}, {2}")
    public static Object[][] getTextData() {
        return new Object[][]{
                {null, RandomStringUtils.randomAlphanumeric(4, 10), RandomStringUtils.randomAlphabetic(3, 9)},
                {"", RandomStringUtils.randomAlphanumeric(4, 10), RandomStringUtils.randomAlphabetic(3, 9)},
                {RandomStringUtils.randomAlphanumeric(6, 9) + "@yandex.ru", null, RandomStringUtils.randomAlphabetic(3, 9)},
                {RandomStringUtils.randomAlphanumeric(6, 9) + "@yandex.ru", "", RandomStringUtils.randomAlphabetic(3, 9)},
                {RandomStringUtils.randomAlphanumeric(6, 9) + "@yandex.ru", RandomStringUtils.randomAlphanumeric(4, 10), null},
                {RandomStringUtils.randomAlphanumeric(6, 9) + "@yandex.ru", RandomStringUtils.randomAlphanumeric(4, 10), ""}
        };
    }

    @Before
    public void setUp() {
        user = new User(email, password, name);

        response = method.requestAuthUser(Credentials.from(user));  //TODO +попробовать залогинется и если ок удалить
        if (response.statusCode() == SC_OK) {
            token = method.getTokenFromAuthUserOk(response);
            method.requestDeleteUser(token);
        }
    }

    @After
    public void cleanUp() {
        response = method.requestAuthUser(Credentials.from(user));  //TODO +попробовать залогинется и если ок удалить
        if (response.statusCode() == SC_OK) {
            token = method.getTokenFromAuthUserOk(response);
            method.requestDeleteUser(token);
        }
    }

    @Test
    @Description("создать пользователя и не заполнить одно из обязательных полей")
    public void registerUserMissingFieldRezultMissingError() {
        response = method.requestRegisterUser(user);
        method.responseRegisterUserMissingField(response);
    }
}
