package keywords;

import org.openqa.selenium.WebDriver;
import pages.Login;
import utils.ConfigReader;

public class LoginKeywords extends BaseKeywords {

    private final Login loginPage;

    public LoginKeywords(WebDriver driver, Login loginPage) {
        super(driver);
        this.loginPage = loginPage;
    }

    public void openLoginPage() {
        step("Open the login page", () -> {
            open(loginPage.path);
            verifyTrue(loginPage.loginHeader().isDisplayed(), "Expected the login form to be visible.");
        });
    }

    public void loginWithRegisteredUser(String userProfile) {
        step("Login with registered user profile: " + userProfile, () -> {
            loginPage.emailInput().clear();
            loginPage.emailInput().sendKeys(ConfigReader.getUsername(userProfile));
            loginPage.passwordInput().clear();
            loginPage.passwordInput().sendKeys(ConfigReader.getPassword(userProfile));
            loginPage.loginButton().click();

            verifyTrue(loginPage.loggedInAsBanner().isDisplayed(), "Expected the user to be logged in.");
        });
    }
}
