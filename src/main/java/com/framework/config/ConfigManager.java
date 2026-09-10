package com.framework.config;

import com.framework.constants.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Singleton ConfigManager.
 * Loads config-{env}.properties based on the 'env' system property.
 * Falls back to system environment variables so CI/CD secrets work.
 *
 * Usage: ConfigManager.get("app.url")
 */
public class ConfigManager {

    private static final Logger log = LogManager.getLogger(ConfigManager.class);
    private static ConfigManager instance;
    private final Properties properties = new Properties();

    private ConfigManager() {
        String env = System.getProperty("env", "qa");
        String configFile = FrameworkConstants.CONFIG_DIR + "config-" + env + ".properties";
        log.info("Loading config: {}", configFile);
        try (FileInputStream fis = new FileInputStream(configFile)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load config file: " + configFile, e);
        }
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    /**
     * Returns property value.
     * Priority: System property > ENV variable > config file.
     * Treats unresolved "${...}" placeholders as absent (defensive — in case
     * someone pastes a placeholder into a properties file by mistake).
     */
    public String get(String key) {
        // 1. System property (-Dkey=value)
        String value = System.getProperty(key);
        if (isUsable(value)) return value.trim();

        // 2. Environment variable (CI secrets)
        value = System.getenv(key.toUpperCase().replace(".", "_"));
        if (isUsable(value)) return value.trim();

        // 3. Config file
        value = properties.getProperty(key);
        if (isUsable(value)) return value.trim();

        log.warn("Property '{}' not found — returning empty string", key);
        return "";
    }

    private boolean isUsable(String value) {
        if (value == null || value.isBlank()) return false;
        String trimmed = value.trim();
        // Guard against unresolved placeholder syntax like ${SOME_VAR}
        return !(trimmed.startsWith("${") && trimmed.endsWith("}"));
    }

    public int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }
}
