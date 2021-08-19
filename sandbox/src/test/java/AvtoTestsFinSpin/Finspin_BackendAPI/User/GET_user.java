
package AvtoTestsFinSpin.Finspin_BackendAPI.User;


        import io.restassured.RestAssured;
        import io.restassured.response.Response;
        import org.junit.Test;
        import org.testng.Assert;


public class GET_user extends auth_green_branch{



    @Test
    //Возможность создания новой заявки для не авторизованного пользователя
    public void userWithoutAuthorization() {

        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        Response response = RestAssured.given()
                .headers("Cookie", cookieWithoutAuthorization).
                        when().get("/user").
                        then().statusCode(401).extract().response();
        Assert.assertEquals(response.jsonPath().getString("errors"), "Вам не разрешено производить данное действие.");
        System.out.println("Возможность создания новой заявки для не авторизованного пользователя: " + response.jsonPath().getString("errors"));
    }

    @Test
    //Возможность создания новой заявки для авторизованного пользователя
    public void userWithAuthorization() {

        RestAssured.baseURI = "https://api.test2.finspin.ru/v3";
        Response response = RestAssured.given()
                .headers("Cookie", cookieWithAuthorization).
                        when().get("/user").
                        then().statusCode(200).extract().response();
        Assert.assertEquals(response.jsonPath().getString("canCreateNewLoan"), "true");
        Assert.assertEquals(response.jsonPath().getString("canCreateNewLoanAfter"), null);
        Assert.assertEquals(response.jsonPath().getString("phone").length(), 10);
        Assert.assertEquals(response.jsonPath().getString("emptyArchiveLoans"), "true");
        Assert.assertEquals(response.jsonPath().getString("canCreateNewCreditCard"), "true");

        System.out.println("Возможность создания новой заявки для авторизованного пользователя: \ncanCreateNewLoan->" + response.jsonPath().getString("canCreateNewLoan")+"\ncanCreateNewCreditCard->"+response.jsonPath().getString("canCreateNewCreditCard"));
    }


}


