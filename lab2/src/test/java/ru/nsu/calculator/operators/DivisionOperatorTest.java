package ru.nsu.calculator.operators;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.nsu.calculator.ExecutionContext.ExecutionContext;

public class DivisionOperatorTest {
    private static final Logger logger = LogManager.getLogger(DivisionOperatorTest.class);

    @Test
    public void testCalculateValue() {
        PushOperator push = new PushOperator();
        DivisionOperator divide = new DivisionOperator();
        ExecutionContext context = new ExecutionContext();

        context.push(42.0);
        context.push(2.0);
        divide.execute(context);

        Assert.assertEquals(context.peek(), 21.0);

        logger.info("Division test passed!");
    }
}