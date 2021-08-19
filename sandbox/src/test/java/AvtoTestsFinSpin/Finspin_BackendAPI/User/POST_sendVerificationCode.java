package AvtoTestsFinSpin.Finspin_BackendAPI.User;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.junit.Test;


public class POST_sendVerificationCode extends auth_green_branch{

    @Test
    //Отправка запроса с ошибкой в теле (Синтаксическая ошибка: лишняя запятая)
    public void sendVerificationCodeSyntaxError() {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        Response response = RestAssured.given()
                .headers("Content-Type", ContentType)
                .body("{\"phone\":\"2000000001\",").
                        when().post("/sendVerificationCode").
                        then().statusCode(400).extract().response();
        Assert.assertFalse(response.jsonPath().getString("errors").isEmpty());
        Assert.assertEquals(response.jsonPath().getString("errors"), "Invalid JSON data in request body: Syntax error.");
        System.out.println("Синтаксическая ошибка: лишняя запятая в теле запроса: " + response.jsonPath().getString("errors"));

    }

    @Test
    //Отправка запроса с ошибкой в токене
    public void sendVerificationCodeBadToken() {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        Response response = RestAssured.given()
                .headers("Content-Type", ContentType)
                .headers("request-date", requestDate)
                .headers("request-token", "mjawns0woc0xnvqxnto1mjowmsswmdowmdgyruywourcn0jfn0y3mjm2rknentneney3qji2qjd")
                .body("{\"phone\":\"2000000001\", \"isNewUser\": false}").
                        when().post("/sendVerificationCode").
                        then().statusCode(500).extract().response();
        Assert.assertEquals(response.jsonPath().getString("errors"), "Bad Token");

        System.out.println("Отправка запроса с ошибкой в токене: "+ response.jsonPath().getString("errors"));
    }

    @Test
    //Отправка запроса с ошибкой в теле (неправильный номер телефона)
    public void sendVerificationCodeValuePhoneIsNotCorrect() {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        Response response = RestAssured.given()
                .headers("Content-Type", ContentType)
                .headers("request-date", requestDate)
                .headers("request-token", requestToken)
                .body("{\"phone\":\"200000000\"}").
                        when().post("/sendVerificationCode").
                        then().statusCode(400).extract().response();
        Assert.assertEquals(response.jsonPath().getString("message"), null);
        Assert.assertEquals(response.jsonPath().getString("details.message"), "[Значение «Phone» неверно.]");
        Assert.assertEquals(response.jsonPath().getString("details.path"), "[phone]");

        System.out.println("Неправильный номер телефона: "+ response.jsonPath().getString("details.message"));
    }

    @Test
    //Отправка запроса с ошибкой в теле (нет обязательного поля телефон)
    public void sendVerificationCodeNoPhone() {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        Response response = RestAssured.given()
                .headers("Content-Type", ContentType)
                .headers("request-date", requestDate)
                .headers("request-token", requestToken).
                        when().post("/sendVerificationCode").
                        then().statusCode(400).extract().response();
        Assert.assertEquals(response.jsonPath().getString("message"), null);
        Assert.assertEquals(response.jsonPath().getString("details.message"), "[Необходимо заполнить «Phone».]");
        Assert.assertEquals(response.jsonPath().getString("details.path"), "[phone]");

        System.out.println("Нет обязательного поля телефон: "+ response.jsonPath().getString("details.message"));
    }

    @Test
    //Стоит признак старого пользователя
    public void sendVerificationCodeIsNewUserFalse() {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        Response response = RestAssured.given()
                .headers("Content-Type", ContentType)
                .headers("request-date", requestDate)
                .headers("request-token", requestToken)
                .body("{\"phone\":\"2000000001\", \"isNewUser\": false}").
                        when().post("/sendVerificationCode").
                        then().statusCode(400).extract().response();
        Assert.assertEquals(response.jsonPath().getString("message"), null);
        Assert.assertEquals(response.jsonPath().getString("details.message"), "[Указанный телефон не найден в системе.]");
        Assert.assertEquals(response.jsonPath().getString("details.path"), "[phone]");

        System.out.println("Стоит признак старого пользователя: "+ response.jsonPath().getString("details.message"));
    }


    @Test
    // Значение поля "phone" заполненно не строкой
    public void sendVerificationCodeValuePhoneNotString() {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        Response response = RestAssured.given()
                .headers("Content-Type", ContentType)
                .headers("request-date", requestDate)
                .headers("request-token", requestToken)
                .body("{\"phone\": 2000000001, \"isNewUser\": true}").
                        when().post("/sendVerificationCode").
                        then().statusCode(400).extract().response();
        Assert.assertEquals(response.jsonPath().getString("message"), null);
        Assert.assertEquals(response.jsonPath().getString("details.message"), "[Значение «Phone» должно быть строкой.]");
        Assert.assertEquals(response.jsonPath().getString("details.path"), "[phone]");

        System.out.println("Значение поля \"phone\" заполненно не строкой:"+ response.jsonPath().getString("details.message"));
    }

    @Test
    // Значение поля "isNewUser" заполненно неверно
    public void sendVerificationCodeValueIsNewUserIsString() {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        Response response = RestAssured.given()
                .headers("Content-Type", ContentType)
                .headers("request-date", requestDate)
                .headers("request-token", requestToken)
                .body("{\"phone\": \"2000000001\", \"isNewUser\": \"true\"}").
                        when().post("/sendVerificationCode").
                        then().statusCode(400).extract().response();
        Assert.assertEquals(response.jsonPath().getString("message"), null);
        Assert.assertEquals(response.jsonPath().getString("details.message"), "[Значение «Is New User» должно быть равно «1» или «0».]");
        Assert.assertEquals(response.jsonPath().getString("details.path"), "[isNewUser]");

        System.out.println("Значение поля \"isNewUser\" заполненно неверно:"+ response.jsonPath().getString("details.message"));
    }



    @Test
    //Стоит признак нового пользователя
    public void sendVerificationCodeIsNewUserTrue() {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        Response response = RestAssured.given()
                .headers("Content-Type", ContentType)
                .headers("request-date", requestDate)
                .headers("request-token", requestToken)
                .body("{\"phone\":\"2000000001\", \"isNewUser\": true}").
                        when().post("/sendVerificationCode").
                        then().statusCode(200).extract().response();
        Assert.assertEquals(response.jsonPath().getString("timeToLife"), "1200");
        Assert.assertEquals(response.jsonPath().getString("canRepeatAfter"), "30");
        Assert.assertEquals(response.jsonPath().getString("isNewUser"), "true");
        Assert.assertEquals(response.jsonPath().getString("smsWasSend"), "true");

        System.out.println("Указанный телефон не найден в системе: smsWasSend:"+ response.jsonPath().getString("smsWasSend"));
    }

    @Test
    //Телефонный номер уже есть в системе
    public void sendVerificationCodePhoneWasSistem() {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        Response response = RestAssured.given()
                .headers("Content-Type", ContentType)
                .headers("request-date", requestDate)
                .headers("request-token", requestToken)
                .body("{\"phone\":\"9000000001\", \"isNewUser\": true}").
                        when().post("/sendVerificationCode").
                        then().statusCode(400).extract().response();
        Assert.assertEquals(response.jsonPath().getString("message"), null);
        Assert.assertEquals(response.jsonPath().getString("details.message"), "[Указанный телефон уже используется, вернитесь назад и нажмите кнопку “Войти” или введите новый/другой номер телефона.]");
        Assert.assertEquals(response.jsonPath().getString("details.path"), "[phone]");


        System.out.println("Телефонный номер уже есть в системе: "+ response.jsonPath().getString("details.message"));
    }

}
