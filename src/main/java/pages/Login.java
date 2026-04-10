package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.ConfigReader;
import utils.WaitUtils;

public class Login {

    private final WebDriver driver;

    private final By loginHeader = By.xpath("//h2[normalize-space()='Login to your account']");
    private final By emailInput = By.cssSelector("form[action='/login'] input[data-qa='login-email']");
    private final By passwordInput = By.cssSelector("form[action='/login'] input[data-qa='login-password']");
    private final By loginButton = By.cssSelector("form[action='/login'] button[data-qa='login-button']");
    private final By loggedInAsBanner = By.xpath("//a[contains(.,'Logged in as')]");

    public Login(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get(ConfigReader.getBaseUrl() + "/login");
    }

    public boolean isLoginFormVisible() {
        return WaitUtils.waitForVisible(driver, loginHeader).isDisplayed();
    }

    public void loginAs(String email, String password) {
        WebElement emailField = WaitUtils.waitForVisible(driver, emailInput);
        emailField.clear();
        emailField.sendKeys(email);

        WebElement passwordField = WaitUtils.waitForVisible(driver, passwordInput);
        passwordField.clear();
        passwordField.sendKeys(password);

        WaitUtils.waitForClickable(driver, loginButton).click();
    }

    public boolean isLoggedIn() {
        return WaitUtils.waitForVisible(driver, loggedInAsBanner).isDisplayed();
    }

    public String getLoggedInBannerText() {
        return WaitUtils.waitForVisible(driver, loggedInAsBanner).getText();
    }
}
