package keywords;

import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.ConfigReader;
import utils.FrameworkAssertions;
import utils.WaitUtils;

public abstract class BaseKeywords {

    protected final WebDriver driver;

    protected BaseKeywords(WebDriver driver) {
        this.driver = driver;
    }

    protected void step(String message, Runnable action) {
        Allure.step(message, action::run);
    }

    protected void verifyTrue(boolean condition, String message) {
        FrameworkAssertions.assertTrue(condition, message);
    }

    protected void open(String path) {
        driver.get(ConfigReader.getBaseUrl() + path);
    }

    protected boolean isVisible(By locator) {
        return findVisible(locator).isDisplayed();
    }

    protected String getText(By locator) {
        return findVisible(locator).getText();
    }

    protected void type(By locator, String text) {
        WebElement element = findVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected void click(By locator) {
        WaitUtils.waitForClickable(driver, locator).click();
    }

    private WebElement findVisible(By locator) {
        return WaitUtils.waitForVisible(driver, locator);
    }
}
