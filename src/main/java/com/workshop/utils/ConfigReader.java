package com.workshop.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Loads src/test/resources/config.properties once and exposes it as simple
 * typed getters. Centralizing config here means tests never hardcode
 * browser choice, timeouts, etc.
 */
public class ConfigReader {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream in = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) {
                throw new RuntimeException("config.properties not found on classpath (src/test/resources)");
            }
            PROPERTIES.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    private ConfigReader() {
    }

    public static String browser() {
        return PROPERTIES.getProperty("browser", "chrome");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(PROPERTIES.getProperty("headless", "false"));
    }

    public static int explicitWaitSeconds() {
        return Integer.parseInt(PROPERTIES.getProperty("explicitWaitSeconds", "10"));
    }

    /**
     * Builds a file:// URL for a page inside /webapp so tests can run
     * against the bundled demo site with zero server setup.
     */
    public static String webappUrl(String page) {
        String projectRoot = Paths.get("").toAbsolutePath().toString();
        return Paths.get(projectRoot, "webapp", page).toUri().toString();
    }
}
