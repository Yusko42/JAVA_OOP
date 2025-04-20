package ru.nsu.calculator.exceptions;

public class OperatorException extends CalculatorException{
    public OperatorException(String message){
        super(message);
    }
    public OperatorException(String message, Throwable cause) {
        super(message, cause);
    }
}

