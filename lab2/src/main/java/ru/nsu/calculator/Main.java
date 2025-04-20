package ru.nsu.calculator;

import ru.nsu.calculator.CalculatorEngine.CalculatorEngine;
import ru.nsu.calculator.exceptions.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main{
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.debug("Program started");

        CalculatorEngine engine;
        try {
            engine = new CalculatorEngine(args);
        } catch (ConfigurationException e) {
            logger.fatal("Error while loading configuration.");
            logger.fatal("Cause: {}", String.valueOf(e));
            logger.debug("Program closed");
            return;
        }
        if (engine.enabledTerminalMode()) {
            logger.debug("Terminal mode enabled.");
            engine.terminalRun();
        }
        else if (!engine.enabledTerminalMode()) {
            logger.debug("File mode enabled.");
            engine.fileRun();
        }
    }
}