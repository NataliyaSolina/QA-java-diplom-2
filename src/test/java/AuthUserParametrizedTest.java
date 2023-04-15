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
import static org.example.user.UserGen.*;

@RunWith(Parameterized.class)
public class AuthUserParametrizedTest {
    UserMethods method = new UserMethods();
    User user;
    Credentials cred;
    Response response;
    String token;
    private final String email;
    private final String password;
    private final String emailCred;
    private final String passwordCred;

    public AuthUserParametrizedTest(String email, String password, String emailCred, String passwordCred) {
        this.email = email;
        this.password = password;
        this.emailCred = emailCred;
        this.passwordCred = passwordCred;
    }

    @Parameterized.Parameters(name = "creds - {2}, {3}")
    public static Object[][] getTextData() {
        return new Object[][]{
                {EMAIL, PASSWORD, null, PASSWORD},
                {EMAIL, PASSWORD, "", PASSWORD},
                {EMAIL, PASSWORD, RandomStringUtils.randomAlphanumeric(6, 9) + "@yandex.ru", PASSWORD},
                {EMAIL, PASSWORD, EMAIL, null},
                {EMAIL, PASSWORD, EMAIL, ""},
                {EMAIL, PASSWORD, EMAIL, RandomStringUtils.randomAlphanumeric(3, 6)},
                {EMAIL, PASSWORD, null, null},
                {EMAIL, PASSWORD, RandomStringUtils.randomAlphanumeric(6, 9) + "@yandex.ru", RandomStringUtils.randomAlphanumeric(3, 6)}
        };
    }

    @Before
    public void setUp() {
        user = new User(email, password, RandomStringUtils.randomAlphabetic(3, 9));
        cred = new Credentials(emailCred, passwordCred);

        method.requestRegisterUser(user);
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
    @Description("Логин с неверным логином и паролем")
    public void authUserInvalideDataRezultAuthError() {
        response = method.requestAuthUser(cred);
        method.responseAuthInvalideData(response);
    }
}
