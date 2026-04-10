package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

public class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver createDriver() {
        WebDriver driver;
        String browser = ConfigReader.getBrowser().toLowerCase();
        boolean headless = shouldRunHeadless();

        switch (browser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("-headless");
                }
                driver = new FirefoxDriver(firefoxOptions);
                break;
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-gpu");
                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--window-size=1920,1080");
                }
                ChromeDriverService chromeService = new ChromeDriverService.Builder()
                        .usingAnyFreePort()
                        .build();
                driver = new ChromeDriver(chromeService, chromeOptions);
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + ConfigReader.getBrowser());
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getPageLoadTimeout()));
        return driver;
    }

    private static boolean shouldRunHeadless() {
        if (ConfigReader.isHeadless()) {
            return true;
        }

        String ci = System.getenv("CI");
        return ci != null && "true".equalsIgnoreCase(ci);
    }
}
