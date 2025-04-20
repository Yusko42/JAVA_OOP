package ru.nsu.calculator.exceptions;

public class OperatorLoadException extends OperatorException{
    public OperatorLoadException(String message) {
        super(message);
    }
    public OperatorLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
