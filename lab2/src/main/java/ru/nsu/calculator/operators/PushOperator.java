package ru.nsu.calculator.operators;

import ru.nsu.calculator.ExecutionContext.ExecutionContext;
import ru.nsu.calculator.exceptions.InvalidArgumentException;
import ru.nsu.calculator.exceptions.UndefinedVariableException;

import java.util.List;

public class PushOperator implements Operator{
    private String argument; //number or name of the variable

    @Override
    public void setArguments(List<String> args){
        if (args.size() != 1)
            throw new InvalidArgumentException("PUSH expects 1 argument: name of a variable or value.");
        try {
            this.argument = args.get(0);
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException("The argument must be a float number");
        }
    }

    public void execute(ExecutionContext context){
        try {
            Double value;
            if (context.containsVariable(argument))
                value = context.getVariable(argument);
            else
                value = Double.valueOf(argument);
            context.push(value);
        } catch (NumberFormatException e){
            throw new UndefinedVariableException("Variable " + argument + " is absent in the variable list.");
        }
    }
}
