package ru.nsu.fit.yus.factory.details;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Detail {
    private static final AtomicInteger idCounter = new AtomicInteger(0);
    private final int id;

    protected Detail() {
        this.id = idCounter.getAndIncrement();
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " ID: " + id;
    }
}
