import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.example.user.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;

public class AuthUserTest {
    UserMethods method = new UserMethods();
    UserGen generator = new UserGen();
    User user;
    Credentials cred;
    Response response;
    String token;

    @Before
    public void setUp() {
        user = generator.random();
        cred = Credentials.from(user);

        method.requestRegisterUser(user);
    }

    @After
    public void cleanUp() {
        if (response.statusCode() == SC_OK) {
            method.requestDeleteUser(token);
        }
    }

    @Test
    @Description("Логин под существующим пользователем")
    public void authUserValidDataRezultOk() {
        response = method.requestAuthUser(cred);
        method.responseAuthUserOk(response, user);
        token = method.getTokenFromAuthUserOk(response);
    }

}
