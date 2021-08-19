package AvtoTestsFinSpin.Finspin_BackendAPI.User;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.junit.Test;


public class auth_green_branch {

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

    public void authStatusIsFalse() {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        Response response = RestAssured.given()
                .headers("Cookie", cookieWithoutAuthorization).
                        when().get("/authStatus").
                        then().statusCode(200).extract().response();
        Assert.assertFalse(response.jsonPath().getString("authorized").isEmpty());
        Assert.assertEquals(response.jsonPath().getString("authorized"), "false");
        System.out.println("Проверка статуса не авторизованного пользователя: " + response.jsonPath().getString("authorized"));
    }


    public void sendVerificationCode() {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";

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
        System.out.println("Отправка смс кода для верификации: smsWasSend->" + response.jsonPath().getString("smsWasSend"));

    }


    public void authorization()  {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        Response response = RestAssured.given()
                .headers("Content-Type", ContentType)
                .headers("Cookie", cookieWithoutAuthorization)
                .body("{\"processDataAgreement\": true,\"receiveInfoAgreement\": true,\"creditHistoryRequestAgreement\": true,\"usePersonalSignAgreement\": true,\"code\": \"1111\",\"host\": \"test2.finspin.ru\",\"timezone\": \"+03:00\"}").
                        when().post("/auth").
                        then().statusCode(200).extract().response();
        System.out.println("Авторизация прошла успешно!");
    }


    public void authStatusIsTrue() {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        Response response = RestAssured.given()
                .headers("Cookie", cookieWithAuthorization).
                        when().get("/authStatus").
                        then().statusCode(200).extract().response();
        Assert.assertFalse(response.jsonPath().getString("authorized").isEmpty());
        Assert.assertEquals(response.jsonPath().getString("authorized"), "true");
        System.out.println("Проверка статуса авторизованного пользователя: " + response.jsonPath().getString("authorized"));
    }

    public void logOut() {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";


        Response response = RestAssured.given()
                .headers("Content-Type", ContentType)
                .headers("Cookie", cookieWithAuthorization).
                        when().post("/logOut").
                        then().statusCode(200).extract().response();
        Assert.assertEquals(response.jsonPath().getString("isAuth"), "false");
        Assert.assertEquals(response.jsonPath().getString("logout"), "true");
        System.out.println("Разлогирование пользователя: logout->" + response.jsonPath().getString("logout"));
    }



        @Test
        public void auth() {
            authStatusIsFalse();
            sendVerificationCode();
            authorization();
            authStatusIsTrue();
            logOut();
        }

    }



