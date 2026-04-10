package base;

import keywords.LoginKeywords;
import listeners.TestExecutionListener;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Listeners;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.Login;
import utils.DriverManager;
import utils.DriverFactory;

@Listeners(TestExecutionListener.class)
public abstract class BaseUiTest {

    protected WebDriver driver;
    protected Login loginPage;
    protected LoginKeywords loginKeywords;

    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.createDriver();
        DriverManager.setDriver(driver);
        loginPage = new Login(driver);
        loginKeywords = new LoginKeywords(loginPage);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        DriverManager.unload();
    }
}
