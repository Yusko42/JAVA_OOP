package ru.nsu.calculator.operators;

import ru.nsu.calculator.ExecutionContext.ExecutionContext;
import ru.nsu.calculator.exceptions.InvalidArgumentException;

import java.util.List;

//Интересно, а стоит ли объявлять наследников как final?
//IMPORTANT: check if the name contains only NUMBERS (should I stop the execution?)

///Fills the list with defined arguments
public class DefineOperator implements Operator{
    private String name;
    private Double value;

    @Override
    public void setArguments(List<String> args){
        if (args.size() != 2)
            throw new InvalidArgumentException("DEFINE expects 2 arguments: name and value.");
        try {
            this.name = args.get(0);
            if (!isValidVariableName(this.name))
                throw new InvalidArgumentException("First argument cannot be a float number.");
            this.value = Double.parseDouble(args.get(1));
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException("Second argument must be a float number");
        }
    }

    public void execute(ExecutionContext context){
        context.defineVariable(name, value);
    }

    private static boolean isValidVariableName(String name) {
        try {
            Double.parseDouble(name);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }
}
