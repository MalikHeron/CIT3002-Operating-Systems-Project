package controller;

import java.util.ArrayList;

public class CPU {
    private final ArrayList<Thread> cores;
    private final Thread defaultThread;

    public CPU() {
        cores = new ArrayList<>(2);
        // defaultThread properties
        defaultThread = new Thread();
        defaultThread.setName("default");
        // Add two cores to CPU
        cores.add(defaultThread);
        cores.add(defaultThread);
    }

    private int getCoreNumber(Thread process) {
        return cores.indexOf(process) + 1;
    }

    public void addProcess(Thread process) {
        int index = 0;
        for (Thread thread : cores) {
            if (thread.getName().equals("default")) {
                index = cores.indexOf(thread);
                break;
            }
        }
        cores.set(index, process);
        System.out.println(process.getName() + " Assigned to CORE " + getCoreNumber(process));
        process.start();
    }

    public int getNumberOfProcesses() {
        int processCount = 0;
        if (checkCPUs()) {
            for (Thread thread : cores) {
                if (!thread.getName().equals("default")) {
                    if (thread.isAlive()) {
                        processCount++;
                    }
                }
            }
        }
        return processCount;
    }

    private boolean checkCPUs() {
        for (Thread process : cores) {
            if (!process.getName().equals("default")) {
                if (!process.isAlive()) {
                    int index = cores.indexOf(process);
                    System.out.println(process.getName() + " Finished executing on CORE " + (index + 1) + ".");
                    cores.set(index, defaultThread);
                }
            }
        }
        return true;
    }
}
