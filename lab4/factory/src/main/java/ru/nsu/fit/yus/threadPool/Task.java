package ru.nsu.fit.yus.threadPool;

public interface Task {
    void performWork() throws InterruptedException;
}
