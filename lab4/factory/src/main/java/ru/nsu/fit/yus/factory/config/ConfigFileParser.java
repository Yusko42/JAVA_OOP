package ru.nsu.fit.yus.factory.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigFileParser {
    public static Properties loadConfigData(String path) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(path));
            return properties;
        } catch (IOException e) {
            throw new IllegalArgumentException("Error loading configuration: " + e.getMessage());
        }
    }
}
