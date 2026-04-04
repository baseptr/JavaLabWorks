package com.esdc.lab1.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class AppConfig {
    private static final Properties props = new Properties();

    static {
        try (InputStream is = AppConfig.class.getClassLoader().getResourceAsStream("com/esdc/lab1/lab1.properties")) {
            if (is == null) throw new RuntimeException("lab1.properties not found on classpath");
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load lab1.properties", e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
