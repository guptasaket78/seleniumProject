package pages;

import org.openqa.selenium.By;
public class Login {

    public final String path = "/login";
    public final By loginHeader = By.xpath("//h2[normalize-space()='Login to your account']");
    public final By emailInput = By.cssSelector("form[action='/login'] input[data-qa='login-email']");
    public final By passwordInput = By.cssSelector("form[action='/login'] input[data-qa='login-password']");
    public final By loginButton = By.cssSelector("form[action='/login'] button[data-qa='login-button']");
    public final By loggedInAsBanner = By.xpath("//a[contains(.,'Logged in as')]");
}
