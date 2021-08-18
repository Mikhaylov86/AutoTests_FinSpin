package AvtoTestsFinSpin.Finspin_BackendAPI.User;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import org.testng.Assert;
import org.junit.Test;


public class POST_auth {


    public static String phone() {
        int a = (int) (Math.random() * 1000000000);
        var b = "9" + a;
        return b;
    }

    public void sendVerificationCode() {

        Response response = RestAssured.given()
                .headers("Content-Type", "application/json; charset=UTF-8")
                .headers("request-date", "2005-08-15T15:52:01+00:00")
                .headers("request-token", "mjawns0woc0xnvqxnto1mjowmsswmdowmdgyruywourcn0jfn0y3mjm2rknentneney3qji2qjdg")
                .body("{\"phone\":\"" + phone() + "\", \"isNewUser\": true}").
                        when().post("/sendVerificationCode").
                        then().statusCode(200).extract().response();
        Cookie cookie = response.getDetailedCookie("JSESSIONID");
        Assert.assertEquals(response.jsonPath().getString("timeToLife"), "1200");
        Assert.assertEquals(response.jsonPath().getString("canRepeatAfter"), "30");
        Assert.assertEquals(response.jsonPath().getString("isNewUser"), "true");
        Assert.assertEquals(response.jsonPath().getString("smsWasSend"), "true");
    }


    public void auth() {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";

        Response response = RestAssured.given()
                .headers("Content-Type", ContentType.JSON)
                .body("{\"processDataAgreement\": true,\"receiveInfoAgreement\": true,\"creditHistoryRequestAgreement\": true,\"usePersonalSignAgreement\": true,\"code\": \"1111\",\"host\": \"test2.finspin.ru\",\"timezone\": \"+03:00\"}").
                        when().post("/auth").
                        then().statusCode(200).extract().response();



    }





    @Test
    //Отправка запроса с ошибкой в теле (Синтаксическая ошибка: лишняя запятая)
    public void authSyntaxError() {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        Response response = RestAssured.given()
                .headers("Content-Type", ContentType.JSON)
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
                .headers("Content-Type", ContentType.JSON)
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
                .headers("Content-Type", ContentType.JSON)
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
                .headers("Content-Type", ContentType.JSON)
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
                .headers("Content-Type", ContentType.JSON)
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
                .headers("Content-Type", ContentType.JSON)
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
                .headers("Content-Type", ContentType.JSON)
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
                .headers("Content-Type", ContentType.JSON)
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
                .headers("Content-Type", ContentType.JSON)
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
                .headers("Content-Type", ContentType.JSON)
                .body("{\"processDataAgreement\": true, \"receiveInfoAgreement\": true, \"creditHistoryRequestAgreement\": true, \"usePersonalSignAgreement\": true, \"code\": \"1111\", \"host\": \"test2.finspin.ru\", \"timezone\": \"12345\"}").
                        when().post("/auth").
                        then().statusCode(400).extract().response();
        Assert.assertEquals(response.jsonPath().getString("message"), null);
        Assert.assertEquals(response.jsonPath().getString("details.path"), "[timezone]");
        Assert.assertEquals(response.jsonPath().getString("details.message"), "[Значение «Timezone» неверно.]");
        System.out.println("Отправка запроса с неправильным типом в 1 поле (timezone): " + response.jsonPath().getString("details.message"));

    }


    @Test
    //Отправка запроса со всеми полями (Код отправлен)
    public void authOllFields() throws InterruptedException {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";

        Response response = RestAssured.given()
                .headers("Content-Type", "application/json; charset=UTF-8")
                .headers("request-date", "2005-08-15T15:52:01+00:00")
                .headers("request-token", "mjawns0woc0xnvqxnto1mjowmsswmdowmdgyruywourcn0jfn0y3mjm2rknentneney3qji2qjdg")
                .headers("Cookie","_csrf-frontend=qAMKkfCGopQtu_mnffYcHYLwpGobHKbn; advanced-frontend=lrrgbu45gg5m0gpr2tue3jnqjg")
                .body("{\"phone\":\"" + phone() + "\", \"isNewUser\": true}").
                        when().post("/sendVerificationCode").
                        then().statusCode(200).extract().response();

        Assert.assertEquals(response.jsonPath().getString("timeToLife"), "1200");
        Assert.assertEquals(response.jsonPath().getString("canRepeatAfter"), "30");
        Assert.assertEquals(response.jsonPath().getString("isNewUser"), "true");
        Assert.assertEquals(response.jsonPath().getString("smsWasSend"), "true");

        Response response2 = RestAssured.given()
                .headers("Content-Type", ContentType.JSON)
                .headers("Cookie","_csrf-frontend=qAMKkfCGopQtu_mnffYcHYLwpGobHKbn; advanced-frontend=lrrgbu45gg5m0gpr2tue3jnqjg")
                .body("{\"processDataAgreement\": true,\"receiveInfoAgreement\": true,\"creditHistoryRequestAgreement\": true,\"usePersonalSignAgreement\": true,\"code\": \"1111\",\"host\": \"test2.finspin.ru\",\"timezone\": \"+03:00\"}").
                        when().post("/auth").
                        then().statusCode(200).extract().response();

        Response response3 = RestAssured.given()
                .headers("Content-Type", ContentType.JSON)
                .headers("Cookie","_csrf-frontend=qAMKkfCGopQtu_mnffYcHYLwpGobHKbn; advanced-frontend=lrrgbu45gg5m0gpr2tue3jnqjg").
                        when().post("/logOut").
                        then().statusCode(200).extract().response();
        Assert.assertEquals(response3.jsonPath().getString("isAuth"), false);
        Assert.assertEquals(response3.jsonPath().getString("logout"), true);
        Assert.assertEquals(response3.jsonPath().getString("id").length(), 7);



    }





}
