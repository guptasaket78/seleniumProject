package utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Map;

public class ConfigReader {

    private static Map<String, Object> config;

    static {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = ConfigReader.class
                    .getClassLoader()
                    .getResourceAsStream("config/config.json");
            if (is == null) {
                throw new RuntimeException("config/config.json was not found on the classpath");
            }
            config = mapper.readValue(is, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.json", e);
        }
    }

    public static String getBrowser() {
        return (String) config.get("browser");
    }

    public static boolean isHeadless() {
        return (Boolean) config.get("headless");
    }

    public static int getExplicitWait() {
        Map<String, Integer> timeouts = (Map<String, Integer>) config.get("timeouts");
        return timeouts.get("explicit");
    }

    public static int getImplicitWait() {
        Map<String, Integer> timeouts = (Map<String, Integer>) config.get("timeouts");
        return timeouts.get("implicit");
    }

    public static int getPageLoadTimeout() {
        Map<String, Integer> timeouts = (Map<String, Integer>) config.get("timeouts");
        return timeouts.get("pageLoad");
    }

    public static String getBaseUrl() {
        return (String) config.get("baseUrl");
    }

    public static String getUsername(String userProfile) {
        Map<String, Map<String, String>> users = (Map<String, Map<String, String>>) config.get("users");
        return users.get(userProfile).get("username");
    }

    public static String getPassword(String userProfile) {
        Map<String, Map<String, String>> users = (Map<String, Map<String, String>>) config.get("users");
        return users.get(userProfile).get("password");
    }
}
