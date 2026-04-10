package keywords;

import pages.Login;
import utils.ConfigReader;

public class LoginKeywords {

    private final Login loginPage;

    public LoginKeywords(Login loginPage) {
        this.loginPage = loginPage;
    }

    public void openLoginPage() {
        loginPage.open();
        if (!loginPage.isLoginFormVisible()) {
            throw new IllegalStateException("Expected the login form to be visible.");
        }
    }

    public void loginWithRegisteredUser(String userProfile) {
        loginPage.loginAs(
                ConfigReader.getUsername(userProfile),
                ConfigReader.getPassword(userProfile)
        );

        if (!loginPage.isLoggedIn()) {
            throw new IllegalStateException("Expected the user to be logged in.");
        }
    }
}
