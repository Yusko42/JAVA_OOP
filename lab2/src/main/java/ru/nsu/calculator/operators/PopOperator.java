package ru.nsu.calculator.operators;

import ru.nsu.calculator.ExecutionContext.ExecutionContext;
import ru.nsu.calculator.exceptions.StackUnderFlowException;

import java.util.EmptyStackException;

public class PopOperator implements Operator {
    public void execute(ExecutionContext context){
        try {
            context.pop();
        } catch (EmptyStackException e) {
            throw new StackUnderFlowException("Stack is empty.");
        }
    }
}
