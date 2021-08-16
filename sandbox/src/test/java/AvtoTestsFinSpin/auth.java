package AvtoTestsFinSpin;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.*;
import org.junit.Test;


public class auth {


    @Test
    //Проверка статуса не авторизованного пользователя = false
    public void authStatusIsFalse() {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        Response response = RestAssured.given().
                when().get("/authStatus").
                then().statusCode(200).extract().response();
        Assert.assertFalse(response.jsonPath().getString("authorized").isEmpty());
        Assert.assertEquals(response.jsonPath().getString("authorized"), "false");
        System.out.println("Проверка статуса не авторизованного пользователя: "  + response.jsonPath().getString("authorized"));

    }


}
