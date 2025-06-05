package ru.nsu.fit.yus.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.swing.SwingUtilities;
import ru.nsu.fit.yus.factory.config.ConfigFileParser;
import ru.nsu.fit.yus.factory.controller.StorageController;
import ru.nsu.fit.yus.factory.dealer.Dealer;
import ru.nsu.fit.yus.factory.details.Accessory;
import ru.nsu.fit.yus.factory.details.Body;
import ru.nsu.fit.yus.factory.details.Car;
import ru.nsu.fit.yus.factory.details.Motor;
import ru.nsu.fit.yus.factory.gui.GUI;
import ru.nsu.fit.yus.factory.storage.Storage;
import ru.nsu.fit.yus.factory.supplier.Supplier;
import ru.nsu.fit.yus.threadPool.ThreadPool;

public class Factory {
    private final Storage<Body> bodyStorage;
    private final Storage<Motor> motorStorage;
    private final Storage<Accessory> accessoryStorage;
    private final Storage<Car> carStorage;
    
    private final Supplier<Body> supplierBody;
    private final Supplier<Motor> supplierMotor;
    private final List<Supplier<Accessory>> accessorySuppliers;
    
    int supplierBodyDelay;
    int supplierMotorDelay;
    int supplierAccessoryDelay;
    int dealerDelay;
    
    private final ThreadPool threadPool;
    private final List<Thread> allThreads = new ArrayList<>();
    private final List<Dealer> dealersList;

    public Factory(String configPath) {
        Properties properties = ConfigFileParser.loadConfigData(configPath);
        int storageBodyCapacity = Integer.parseInt(properties.getProperty("StorageBodySize"));
        int storageMotorCapacity = Integer.parseInt(properties.getProperty("StorageMotorSize"));
        int storageAccessorySize = Integer.parseInt(properties.getProperty("StorageAccessorySize"));
        int storageAutoCapacity = Integer.parseInt(properties.getProperty("StorageAutoSize"));
        
        supplierBodyDelay = Integer.parseInt(properties.getProperty("DefaultSupplierBodyDelay"));
        supplierMotorDelay = Integer.parseInt(properties.getProperty("DefaultSupplierMotorDelay"));
        supplierAccessoryDelay = Integer.parseInt(properties.getProperty("DefaultSupplierAccessoryDelay"));
        dealerDelay = Integer.parseInt(properties.getProperty("DefaultDealerDelay"));
        
        int threadPoolSize = Integer.parseInt(properties.getProperty("Workers"));
        int dealerCount = Integer.parseInt(properties.getProperty("Dealers"));
        int supplierAccessoryNumber = Integer.parseInt(properties.getProperty("SupplierAccessoryNumber"));
        boolean isLogging = Boolean.parseBoolean(properties.getProperty("LogSale"));
        
        bodyStorage = new Storage<>(storageBodyCapacity);
        motorStorage = new Storage<>(storageMotorCapacity);
        accessoryStorage = new Storage<>(storageAccessorySize);
        carStorage = new Storage<>(storageAutoCapacity);
        
        supplierBody = new Supplier<>(bodyStorage, Body.class, supplierBodyDelay);
        supplierMotor = new Supplier<>(motorStorage, Motor.class, supplierMotorDelay);
        
        Thread supplierBodyThread = new Thread(supplierBody);
        Thread supplierMotorThread = new Thread(supplierMotor);
        
        allThreads.add(supplierBodyThread);
        allThreads.add(supplierMotorThread);
        
        supplierBodyThread.start();
        supplierMotorThread.start();
        
        accessorySuppliers = new ArrayList<>();

        for(int i = 0; i < supplierAccessoryNumber; ++i) {
            Supplier<Accessory> supplierAccessory = new Supplier<>(accessoryStorage, Accessory.class, supplierAccessoryDelay);
            Thread supplierAccessoryThread = new Thread(supplierAccessory);
            
            accessorySuppliers.add(supplierAccessory);
            allThreads.add(supplierAccessoryThread);
            
            supplierAccessoryThread.start();
        }

        threadPool = new ThreadPool(threadPoolSize);
        StorageController storageController = new StorageController(bodyStorage, motorStorage, accessoryStorage, carStorage, threadPool);
        Thread storageControllerThread = new Thread(storageController);
        allThreads.add(storageControllerThread);
        storageControllerThread.start();
        List<Thread> dealerThreads = new ArrayList<>();
        dealersList = new ArrayList<>();

        for(int i = 0; i < dealerCount; ++i) {
            Dealer newDealer = new Dealer(carStorage, storageController, i + 1, dealerDelay, isLogging);
            Thread dealerThread = new Thread(newDealer);
            dealersList.add(newDealer);
            allThreads.add(dealerThread);
            dealerThreads.add(dealerThread);
        }

        for(Thread dealer : dealerThreads) {
            dealer.start();
        }

        SwingUtilities.invokeLater(() -> new GUI(this));
    }

    public void shutdown() {
        threadPool.shutdown();

        for(Thread t : allThreads) {
            t.interrupt();
        }

    }

    public int getSupplierBodyDelay() {
        return supplierBodyDelay;
    }

    public int getSupplierMotorDelay() {
        return supplierMotorDelay;
    }

    public int getSupplierAccessoryDelay() {
        return supplierAccessoryDelay;
    }

    public int getDealerDelay() {
        return dealerDelay;
    }

    public Storage<Body> getBodyStorage() {
        return bodyStorage;
    }

    public Storage<Motor> getMotorStorage() {
        return motorStorage;
    }

    public Storage<Accessory> getAccessoryStorage() {
        return accessoryStorage;
    }

    public Storage<Car> getCarStorage() {
        return carStorage;
    }

    public Supplier<Body> getSupplierBody() {
        return supplierBody;
    }

    public Supplier<Motor> getSupplierMotor() {
        return supplierMotor;
    }

    public List<Supplier<Accessory>> getAccessorySuppliers() {
        return accessorySuppliers;
    }

    public List<Dealer> getDealersList() {
        return dealersList;
    }

    public ThreadPool getThreadPool() {
        return threadPool;
    }
}
