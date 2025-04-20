package ru.nsu.calculator.CalculatorEngine;

import ru.nsu.calculator.ExecutionContext.ExecutionContext;
import ru.nsu.calculator.exceptions.*;
import ru.nsu.calculator.factory.OperatorFactory;
import ru.nsu.calculator.operators.Operator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CalculatorEngine {
    Boolean mode;   // true = terminal mod, false = file mode
    String fileName;
    File inputFile;

    ExecutionContext context;
    OperatorFactory factory;

    private static final Logger logger = LogManager.getLogger(CalculatorEngine.class);

    public CalculatorEngine(String[] args) throws ConfigurationException{
        if (!(args.length == 0)) {
            if (args.length > 1) {
                logger.warn("Usage for text mode: <program_name> <filepath>\nTerminal mode will be started.\n");
                mode = true;
            } else {
                fileName = args[0];
                mode = false;
            }
        }
        else {
            mode = true;
        }
        //ConfigInitialization();
        context = new ExecutionContext();
        //System.out.println(System.getProperty("java.class.path"));
        factory = new OperatorFactory("/ru/com/calculator/factory/config.properties");
    }

    private void lineParser(String currentLine, ExecutionContext context) {
        try {
            String lineWithoutComments = currentLine.split("#", 2)[0].trim();
            if (lineWithoutComments.isEmpty())
                return;
            String[] tokens = lineWithoutComments.split("\\s+");

            //String[] tokens = currentLine.trim().split("\\s+");
            String operatorName = tokens[0];

            List<String> args = Arrays.asList(tokens).subList(1, tokens.length);

            Operator operator = factory.createOperator(operatorName);
            operator.setArguments(args);

            operator.execute(context);
            logger.debug("\"{}\" executed", currentLine);
            } catch (OperatorException e) {
                logger.error("Error while creating an operator. Cause: {}", e.getMessage());
            } catch (ExecutionException e) {
                logger.error("Error while executing an operator: \"{}\"", currentLine);
                logger.error("Cause: {}", e.getMessage());
        }
    }
/*
    private void ConfigInitialization(){
        context = new ExecutionContext();
        try {
            factory = new OperatorFactory("/ru/nsu/calculator/factory/config.properties");
        } catch (ConfigurationException e) {
            System.err.println("Error while loading configuration.");
            System.err.println("Cause: " + e.getMessage());
        }
    }
*/
    public void fileRun(){
        String currentLine;
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            while ((currentLine = reader.readLine()) != null) {
                try {
                    lineParser(currentLine, context);
                } catch (CalculatorException e) {
                    logger.error("Error while executing: \"{}\"", currentLine);
                    logger.error("Cause: {}", e.getMessage());
                }
            }
        } catch (IOException | NullPointerException e) {
            logger.error("Error while opening the file. \nTerminal mode will be started.\n");
            terminalRun();
        }
    }

    public void terminalRun(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the commands here. \nEnter EXIT for closing the calculator.\n");
        String currentLine;

        while (true){
            currentLine = scanner.nextLine();
            if ("EXIT".equalsIgnoreCase(currentLine.trim())) {
                logger.debug("Program closed");
                break;
            }
            try {
                lineParser(currentLine, context);
            } catch (CalculatorException e) {
                logger.error("Error while executing: \"{}\"", currentLine);
                logger.error("Cause: {}", e.getMessage());
            }
        }
        scanner.close();
    }

    public boolean enabledTerminalMode(){ return mode; }
}
