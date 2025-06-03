package ru.nsu.fit.yus.factory.storage;

import java.util.LinkedList;
import java.util.Queue;

//Типичный склад каких-то деталей некоего типа (одного из 4-х)
public class Storage<T>{
    private final int capacity; // Максимальная вместимость
    private final Queue<T> items = new LinkedList<>();

    private Storage(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void put(T item) throws InterruptedException {
        if (items.size() == capacity) {
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
