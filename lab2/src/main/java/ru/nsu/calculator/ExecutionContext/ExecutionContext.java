package ru.nsu.calculator.ExecutionContext;

import java.util.*;

public class ExecutionContext {
    private final Deque<Double> stack = new ArrayDeque<>();
    private final Map<String, Double> variables = new HashMap<>();

    /// STACK METHODS
    public final void push(double value) {
        stack.push(value);
    }

    public final double pop() {
        if (stack.isEmpty()) {
            throw new EmptyStackException();
        }
        return stack.pop();
    }

    public final double peek() {
        if (stack.isEmpty()) {
            throw new EmptyStackException();
        }
        return stack.peek();
    }

    /// MAP METHODS
    public final void defineVariable(String name, Double value) {
        variables.put(name, value);
    }

    public final boolean containsVariable(String name) {
        return variables.containsKey(name);
    }

    public final Double getVariable(String name) {
        return variables.get(name);
    }

}
