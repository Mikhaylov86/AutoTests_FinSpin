
package AvtoTestsFinSpin.Finspin_BackendAPI.User;

        import org.junit.Test;


public class GET_authStatus extends auth_green_branch{

    @Test
    //Проверка неавтаризованного пользователя
    public void authStatusNotAuthUser() {
        authStatusIsFalse();
    }

    @Test
    //Проверка автаризованного пользователя
    public void authStatusAuthUser() {
        sendVerificationCode();
        authorization();
        authStatusIsTrue();
        logOut();
    }
}



