package controller;

import java.util.ArrayList;

public class Threads {
    private final ArrayList<Thread> threads;
    int CPU;

    public Threads() {
        threads = new ArrayList<>();
        CPU = 0;
    }

    public int getCPU(Thread thread) {
        return threads.indexOf(thread) + 1;
    }

    public void addThread(Thread thread) {
        threads.add(thread);
        System.out.println("-> Running on CPU " + getCPU(thread));
        thread.start();
    }

    public int getNumberOfThreads() {
        return threads.size();
    }

    public void checkThreads() {
        if (threads.removeIf(thread -> !thread.isAlive())) {
            for (Thread thread : threads) {
                System.out.println("-> A process is running on CPU " + getCPU(thread));
            }
        }
    }
}
