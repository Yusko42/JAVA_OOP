package ru.nsu.fit.yus.factory.supplier;

import java.lang.reflect.InvocationTargetException;
import ru.nsu.fit.yus.factory.details.Detail;
import ru.nsu.fit.yus.factory.storage.Storage;

public class Supplier<T extends Detail> implements Runnable {
    private final Storage<T> storage;
    private final Class<T> detailType;
    private int delay;
    private int numberOfSuppliedDetails;

    public Supplier(Storage<T> storage, Class<T> detailType, int delay) {
        this.storage = storage;
        this.detailType = detailType;
        this.delay = delay;
        this.numberOfSuppliedDetails = 0;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return this.delay;
    }

    public int getNumberOfSuppliedDetails() {
        return this.numberOfSuppliedDetails;
    }

    private T createNewDetail() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return (T)(this.detailType.getDeclaredConstructor().newInstance());
    }

    public void run() {
        try {
            while(!Thread.currentThread().isInterrupted()) {
                T newDetail = this.createNewDetail();
                this.storage.put(newDetail);
                ++this.numberOfSuppliedDetails;
                Thread.sleep(delay);
            }
        } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("Supplier error: " + e.getMessage());
            e.printStackTrace();
        }

    }
}