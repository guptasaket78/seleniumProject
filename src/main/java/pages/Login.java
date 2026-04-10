package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.ConfigReader;

public class Login extends BasePage {

    private final By loginHeader = By.xpath("//h2[normalize-space()='Login to your account']");
    private final By emailInput = By.cssSelector("form[action='/login'] input[data-qa='login-email']");
    private final By passwordInput = By.cssSelector("form[action='/login'] input[data-qa='login-password']");
    private final By loginButton = By.cssSelector("form[action='/login'] button[data-qa='login-button']");
    private final By loggedInAsBanner = By.xpath("//a[contains(.,'Logged in as')]");

    public Login(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get(ConfigReader.getBaseUrl() + "/login");
    }

    public boolean isLoginFormVisible() {
        return isVisible(loginHeader);
    }

    public void loginAs(String email, String password) {
        type(emailInput, email);
        type(passwordInput, password);
        click(loginButton);
    }

    public boolean isLoggedIn() {
        return isVisible(loggedInAsBanner);
    }

    public String getLoggedInBannerText() {
        return findVisible(loggedInAsBanner).getText();
    }
}
