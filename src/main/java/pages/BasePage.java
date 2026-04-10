package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

public abstract class BasePage {

    protected final WebDriver driver;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
    }

    protected WebElement findVisible(By locator) {
        return WaitUtils.waitForVisible(driver, locator);
    }

    protected WebElement findClickable(By locator) {
        return WaitUtils.waitForClickable(driver, locator);
    }

    protected void type(By locator, String text) {
        WebElement element = findVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected void click(By locator) {
        findClickable(locator).click();
    }

    protected boolean isVisible(By locator) {
        return findVisible(locator).isDisplayed();
    }
}
