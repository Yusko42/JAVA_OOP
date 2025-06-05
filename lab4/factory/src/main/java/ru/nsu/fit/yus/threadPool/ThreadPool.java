package ru.nsu.fit.yus.threadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool {
    private final BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();
    private List<Thread> workersList = new ArrayList<>();;
    private boolean isRunning = true;

    public ThreadPool(int numberOfThreads) {
        for (int i = 0; i < numberOfThreads; i++) {
            Thread worker = new Thread(() -> {
                try {
                    while (isRunning) {
                        Task task = taskQueue.take(); // Пойдёт в wait(), если будет пустым
                        task.performWork();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    System.err.println("Thread from pool error: " + e.getMessage());
                    e.printStackTrace();
                }
            }, "Worker-" + i);
            workersList.add(worker);
            worker.start();
        }
    }

    public void addTask(Task task) {
        if (!isRunning) {
            throw new IllegalStateException("ThreadPool is shut down");
        }
        taskQueue.add(task);
    }

    public void shutdown() {
        isRunning = false;
        for (Thread worker : workersList) {
            worker.interrupt();
        }
    }

    public int getPendingTaskCount() {
        return taskQueue.size();
    }
}