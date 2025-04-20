package ru.nsu.calculator.operators;

import ru.nsu.calculator.ExecutionContext.ExecutionContext;
import ru.nsu.calculator.exceptions.StackUnderFlowException;

import java.util.EmptyStackException;

public class SquareRootOperator implements Operator{
    public void execute(ExecutionContext context){
        try {
            double value, result;
            value = context.pop();
            result = Math.sqrt(value);
            context.push(result);
        } catch (EmptyStackException e) {
            throw new StackUnderFlowException("Not enough elements in the stack.");
        }
    }
}
