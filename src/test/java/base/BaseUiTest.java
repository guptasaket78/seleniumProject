package base;

import keywords.LoginKeywords;
import listeners.TestExecutionListener;
import org.openqa.selenium.WebDriver;
import org.testng.SkipException;
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
        try {
            driver = DriverFactory.createDriver();
        } catch (RuntimeException e) {
            if (isBrowserInfrastructureFailure(e)) {
                throw new SkipException("Skipping UI test because browser startup is unavailable: " + e.getMessage(), e);
            }
            throw e;
        }
        DriverManager.setDriver(driver);
        loginPage = new Login(driver);
        loginKeywords = new LoginKeywords(driver, loginPage);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        DriverManager.unload();
    }

    private boolean isBrowserInfrastructureFailure(RuntimeException exception) {
        Throwable current = exception;
        while (current != null) {
            String message = current.getMessage();
            if (message != null) {
                String normalized = message.toLowerCase();
                if (normalized.contains("unable to find a free port")
                        || normalized.contains("cannot find chrome binary")
                        || normalized.contains("chrome binary")
                        || normalized.contains("failed to create driver")
                        || normalized.contains("session not created")
                        || normalized.contains("this version of chromedriver only supports chrome version")) {
                    return true;
                }
            }
            current = current.getCause();
        }
        return false;
    }
}
