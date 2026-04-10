package config;

import java.util.Map;

public class FrameworkConfig {

    private ExecutionConfig execution;
    private TimeoutConfig timeouts;
    private String defaultEnvironment;
    private Map<String, EnvironmentConfig> environments;
    private Map<String, UserConfig> users;

    public ExecutionConfig getExecution() {
        return execution;
    }

    public TimeoutConfig getTimeouts() {
        return timeouts;
    }

    public String getDefaultEnvironment() {
        return defaultEnvironment;
    }

    public Map<String, EnvironmentConfig> getEnvironments() {
        return environments;
    }

    public Map<String, UserConfig> getUsers() {
        return users;
    }

    public static class ExecutionConfig {
        private String browser;
        private boolean headless;

        public String getBrowser() {
            return browser;
        }

        public boolean isHeadless() {
            return headless;
        }
    }

    public static class TimeoutConfig {
        private int implicit;
        private int explicit;
        private int pageLoad;

        public int getImplicit() {
            return implicit;
        }

        public int getExplicit() {
            return explicit;
        }

        public int getPageLoad() {
            return pageLoad;
        }
    }

    public static class EnvironmentConfig {
        private String baseUrl;

        public String getBaseUrl() {
            return baseUrl;
        }
    }

    public static class UserConfig {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}
