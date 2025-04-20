package ru.nsu.calculator.exceptions;

public class ConfigurationLoadException extends ConfigurationException {
    public ConfigurationLoadException(String message) {
        super(message);
    }

    public ConfigurationLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
