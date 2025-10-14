package com.automation.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration reader utility for reading properties from config files
 */
public class ConfigReader {

    private static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);
    private static ConfigReader instance;
    private Properties properties;

    private ConfigReader() {
        loadProperties();
    }

    /**
     * Get singleton instance of ConfigReader
     *
     * @return ConfigReader instance
     */
    public static ConfigReader getInstance() {
        if (instance == null) {
            synchronized (ConfigReader.class) {
                if (instance == null) {
                    instance = new ConfigReader();
                }
            }
        }
        return instance;
    }

    /**
     * Load properties from config.properties file
     */
    private void loadProperties() {
        properties = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
                logger.info("Configuration properties loaded successfully");
            } else {
                logger.warn("config.properties file not found, using system properties and defaults");
            }
        } catch (IOException e) {
            logger.error("Error loading configuration properties", e);
        }
    }

    /**
     * Get property value by key
     *
     * @param key Property key
     * @return Property value or null if not found
     */
    public String getProperty(String key) {
        // First check system properties, then config file properties
        String value = System.getProperty(key);
        if (value == null) {
            value = properties.getProperty(key);
        }
        return value;
    }

    /**
     * Get property value by key with default value
     *
     * @param key          Property key
     * @param defaultValue Default value if property not found
     * @return Property value or default value
     */
    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Get boolean property value
     *
     * @param key          Property key
     * @param defaultValue Default value if property not found
     * @return Boolean property value
     */
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    /**
     * Get integer property value
     *
     * @param key          Property key
     * @param defaultValue Default value if property not found
     * @return Integer property value
     */
    public int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        try {
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer value for property {}: {}, using default: {}", key, value, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Get long property value
     *
     * @param key          Property key
     * @param defaultValue Default value if property not found
     * @return Long property value
     */
    public long getLongProperty(String key, long defaultValue) {
        String value = getProperty(key);
        try {
            return value != null ? Long.parseLong(value) : defaultValue;
        } catch (NumberFormatException e) {
            logger.warn("Invalid long value for property {}: {}, using default: {}", key, value, defaultValue);
            return defaultValue;
        }
    }
}