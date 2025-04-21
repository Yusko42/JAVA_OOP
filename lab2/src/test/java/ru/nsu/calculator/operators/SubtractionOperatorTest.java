package ru.nsu.calculator.operators;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.nsu.calculator.ExecutionContext.ExecutionContext;

public class SubtractionOperatorTest {
    private static final Logger logger = LogManager.getLogger(SubtractionOperatorTest.class);

    @Test
    public void testCalculateValue() {
        PushOperator push = new PushOperator();
        SubtractionOperator minus = new SubtractionOperator();
        ExecutionContext context = new ExecutionContext();

        context.push(10.0);
        context.push(32.0);
        minus.execute(context);

        Assert.assertEquals(context.peek(), -22.0);

        logger.info("Subtraction test passed!");
    }
}