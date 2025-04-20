package ru.nsu.calculator.exceptions;

public class ConfigurationException extends CalculatorException{
    public ConfigurationException(String message){
        super(message);
    }
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
