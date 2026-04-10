package utils;

import org.openqa.selenium.WebDriver;

public class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER_HOLDER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static void setDriver(WebDriver driver) {
        DRIVER_HOLDER.set(driver);
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER_HOLDER.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver is not initialized for the current thread.");
        }
        return driver;
    }

    public static void unload() {
        DRIVER_HOLDER.remove();
    }
}
