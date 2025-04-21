package ru.nsu.calculator.operators;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.nsu.calculator.ExecutionContext.ExecutionContext;

public class AdditionOperatorTest {
    private static final Logger logger = LogManager.getLogger(AdditionOperatorTest.class);

    @Test
    public void testCalculateValue() {
        PushOperator push = new PushOperator();
        AdditionOperator plus = new AdditionOperator();
        ExecutionContext context = new ExecutionContext();

        context.push(10.0);
        context.push(32.0);
        plus.execute(context);

        Assert.assertEquals(context.peek(), 42.0);

        logger.info("Addition test passed!");
    }
}