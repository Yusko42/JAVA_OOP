package ru.nsu.calculator.factory;

import ru.nsu.calculator.exceptions.*;
import ru.nsu.calculator.operators.Operator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class OperatorFactory {
    // Stores operator names & classes (for checking the correctness)
    public static final Map<String, Class<? extends Operator>> operatorsRegistry = new HashMap<>();

    private static final Logger logger = LogManager.getLogger(OperatorFactory.class);

    public OperatorFactory(String configFile){
        // For loading config file information
        Properties properties = new Properties();

        try (InputStream is = OperatorFactory.class.getResourceAsStream(configFile)) {
            if (is == null)
                throw new ConfigurationFileNotFoundException("Config file not found.");
            properties.load(is);
        } catch (IOException e) {
            throw new ConfigurationLoadException("Failed to load configuration", e);
        }

        // Loading to register and checking the existence of classes
        for (String key : properties.stringPropertyNames()) {
            String className = properties.getProperty(key);
            try {
                Class<?> operatorClass = Class.forName(className);
                if (!Operator.class.isAssignableFrom(operatorClass))
                    throw new OperatorLoadException(className + " does not implement the Operator interface");
                // Casts operatorClass to represent a subclass of the class represented by Operator.class
                operatorsRegistry.put(key, operatorClass.asSubclass(Operator.class));
            } catch (ConfigurationException | ClassNotFoundException e) {
                logger.error("Error while loading: \"" + key + "\".");
                logger.error("Cause: " + e.getMessage());
                //e.printStackTrace();
            }
        }
    }

    public Operator createOperator(String input){
        try {
            Class<? extends Operator> operatorClass = operatorsRegistry.get(input);
            return operatorClass.getDeclaredConstructor().newInstance();
        } catch (NullPointerException e) {
            throw new OperatorNotFoundException("Operator name not found: " + input);
        } catch (Exception e) {
            throw new OperatorLoadException("Failed to create an operator instance for: " + input, e);
        }
    }
}