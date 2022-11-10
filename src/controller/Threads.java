package controller;

import java.util.ArrayList;

public class Threads {
    private final ArrayList<Thread> threads;

    public Threads() {
        threads = new ArrayList<>();
    }

    public void addThread(Thread thread) {
        threads.add(thread);
        System.out.println("Added thread: " + thread.getName());
        thread.start();
    }

    public int getNumberOfThreads() {
        return threads.size();
    }

    public void checkThreads() {
        threads.removeIf(thread -> !thread.isAlive());
    }
}
