package tests;

import base.BaseUiTest;
import org.testng.annotations.Test;

public class LoginTest extends BaseUiTest {

    @Test(groups = {"smoke", "regression"}, description = "Logs in to Automation Exercise with a registered user")
    public void registeredUserCanLogIn() {
        loginKeywords.openLoginPage();
        loginKeywords.loginWithRegisteredUser("standard");
    }
}
