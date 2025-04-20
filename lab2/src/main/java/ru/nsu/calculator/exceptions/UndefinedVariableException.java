package ru.nsu.calculator.exceptions;

public class UndefinedVariableException extends ExecutionException{
    public UndefinedVariableException(String message){
        super(message);
    }
}
