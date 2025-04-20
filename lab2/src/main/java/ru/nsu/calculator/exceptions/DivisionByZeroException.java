package ru.nsu.calculator.exceptions;

public class DivisionByZeroException extends ExecutionException{
    public DivisionByZeroException(String message){
        super(message);
    }
}
