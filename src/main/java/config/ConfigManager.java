package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {

    private static Properties props = new Properties();

    static {
        try {
            // Load properties file once during class loading
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            props.load(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load config.properties file.");
        }
    }

    /**
     * Get the value for key from environment variable if exists,
     * otherwise from config.properties file.
     *
     * @param key Key for the config property or environment variable name
     * @return Value corresponding to the key or null if not found
     */
    public static String getProperty(String key) {
        // Environment variable overrides config file property
        String envValue = System.getenv(key);
        if (envValue != null && !envValue.isEmpty()) {
            return envValue;
        }
        return props.getProperty(key);
    }

    /**
     * Convenience method to get the base URL property.
     *
     * @return Base URL string as found in config or environment
     */
    public static String getBaseUrl() {
        String baseUrl = getProperty("base.url");
        if (baseUrl == null || baseUrl.isEmpty()) {
            throw new RuntimeException("Property base.url not set in config.properties or environment variables");
        }
        return baseUrl;
    }
}