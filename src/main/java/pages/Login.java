package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

public class Login {

    public final String path = "/login";
    private final WebDriver driver;

    public Login(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement loginHeader() {
        By element = By.xpath("//h2[normalize-space()='Login to your account']");
        return WaitUtils.waitForVisible(driver, element);
    }

    public WebElement emailInput() {
        By element = By.cssSelector("form[action='/login'] input[data-qa='login-email']");
        return WaitUtils.waitForVisible(driver, element);
    }

    public WebElement passwordInput() {
        By element = By.cssSelector("form[action='/login'] input[data-qa='login-password']");
        return WaitUtils.waitForVisible(driver, element);
    }

    public WebElement loginButton() {
        By element = By.cssSelector("form[action='/login'] button[data-qa='login-button']");
        return WaitUtils.waitForClickable(driver, element);
    }

    public WebElement loggedInAsBanner() {
        By element = By.xpath("//a[contains(.,'Logged in as')]");
        return WaitUtils.waitForVisible(driver, element);
    }
}
