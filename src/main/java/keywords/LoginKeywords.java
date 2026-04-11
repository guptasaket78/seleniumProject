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
            verifyTrue(isVisible(loginPage.loginHeader), "Expected the login form to be visible.");
        });
    }

    public void loginWithRegisteredUser(String userProfile) {
        step("Login with registered user profile: " + userProfile, () -> {
            type(loginPage.emailInput, ConfigReader.getUsername(userProfile));
            type(loginPage.passwordInput, ConfigReader.getPassword(userProfile));
            click(loginPage.loginButton);

            verifyTrue(isVisible(loginPage.loggedInAsBanner), "Expected the user to be logged in.");
        });
    }
}
