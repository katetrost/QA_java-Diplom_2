package site.stellarburgers;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends RestAssuredClient {
    private static final String USER_PATH = "/api/auth";

    @Step("Создание пользователя.")
    public ValidatableResponse create(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH + "/register")
                .then();
    }

    @Step("Авторизация пользователя.")
    public ValidatableResponse login (UserCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(USER_PATH + "/login")
                .then()
                .log().body();
    }

    @Step("Выход из системы.")
    public ValidatableResponse exit (String refreshToken) {
        return given()
                .spec(getBaseSpec())
                .body(refreshToken)
                .when()
                .post(USER_PATH + "/logout")
                .then();
    }

    @Step ("Удалить пользователя.")
    public void delete() {
        if (Tokens.getAccessToken() == null) {
            return;
        }
        given()
                .spec(getBaseSpec())
                .auth().oauth2(Tokens.getAccessToken())
                .when()
                .delete(USER_PATH)
                .then()
                .statusCode(202);
    }

    @Step ("Информация о пользователе")
    public ValidatableResponse userInfo(String token) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token.replace("Bearer ", ""))
                .when()
                .get(USER_PATH + "/user")
                .then();
    }

    @Step ("Изменение информации о пользователе")
    public ValidatableResponse userInfoChange(String token, UserCredentials userCredentials) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token.replace("Bearer ", ""))
                .body(userCredentials)
                .when()
                .patch(USER_PATH + "/user")
                .then();
    }
}
