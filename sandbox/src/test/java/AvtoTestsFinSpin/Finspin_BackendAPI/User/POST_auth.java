package AvtoTestsFinSpin.Finspin_BackendAPI.User;


import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.junit.Test;


public class POST_auth extends auth_green_branch{

    String ContentType = "application/json; charset=UTF-8";
    String requestDate = "2005-08-15T15:52:01+00:00";
    String requestToken = "mjawns0woc0xnvqxnto1mjowmsswmdowmdgyruywourcn0jfn0y3mjm2rknentneney3qji2qjdg";
    String cookieWithoutAuthorization = "_csrf-frontend=u6M91LPw_6KwYlCStQe1z36Y_3V6ZSGw; advanced-frontend=jmbagra6ahp6nvbkfloaunqak2";
    String cookieWithAuthorization = "_csrf-frontend=DwCK8LVxNNifzdOmjLwwQCsKPdPOsg96; _identity-frontend=%5B1389606%2C%22hX01t82QvvPegqHbwBTsZXyd9Z7yvZYR%22%2C2592000%5D; advanced-frontend=jq22irqll1hu7u7nimj2jilvh0";

    public static String phone() {
        int a = (int) (Math.random() * 1000000000);
        var b = "9" + a;
        return b;
    }


    public void auth(boolean processDataAgreement, boolean receiveInfoAgreement, boolean creditHistoryRequestAgreement, boolean usePersonalSignAgreement, String code, String host, String timezone, int statusCode) {

        Response response = RestAssured.given()
                .headers("Content-Type", ContentType)
                .headers("Cookie", cookieWithoutAuthorization)
                .body("{" +
                        "\"processDataAgreement\": " + processDataAgreement + "," +
                        "\"receiveInfoAgreement\": " + receiveInfoAgreement + "," +
                        "\"creditHistoryRequestAgreement\": " + creditHistoryRequestAgreement + "," +
                        "\"usePersonalSignAgreement\": " + usePersonalSignAgreement + "," +
                        "\"code\":\"" + code + "\"," +
                        "\"host\":\"" + host + "\"," +
                        "\"timezone\":\"" + timezone + "\"" +
                        "}").
                        when().post("/auth").
                        then().statusCode(statusCode).extract().response();
        System.out.println("Авторизация прошла успешно");

    }

    public void sendVerificationCode() {

        Response response = RestAssured.given()
                .headers("Content-Type", ContentType)
                .headers("request-date", requestDate)
                .headers("request-token", requestToken)
                .headers("Cookie", cookieWithoutAuthorization)
                .body("{\"phone\":\"" + phone() + "\", \"isNewUser\": true}").
                        when().post("/sendVerificationCode").
                        then().statusCode(200).extract().response();
        Assert.assertEquals(response.jsonPath().getString("timeToLife"), "1200");
        Assert.assertEquals(response.jsonPath().getString("canRepeatAfter"), "30");
        Assert.assertEquals(response.jsonPath().getString("isNewUser"), "true");
        Assert.assertEquals(response.jsonPath().getString("smsWasSend"), "true");

        System.out.println("Код отправлен: " + response.jsonPath().getString("smsWasSend"));
    }

    public void logOut() {
        Response response = RestAssured.given()
                .headers("Content-Type", ContentType)
                .headers("Cookie", cookieWithAuthorization).
                        when().post("/logOut").
                        then().statusCode(200).extract().response();
        Assert.assertEquals(response.jsonPath().getString("isAuth"), "false");
        Assert.assertEquals(response.jsonPath().getString("logout"), "true");

        System.out.println("Пользователь разлогирован: " + response.jsonPath().getString("logout"));
    }


    @Test
    //Отправка запроса с ошибкой в теле (Синтаксическая ошибка: лишняя запятая)
    public void authSyntaxError() {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        Response response = RestAssured.given()
                .headers("Content-Type", ContentType)
                .body("{\"code\":\"1111\",").
                        when().post("/auth").
                        then().statusCode(400).extract().response();
        Assert.assertFalse(response.jsonPath().getString("errors").isEmpty());
        Assert.assertEquals(response.jsonPath().getString("errors"), "Invalid JSON data in request body: Syntax error.");
        System.out.println("Синтаксическая ошибка: лишняя запятая в теле запроса: " + response.jsonPath().getString("errors"));

    }

    @Test
    //Отправка запроса без обязательного поля (code)
    public void authWithoutCode() {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        Response response = RestAssured.given()
                .headers("Content-Type", ContentType)
                .body("{\"processDataAgreement\": true, \"receiveInfoAgreement\": true, \"creditHistoryRequestAgreement\": true, \"usePersonalSignAgreement\": true, \"host\": \"test2.finspin.ru\", \"timezone\": \"+03:00\"}").
                        when().post("/auth").
                        then().statusCode(400).extract().response();
        Assert.assertEquals(response.jsonPath().getString("message"), null);
        Assert.assertEquals(response.jsonPath().getString("details.path"), "[code]");
        Assert.assertEquals(response.jsonPath().getString("details.message"), "[Необходимо заполнить «Code».]");
        System.out.println("Отправка запроса без обязательного поля (code): " + response.jsonPath().getString("details.message"));

    }

    @Test
    //Отправка запроса с неправильным типом в 1 поле (processDataAgreement)
    public void authProcessDataAgreementNotСorrect() {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        Response response = RestAssured.given()
                .headers("Content-Type", ContentType)
                .body("{\"processDataAgreement\": 1111, \"receiveInfoAgreement\": true, \"creditHistoryRequestAgreement\": true, \"usePersonalSignAgreement\": true, \"code\": \"1111\", \"host\": \"test2.finspin.ru\", \"timezone\": \"+03:00\"}").
                        when().post("/auth").
                        then().statusCode(400).extract().response();
        Assert.assertEquals(response.jsonPath().getString("message"), null);
        Assert.assertEquals(response.jsonPath().getString("details.path"), "[processDataAgreement]");
        Assert.assertEquals(response.jsonPath().getString("details.message"), "[Значение «Process Data Agreement» должно быть равно «1» или «0».]");
        System.out.println("Отправка запроса с неправильным типом в 1 поле (processDataAgreement): " + response.jsonPath().getString("details.message"));

    }

    @Test
    //Отправка запроса с неправильным типом в 1 поле (receiveInfoAgreement)
    public void authReceiveInfoAgreementNotСorrect() {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        Response response = RestAssured.given()
                .headers("Content-Type", ContentType)
                .body("{\"processDataAgreement\": true, \"receiveInfoAgreement\": \"true\", \"creditHistoryRequestAgreement\": true, \"usePersonalSignAgreement\": true, \"code\": \"1111\", \"host\": \"test2.finspin.ru\", \"timezone\": \"+03:00\"}").
                        when().post("/auth").
                        then().statusCode(400).extract().response();
        Assert.assertEquals(response.jsonPath().getString("message"), null);
        Assert.assertEquals(response.jsonPath().getString("details.path"), "[receiveInfoAgreement]");
        Assert.assertEquals(response.jsonPath().getString("details.message"), "[Значение «Receive Info Agreement» должно быть равно «1» или «0».]");
        System.out.println("Отправка запроса с неправильным типом в 1 поле (receiveInfoAgreement): " + response.jsonPath().getString("details.message"));

    }

    @Test
    //Отправка запроса с неправильным типом в 1 поле (creditHistoryRequestAgreement)
    public void authCreditHistoryRequestAgreementNotСorrect() {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        Response response = RestAssured.given()
                .headers("Content-Type", ContentType)
                .body("{\"processDataAgreement\": true, \"receiveInfoAgreement\": true, \"creditHistoryRequestAgreement\": \"fdgfgdf\", \"usePersonalSignAgreement\": true, \"code\": \"1111\", \"host\": \"test2.finspin.ru\", \"timezone\": \"+03:00\"}").
                        when().post("/auth").
                        then().statusCode(400).extract().response();
        Assert.assertEquals(response.jsonPath().getString("message"), null);
        Assert.assertEquals(response.jsonPath().getString("details.path"), "[creditHistoryRequestAgreement]");
        Assert.assertEquals(response.jsonPath().getString("details.message"), "[Значение «Credit History Request Agreement» должно быть равно «1» или «0».]");
        System.out.println("Отправка запроса с неправильным типом в 1 поле (creditHistoryRequestAgreement): " + response.jsonPath().getString("details.message"));

    }

    @Test
    //Отправка запроса с неправильным типом в 1 поле (usePersonalSignAgreement)
    public void authUsePersonalSignAgreementNotСorrect() {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        Response response = RestAssured.given()
                .headers("Content-Type", ContentType)
                .body("{\"processDataAgreement\": true, \"receiveInfoAgreement\": true, \"creditHistoryRequestAgreement\": true, \"usePersonalSignAgreement\": \"23\", \"code\": \"1111\", \"host\": \"test2.finspin.ru\", \"timezone\": \"+03:00\"}").
                        when().post("/auth").
                        then().statusCode(400).extract().response();
        Assert.assertEquals(response.jsonPath().getString("message"), null);
        Assert.assertEquals(response.jsonPath().getString("details.path"), "[usePersonalSignAgreement]");
        Assert.assertEquals(response.jsonPath().getString("details.message"), "[Значение «Use Personal Sign Agreement» должно быть равно «1» или «0».]");
        System.out.println("Отправка запроса с неправильным типом в 1 поле (usePersonalSignAgreement): " + response.jsonPath().getString("details.message"));

    }

    @Test
    //Отправка запроса с неправильным типом в 1 поле (host)
    public void authHostNotСorrect() {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        Response response = RestAssured.given()
                .headers("Content-Type", ContentType)
                .body("{\"processDataAgreement\": true, \"receiveInfoAgreement\": true, \"creditHistoryRequestAgreement\": true, \"usePersonalSignAgreement\": true, \"code\": \"1111\", \"host\": 1234, \"timezone\": \"+03:00\"}").
                        when().post("/auth").
                        then().statusCode(400).extract().response();
        Assert.assertEquals(response.jsonPath().getString("message"), null);
        Assert.assertEquals(response.jsonPath().getString("details.path"), "[host]");
        Assert.assertEquals(response.jsonPath().getString("details.message"), "[Значение «Host» должно быть строкой.]");
        System.out.println("Отправка запроса с неправильным типом в 1 поле (host): " + response.jsonPath().getString("details.message"));

    }


    @Test
    //Отправка запроса с неправильным типом в 1 поле (timezone)
    public void authTimezoneNotСorrect() {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        Response response = RestAssured.given()
                .headers("Content-Type", ContentType)
                .body("{\"processDataAgreement\": true, \"receiveInfoAgreement\": true, \"creditHistoryRequestAgreement\": true, \"usePersonalSignAgreement\": true, \"code\": \"1111\", \"host\": \"test2.finspin.ru\", \"timezone\": 12345}").
                        when().post("/auth").
                        then().statusCode(400).extract().response();
        Assert.assertEquals(response.jsonPath().getString("message"), null);
        Assert.assertEquals(response.jsonPath().getString("details.path"), "[timezone]");
        Assert.assertEquals(response.jsonPath().getString("details.message"), "[Значение «Timezone» должно быть строкой.]");
        System.out.println("Отправка запроса с неправильным типом в 1 поле (timezone): " + response.jsonPath().getString("details.message"));

    }

    @Test
    //Отправка запроса с неправильным типом в 1 поле (host2)
    public void authHost2NotСorrect() {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        Response response = RestAssured.given()
                .headers("Content-Type", ContentType)
                .body("{\"processDataAgreement\": true, \"receiveInfoAgreement\": true, \"creditHistoryRequestAgreement\": true, \"usePersonalSignAgreement\": true, \"code\": \"1111\", \"host\": \"1234\", \"timezone\": \"+03:00\"}").
                        when().post("/auth").
                        then().statusCode(400).extract().response();
        Assert.assertEquals(response.jsonPath().getString("message"), null);
        Assert.assertEquals(response.jsonPath().getString("details.path"), "[host]");
        Assert.assertEquals(response.jsonPath().getString("details.message"), "[Значение «Host» неверно.]");
        System.out.println("Отправка запроса с неправильным типом в 1 поле (host2): " + response.jsonPath().getString("details.message"));

    }

    @Test
    //Отправка запроса с неправильным типом в 1 поле (timezone2)
    public void authTimezone2NotСorrect() {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        Response response = RestAssured.given()
                .headers("Content-Type", ContentType)
                .body("{\"processDataAgreement\": true, \"receiveInfoAgreement\": true, \"creditHistoryRequestAgreement\": true, \"usePersonalSignAgreement\": true, \"code\": \"1111\", \"host\": \"test2.finspin.ru\", \"timezone\": \"12345\"}").
                        when().post("/auth").
                        then().statusCode(400).extract().response();
        Assert.assertEquals(response.jsonPath().getString("message"), null);
        Assert.assertEquals(response.jsonPath().getString("details.path"), "[timezone]");
        Assert.assertEquals(response.jsonPath().getString("details.message"), "[Значение «Timezone» неверно.]");
        System.out.println("Отправка запроса с неправильным типом в 1 поле (timezone): " + response.jsonPath().getString("details.message"));

    }


    @Test
    //Отправка запроса со всеми полями (Код перед этим не отправлен)
    public void authNoSmsCode() {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        Response response = RestAssured.given()
                .headers("Content-Type", ContentType)
                .body("{" +
                        "\"processDataAgreement\": true, " +
                        "\"receiveInfoAgreement\": true, " +
                        "\"creditHistoryRequestAgreement\": true, " +
                        "\"usePersonalSignAgreement\": true, " +
                        "\"code\": \"1111\", " +
                        "\"host\": \"test2.finspin.ru\", " +
                        "\"timezone\": \"+03:00\"" +
                        "}").
                        when().post("/auth").
                        then().statusCode(400).extract().response();
        Assert.assertEquals(response.jsonPath().getString("message"), null);
        Assert.assertEquals(response.jsonPath().getString("details.path"), "[code]");
        Assert.assertEquals(response.jsonPath().getString("details.message"), "[Undefined session]");
        System.out.println("Отправка запроса со всеми полями (Код перед этим не отправлен): " + response.jsonPath().getString("details.message"));

    }


    @Test
    //Отправка запроса со всеми полями (Код отправлен)
    public void authOllFields1()  {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        sendVerificationCode();
        auth(true, true, true, true, "1111", "test2.finspin.ru", "+03:00", 200);
        logOut();
    }

    //TODO добавить тест с отправкой только 1 обязательнго поля code (сейчас не получается т.к. просит еще 1 поле ХОСТ. выдает ошибку "errors": "Array to string conversion"


    @Test
    //TODO почему 500 ошибка выскакивает? и сообщение "errors": "Array to string conversion" ,  задать вопрос разработчикам
    //Отправка запроса со всеми полями (Код отправлен)
    public void authOllFields2()  {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        sendVerificationCode();
        auth(false, true, true, true, "1111", "test2.finspin.ru", "+03:00", 200);
        logOut();
    }


    @Test
    //TODO почему 500 ошибка выскакивает? и сообщение "errors": "Array to string conversion" ,  задать вопрос разработчикам
    //Отправка запроса со всеми полями (Код отправлен)
    public void authOllFields3()  {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        sendVerificationCode();
        auth(true, false, true, true, "1111", "test2.finspin.ru", "+03:00", 200);
        logOut();
    }


    @Test
    //TODO почему 500 ошибка выскакивает? и сообщение "errors": "Array to string conversion" ,  задать вопрос разработчикам
    //Отправка запроса со всеми полями (Код отправлен)
    public void authOllFields4(){
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        sendVerificationCode();
        auth(true, true, false, true, "1111", "test2.finspin.ru", "+03:00", 200);
        logOut();
    }

    @Test
    //TODO почему 500 ошибка выскакивает? и сообщение "errors": "Array to string conversion" ,  задать вопрос разработчикам
    //Отправка запроса со всеми полями (Код отправлен)
    public void authOllFields5()  {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        sendVerificationCode();
        auth(true, true, true, false, "1111", "test2.finspin.ru", "+03:00", 200);
        logOut();
    }

    @Test
    //TODO почему 500 ошибка выскакивает? и сообщение "errors": "Array to string conversion" ,  задать вопрос разработчикам
    //Отправка запроса со всеми полями (Код отправлен)
    public void authOllFields6()  {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        sendVerificationCode();
        auth(false, false, false, false, "1111", "test2.finspin.ru", "+03:00", 200);
        logOut();
    }

    @Test
    //Отправка запроса с неправильным кодом (Код отправлен)
    public void authSmsCodeNotTrue()  {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        sendVerificationCode();

        Response response = RestAssured.given()
                .headers("Content-Type", ContentType)
                .headers("request-date", requestDate)
                .headers("request-token", requestToken)
                .headers("Cookie", cookieWithoutAuthorization)
                .body("{" +
                        "\"processDataAgreement\": true, " +
                        "\"receiveInfoAgreement\": true, " +
                        "\"creditHistoryRequestAgreement\": true, " +
                        "\"usePersonalSignAgreement\": true, " +
                        "\"code\": \"1112\", " +
                        "\"host\": \"test2.finspin.ru\", " +
                        "\"timezone\": \"+03:00\"" +
                        "}").
                        when().post("/auth").
                        then().statusCode(400).extract().response();
        Assert.assertEquals(response.jsonPath().getString("message"), null);
        Assert.assertEquals(response.jsonPath().getString("details.path"), "[code]");
        Assert.assertEquals(response.jsonPath().getString("details.message"), "[Неверное значение кода верификации.]");
        System.out.println("Отправка запроса с неправильным кодом: " + response.jsonPath().getString("details.message"));

    }

    @Test
    //Отправка запроса с неправильным кодом - не строкой (Код отправлен)
    public void authSmsCodeNotString()  {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        sendVerificationCode();

        Response response = RestAssured.given()
                .headers("Content-Type", ContentType)
                .headers("request-date", requestDate)
                .headers("request-token", requestToken)
                .headers("Cookie", cookieWithoutAuthorization)
                .body("{" +
                        "\"processDataAgreement\": true, " +
                        "\"receiveInfoAgreement\": true, " +
                        "\"creditHistoryRequestAgreement\": true, " +
                        "\"usePersonalSignAgreement\": true, " +
                        "\"code\": 1112, " +
                        "\"host\": \"test2.finspin.ru\", " +
                        "\"timezone\": \"+03:00\"" +
                        "}").
                        when().post("/auth").
                        then().statusCode(400).extract().response();
        Assert.assertEquals(response.jsonPath().getString("message"), null);
        Assert.assertEquals(response.jsonPath().getString("details.path"), "[code]");
        Assert.assertEquals(response.jsonPath().getString("details.message"), "[Значение «Code» должно быть строкой.]");
        System.out.println("Отправка запроса с неправильным кодом - не строкой: " + response.jsonPath().getString("details.message"));

    }



}
