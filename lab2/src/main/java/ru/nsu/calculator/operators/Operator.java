package ru.nsu.calculator.operators;

import ru.nsu.calculator.ExecutionContext.ExecutionContext;
import ru.nsu.calculator.exceptions.InvalidArgumentException;

import java.util.List;

public interface Operator {

    void execute(ExecutionContext context);
    default void setArguments(List<String> args){
        if (!args.isEmpty()){
            throw new InvalidArgumentException("This operator doesn't need arguments");
        }
    }
}
