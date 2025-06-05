package ru.nsu.fit.yus.factory.details;

import java.util.concurrent.atomic.AtomicInteger;

public class Car {
    private final Body body;
    private final Motor motor;
    private final Accessory accessory;
    private static final AtomicInteger idCounter = new AtomicInteger(0);
    private final int id;

    public Car(Body body, Motor motor, Accessory accessory) {
        this.body = body;
        this.motor = motor;
        this.accessory = accessory;
        this.id = idCounter.getAndIncrement();
    }

    public Body getBody() {
        return this.body;
    }

    public Motor getMotor() {
        return this.motor;
    }

    public Accessory getAccessory() {
        return this.accessory;
    }

    public int getId() {
        return this.id;
    }
}
