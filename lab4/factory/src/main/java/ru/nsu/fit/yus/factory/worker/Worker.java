package ru.nsu.fit.yus.factory.worker;

import ru.nsu.fit.yus.factory.details.Accessory;
import ru.nsu.fit.yus.factory.details.Body;
import ru.nsu.fit.yus.factory.details.Car;
import ru.nsu.fit.yus.factory.details.Motor;
import ru.nsu.fit.yus.factory.storage.Storage;
import ru.nsu.fit.yus.threadPool.Task;

public class Worker implements Task {
    private final Storage<Body> bodyStorage;
    private final Storage<Motor> motorStorage;
    private final Storage<Accessory> accessoryStorage;
    private final Storage<Car> carStorage;

    public Worker(Storage<Body> bodyStorage, Storage<Motor> motorStorage, Storage<Accessory> accessoryStorage, Storage<Car> carStorage) {
        this.bodyStorage = bodyStorage;
        this.motorStorage = motorStorage;
        this.accessoryStorage = accessoryStorage;
        this.carStorage = carStorage;
    }

    public void performWork() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Body body = bodyStorage.get();
                Motor motor = motorStorage.get();
                Accessory accessory = accessoryStorage.get();

                Car newCar = new Car(body, motor, accessory);
                carStorage.put(newCar);
            }
        } catch (InterruptedException var5) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("Worker error: " + e.getMessage());
            e.printStackTrace();
        }

    }
}