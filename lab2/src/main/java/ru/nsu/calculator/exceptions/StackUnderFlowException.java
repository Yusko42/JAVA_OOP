package ru.nsu.calculator.exceptions;

public class StackUnderFlowException extends ExecutionException{
    public StackUnderFlowException(String message){
        super(message);
    }
}
