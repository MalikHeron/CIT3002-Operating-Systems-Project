package controller;

import java.util.ArrayList;

public class CPU {
    private final ArrayList<Thread> cores;
    int coreNumber;

    public CPU() {
        cores = new ArrayList<>();
        coreNumber = 0;
    }

    public int getCoreNumber(Thread process) {
        return cores.indexOf(process) + 1;
    }

    public void addProcess(Thread process) {
        cores.add(process);
        System.out.println("-> Running on core " + getCoreNumber(process));
        process.start();
    }

    public int getNumberOfProcesses() {
        return cores.size();
    }

    public void checkCPUs() {
        if (cores.removeIf(process -> !process.isAlive())) {
            for (Thread process : cores) {
                System.out.println("-> A process is running on core " + getCoreNumber(process));
            }
        }
    }
}
