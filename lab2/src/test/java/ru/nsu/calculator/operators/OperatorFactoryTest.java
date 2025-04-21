package ru.nsu.calculator.operators;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.nsu.calculator.CalculatorEngine.CalculatorEngine;
import ru.nsu.calculator.factory.OperatorFactory;

public class OperatorFactoryTest {
    private static final Logger logger = LogManager.getLogger(OperatorFactoryTest.class);

    @Test
    public void testFactoryCreatesPush()  {
        OperatorFactory factory = new OperatorFactory("/ru/com/calculator/factory/config.properties");
        Operator op = factory.createOperator("PUSH");

        Assert.assertTrue(op instanceof PushOperator);
        logger.info("Factory Creates Push passed!");
    }

}
