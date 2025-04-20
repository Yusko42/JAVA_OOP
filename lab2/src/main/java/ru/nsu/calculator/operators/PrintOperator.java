package ru.nsu.calculator.operators;
import ru.nsu.calculator.ExecutionContext.ExecutionContext;
import ru.nsu.calculator.exceptions.StackUnderFlowException;

import java.util.EmptyStackException;

public class PrintOperator implements Operator{
    public void execute(ExecutionContext context){
        try {
            System.out.println(context.peek());
        } catch (EmptyStackException e) {
            throw new StackUnderFlowException("Stack is empty.");
        }
    }
}
