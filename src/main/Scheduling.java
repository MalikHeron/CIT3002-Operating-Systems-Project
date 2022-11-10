package main;

import controller.Queue;
import controller.Resource;
import controller.Threads;
import models.Pairs;
import models.Process;

import java.util.ArrayList;
import java.util.Comparator;

public class Scheduling {
    public static void main(String[] args) {
        Resource sharedResource = new Resource();
        ArrayList<Process> processList = new ArrayList<>();

        // Create a list of 20 processes
        for(int processId = 1; processId <= 20; processId++) {
            Process process = new Process(processId, sharedResource);
            processList.add(process);
        }

        /*System.out.println("Initial Process List:");
        for (Process process : processList) {
            System.out.println("Process ID: " + process.getProcessId() + ", Arrival time: " + process.getArrivalTime());
        }*/

        // Sort listing by fastest arrival time using method reference operator
        processList.sort(Comparator.comparingInt(Process::getArrivalTime));

        /*System.out.println("\nSorted Process List:");
        for (Process process : processList) {
            System.out.println("Process ID: " + process.getProcessId() + ", Arrival time: " + process.getArrivalTime());
        }*/

        // Add process list to the ready queue
        Queue readyQueue = new Queue(processList);

        Threads threads = new Threads();
        Process readyProcess = readyQueue.dequeue();
        Thread thread = new Thread();
        int time = 0;
        int compareArrivalTime;

        while (readyProcess != null) {
            if (readyProcess.getArrivalTime() == time) {
                Process nextProcess = readyQueue.top();
                compareArrivalTime = (readyProcess.getArrivalTime() - nextProcess.getArrivalTime());
                if (compareArrivalTime == 0 || compareArrivalTime < 0) {
                    readyProcess = checkPriority(threads, readyProcess, readyQueue);
                } else {
                    break;
                }
            }
            time++;
        }

        try {
            // Rejoin main thread
            thread.join();
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }

        // Get list of resources
        ArrayList<Pairs> resourceList = sharedResource.getResourceList();

        System.out.println("\nFinished Resource List:");
        for (Pairs resource : resourceList) {
            System.out.println("ID: " + resource.id() + ", Data: " + resource.data());
        }
    }

    public static Process checkPriority(Threads threads, Process readyProcess, Queue readyQueue) {
        int comparePriority = (readyProcess.getPriority() - readyQueue.top().getPriority());
        if (comparePriority < 0) {
            doProcessing(threads, readyProcess);
            readyProcess = readyQueue.dequeue();
        } else if (comparePriority > 0) {
            System.out.println("Next process has lower priority");
            Process tempProcess = readyProcess;
            readyProcess = readyQueue.dequeue();
            doProcessing(threads, readyProcess);
            readyProcess = tempProcess;
        } else {
            System.out.println("Same priority");
            doProcessing(threads, readyProcess);
            readyProcess = readyQueue.dequeue();
        }
        return readyProcess;
    }

    public static void doProcessing(Threads threads, Process readyProcess) {
        Thread thread = new Thread(readyProcess);
        thread.setPriority(readyProcess.getPriority());
        if (threads.getNumberOfThreads() < 5) {
            threads.addThread(thread);
        }
        do {
            threads.checkThreads();
        } while (threads.getNumberOfThreads() == 5);
    }
}