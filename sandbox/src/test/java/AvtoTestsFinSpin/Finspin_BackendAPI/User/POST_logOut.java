package AvtoTestsFinSpin.Finspin_BackendAPI.User;


        import io.restassured.RestAssured;
        import io.restassured.http.ContentType;
        import io.restassured.response.Response;
        import org.testng.Assert;
        import org.junit.Test;


public class POST_logOut extends auth_green_branch{

    @Test
    //разлогирование не залогинованнного пользователя
    public void logOutWithoutAuthorization() {
        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";


        Response response = RestAssured.given()
                .headers("Content-Type", ContentType)
                .headers("Cookie", cookieWithoutAuthorization).
                        when().post("/logOut").
                        then().statusCode(401).extract().response();
        Assert.assertEquals(response.jsonPath().getString("errors"), "Вам не разрешено производить данное действие.");

        System.out.println("разлогирование не залогинованнного пользователя: " + response.jsonPath().getString("errors"));
    }


    @Test
    //разлогирование залогинованнного пользователя
    public void logOutWithAuthorization() {
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




}
