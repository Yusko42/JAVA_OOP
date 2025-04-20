package ru.nsu.calculator.operators;

import ru.nsu.calculator.ExecutionContext.ExecutionContext;
import ru.nsu.calculator.exceptions.StackUnderFlowException;

import java.util.EmptyStackException;

public class MultiplicationOperator implements Operator{
    public void execute(ExecutionContext context){
        try {
            Double a, b, result;
            b = context.pop();// An exception can be thrown here
            a = context.pop();// An exception can be thrown here
            result = a * b;
            context.push(result);
        } catch (EmptyStackException e) {
            throw new StackUnderFlowException("Not enough elements in the stack.");
        }
    }
}
