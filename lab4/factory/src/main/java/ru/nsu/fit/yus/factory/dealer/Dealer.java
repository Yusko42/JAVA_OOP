package ru.nsu.fit.yus.factory.dealer;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import ru.nsu.fit.yus.factory.controller.StorageController;
import ru.nsu.fit.yus.factory.details.Car;
import ru.nsu.fit.yus.factory.storage.Storage;

public class Dealer implements Runnable {
    private static final String LOG_PATH = "/src/main/java/ru/nsu/fit/yus/factory/log/factory.log";
    private final boolean isLogging;
    private final int id;
    private int delay;
    private int numberOfCarsSold = 0;
    private final Storage<Car> carStorage;
    private final StorageController storageController;

    public Dealer(Storage<Car> carStorage, StorageController storageController, int id, int delay, boolean isLogging) {
        this.carStorage = carStorage;
        this.storageController = storageController;
        this.id = id;
        this.delay = delay;
        this.isLogging = isLogging;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getNumberOfCarsSold() {
        return this.numberOfCarsSold;
    }

    public void run() {
        try {
            while(!Thread.currentThread().isInterrupted()) {
                Car car = carStorage.get();
                ++numberOfCarsSold;
                System.out.println("already sold car: " + numberOfCarsSold + " by Dealer #" + id);
                if (isLogging) {
                    logSale(car);
                }

                storageController.notifyForSale();
                Thread.sleep(delay);
            }
        } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.out.println("Dealer error: " + e.getMessage());
            e.printStackTrace();
        }

    }

    private void logSale(Car car) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String logEntry = String.format("%s: Dealer %d: Auto %d (Body: %d, Motor: %d, Accessory: %d)%n",
                time, id, car.getId(), car.getBody().getId(), car.getMotor().getId(), car.getAccessory().getId());

        try (FileWriter writer = new FileWriter(LOG_PATH, true)) {
            writer.write(logEntry);
            writer.flush();
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }

    }
}