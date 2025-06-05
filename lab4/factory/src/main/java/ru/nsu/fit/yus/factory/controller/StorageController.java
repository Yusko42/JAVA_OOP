package ru.nsu.fit.yus.factory.controller;

import ru.nsu.fit.yus.factory.details.Accessory;
import ru.nsu.fit.yus.factory.details.Body;
import ru.nsu.fit.yus.factory.details.Car;
import ru.nsu.fit.yus.factory.details.Motor;
import ru.nsu.fit.yus.factory.storage.Storage;
import ru.nsu.fit.yus.factory.worker.Worker;
import ru.nsu.fit.yus.threadPool.ThreadPool;

public class StorageController implements Runnable {
    private final Storage<Body> bodyStorage;
    private final Storage<Motor> motorStorage;
    private final Storage<Accessory> accessoryStorage;
    private final Storage<Car> carStorage;
    private final ThreadPool threadPool;

    public StorageController(Storage<Body> bodyStorage, Storage<Motor> motorStorage, Storage<Accessory> accessoryStorage, Storage<Car> carStorage, ThreadPool threadPool) {
        this.bodyStorage = bodyStorage;
        this.motorStorage = motorStorage;
        this.accessoryStorage = accessoryStorage;
        this.carStorage = carStorage;
        this.threadPool = threadPool;
    }

    public synchronized void notifyForSale() {
        this.notify();
    }

    public void run() {
        try {
            for(int i = 0; i < this.carStorage.getCapacity(); ++i) {
                this.threadPool.addTask(new Worker(this.bodyStorage, this.motorStorage, this.accessoryStorage, this.carStorage));
            }

            while(!Thread.currentThread().isInterrupted()) {
                synchronized(this) {
                    this.wait();
                }

                synchronized(this.carStorage) {
                    if (this.carStorage.getSize() == 0) {
                        this.threadPool.addTask(new Worker(this.bodyStorage, this.motorStorage, this.accessoryStorage, this.carStorage));
                    }
                }
            }
        } catch (InterruptedException var6) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("Storage Controller error: " + e.getMessage());
            e.printStackTrace();
        }

    }
}

