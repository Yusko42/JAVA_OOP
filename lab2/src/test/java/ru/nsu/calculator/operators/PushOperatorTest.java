package ru.nsu.calculator.operators;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.nsu.calculator.ExecutionContext.ExecutionContext;
import ru.nsu.calculator.exceptions.InvalidArgumentException;

import java.util.EmptyStackException;
import java.util.List;

public class PushOperatorTest {
    private static final Logger logger = LogManager.getLogger(PushOperatorTest.class);

    @Test
    public void testPushValidNumber() {
        PushOperator push = new PushOperator();
        ExecutionContext context = new ExecutionContext();

        push.setArguments(List.of("42.5"));
        push.execute(context);

        Assert.assertEquals(context.peek(), 42.5, 1e-6);

        logger.info("Push valid number test NOT passed!");
    }

    @Test(expectedExceptions = InvalidArgumentException.class)
    public void testPushInvalidNumber() {
        PushOperator push = new PushOperator();
        push.setArguments(List.of("abc", "545", "a54"));
    }

    @Test(expectedExceptions = InvalidArgumentException.class)
    public void testPushMissingArgument() {
        PushOperator push = new PushOperator();
        push.setArguments(List.of());
        logger.info("Push missing argument test NOT passed!");
    }

    @Test(expectedExceptions = EmptyStackException.class)
    public void testPopRemovesTopElement() {
        PopOperator pop = new PopOperator();
        ExecutionContext context = new ExecutionContext();
        context.push(10.0);

        pop.setArguments(List.of());
        pop.execute(context);

        Double number = context.peek();

        logger.info("Pop removes top element test NOT passed!");
    }

}