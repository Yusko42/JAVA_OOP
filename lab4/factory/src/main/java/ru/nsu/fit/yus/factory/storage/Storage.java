package ru.nsu.fit.yus.factory.storage;

import java.util.LinkedList;
import java.util.Queue;

public class Storage<T>{
    private final int capacity; // Максимальная вместимость
    private final Queue<T> items = new LinkedList<>();

    public Storage(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void put(T item) throws InterruptedException {
        while (items.size() >= capacity) {
            wait(); // Освобождаем монитор, ожидаем свободного места...
        }
        items.add(item);
        notifyAll();
    }

    public synchronized T get() throws InterruptedException {
        while (items.isEmpty()) {
            wait();
        }
        T item = items.poll();
        notifyAll();
        return item;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getSize() {
        return items.size();
    }
}
