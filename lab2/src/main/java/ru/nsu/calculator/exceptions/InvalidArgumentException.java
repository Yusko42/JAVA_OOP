package ru.nsu.calculator.exceptions;

public class InvalidArgumentException extends ExecutionException{
    public InvalidArgumentException(String message){
        super(message);
    }
}
