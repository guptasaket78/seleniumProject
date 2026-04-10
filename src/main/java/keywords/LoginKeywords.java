package keywords;

import pages.Login;
import utils.ConfigReader;

public class LoginKeywords extends BaseKeywords {

    private final Login loginPage;

    public LoginKeywords(Login loginPage) {
        this.loginPage = loginPage;
    }

    public void openLoginPage() {
        step("Open the login page", () -> {
            loginPage.open();
            verifyTrue(loginPage.isLoginFormVisible(), "Expected the login form to be visible.");
        });
    }

    public void loginWithRegisteredUser(String userProfile) {
        step("Login with registered user profile: " + userProfile, () -> {
            loginPage.loginAs(
                    ConfigReader.getUsername(userProfile),
                    ConfigReader.getPassword(userProfile)
            );

            verifyTrue(loginPage.isLoggedIn(), "Expected the user to be logged in.");
        });
    }
}
