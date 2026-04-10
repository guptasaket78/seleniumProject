package tests;

import base.BaseUiTest;
import io.qameta.allure.*;
import org.testng.annotations.Test;

public class LoginTest extends BaseUiTest {

    @Epic("Authentication")
    @Feature("Login")
    @Story("Registered user login")
    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = {"smoke", "regression"}, description = "Logs in to Automation Exercise with a registered user")
    public void registeredUserCanLogIn() {
        loginKeywords.openLoginPage();
        loginKeywords.loginWithRegisteredUser("standard");
    }
}
