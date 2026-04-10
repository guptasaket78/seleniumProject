package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.FrameworkConfig;

import java.io.InputStream;

public class ConfigReader {

    private static final FrameworkConfig CONFIG;

    static {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = ConfigReader.class
                    .getClassLoader()
                    .getResourceAsStream("config/config.json");
            if (is == null) {
                throw new RuntimeException("config/config.json was not found on the classpath");
            }
            CONFIG = mapper.readValue(is, FrameworkConfig.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.json", e);
        }
    }

    public static String getBrowser() {
        return readOverride("browser", CONFIG.getExecution().getBrowser());
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(readOverride("headless", String.valueOf(CONFIG.getExecution().isHeadless())));
    }

    public static int getExplicitWait() {
        return CONFIG.getTimeouts().getExplicit();
    }

    public static int getImplicitWait() {
        return CONFIG.getTimeouts().getImplicit();
    }

    public static int getPageLoadTimeout() {
        return CONFIG.getTimeouts().getPageLoad();
    }

    public static String getEnvironment() {
        return readOverride("env", CONFIG.getDefaultEnvironment());
    }

    public static String getBaseUrl() {
        String baseUrlOverride = System.getProperty("baseUrl");
        if (baseUrlOverride != null && !baseUrlOverride.isBlank()) {
            return baseUrlOverride;
        }

        FrameworkConfig.EnvironmentConfig environmentConfig = CONFIG.getEnvironments().get(getEnvironment());
        if (environmentConfig == null || environmentConfig.getBaseUrl() == null || environmentConfig.getBaseUrl().isBlank()) {
            throw new IllegalStateException("No baseUrl configured for environment: " + getEnvironment());
        }
        return environmentConfig.getBaseUrl();
    }

    public static String getUsername(String userProfile) {
        FrameworkConfig.UserConfig user = CONFIG.getUsers().get(userProfile);
        if (user == null) {
            throw new IllegalArgumentException("Unknown user profile: " + userProfile);
        }
        return readOverride(userProfile + ".username", user.getUsername());
    }

    public static String getPassword(String userProfile) {
        FrameworkConfig.UserConfig user = CONFIG.getUsers().get(userProfile);
        if (user == null) {
            throw new IllegalArgumentException("Unknown user profile: " + userProfile);
        }
        return readOverride(userProfile + ".password", user.getPassword());
    }

    private static String readOverride(String key, String defaultValue) {
        String override = System.getProperty(key);
        if (override == null || override.isBlank()) {
            return defaultValue;
        }
        return override;
    }
}
