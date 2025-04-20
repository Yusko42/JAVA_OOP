package ru.nsu.calculator.operators;

import ru.nsu.calculator.ExecutionContext.ExecutionContext;
import ru.nsu.calculator.exceptions.DivisionByZeroException;
import ru.nsu.calculator.exceptions.StackUnderFlowException;

import java.util.EmptyStackException;

public class DivisionOperator implements Operator{
    public void execute(ExecutionContext context){
        try {
            Double a, b, result;
            b = context.pop();
            if (b == 0)
                throw new DivisionByZeroException("Division by zero.");
            a = context.pop();
            result = a / b;
            context.push(result);
        } catch (EmptyStackException e) {
            throw new StackUnderFlowException("Not enough elements in the stack.");
        }
    }
}
