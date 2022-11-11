package main;

import controller.Queue;
import controller.Resource;
import controller.Threads;
import models.Pairs;
import models.Process;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class Scheduler {
    static Resource sharedResource = new Resource();
    private static final Random random = new Random();

    public static void main(String[] args) {
        ArrayList<Process> processList = new ArrayList<>();

        // Create a list of 20 processes
        for (int processId = 1; processId <= 20; processId++) {
            Process process = new Process(processId, sharedResource);
            processList.add(process);
        }

        System.out.println("Initial Process List:");
        for (Process process : processList) {
            System.out.println("PROCESS ID: " + process.getProcessId() +
                    ", ARRIVAL TIME: " + process.getArrivalTime());
        }

        // Sort list by fastest arrival currentTime using method reference operator
        processList.sort(Comparator.comparingInt(Process::getArrivalTime));

        System.out.println("\nSorted Process List by Arrival Time:");
        for (Process process : processList) {
            System.out.println("PROCESS ID: " + process.getProcessId() +
                    ", ARRIVAL TIME: " + process.getArrivalTime());
        }
        System.out.println();

        // Add process list to the ready queue
        Queue readyQueue = new Queue(processList);

        // Object of threads for holding 2 threads on CPU
        Threads threads = new Threads();
        // Get the next process in the ready queue
        Process readyProcess = readyQueue.dequeue();

        int currentTime = 0;
        int endTime = -1;
        int compareArrivalTime;

        while (readyProcess != null) {
            System.out.println("Current time: " + currentTime);
            if (currentTime > endTime) {
                // Check if process has arrived
                if (readyProcess.getArrivalTime() <= currentTime) {
                    System.out.println("\n[PROCESS ID: " + readyProcess.getProcessId() +
                            "] Arrival time: " + readyProcess.getArrivalTime());
                    // Get waiting process in the ready queue
                    Process waitingProcess = readyQueue.top();
                    // Check if next process arrived before current ready process
                    compareArrivalTime = readyProcess.getArrivalTime() - waitingProcess.getArrivalTime();

                    if (compareArrivalTime == 0 || compareArrivalTime < 0) {
                        // Arrivals times are the same or ready process arrived before waiting
                        Object[] objects = checkPriority(threads, readyProcess, readyQueue, compareArrivalTime);
                        // Calculate end time of process
                        endTime = currentTime + (int) objects[1];
                        System.out.println("[PROCESS ID: " + readyProcess.getProcessId() +
                                "] End time: " + endTime + "\n");
                        // Set next ready process
                        readyProcess = (Process) objects[0];
                        // Set current time to end time
                        currentTime = endTime;
                    } else {
                        // Do last process
                        doProcessing(threads, readyProcess);
                        break;
                    }
                }
            }
            // Increase currentTime
            currentTime++;
        }

        while (true) {
            // Check if all processes are finished executing
            threads.checkThreads();
            // Check if any threads remain
            if (threads.getNumberOfThreads() == 0) {
                // Check if queue is empty
                if (readyQueue.isEmpty()) {
                    System.out.println("\nAll processes are completed.");

                    // Display finished process list details
                    System.out.println("\nFinished Process List:");
                    for (Process process : processList) {
                        System.out.println("[PROCESS ID: " + process.getProcessId() + "] PRIORITY: " + process.getPriority() +
                                ", ARRIVAL TIME: " + process.getArrivalTime() + ", BURST TIME: " + process.getBurstTime() +
                                ", BLOCKED TIME: " + process.getBlockedTime());
                    }

                    // Get list of resources
                    ArrayList<Pairs> resourceList = sharedResource.getResourceList();

                    // Display final resource list details
                    System.out.println("\nFinal Resource List:");
                    for (Pairs resource : resourceList) {
                        System.out.println("ID: " + resource.id() + ", Data: " + resource.data());
                    }
                    break;
                }
            }
        }
    }

    public static Object[] checkPriority(Threads threads, Process readyProcess, Queue readyQueue, int arrivalTime) {
        // Check if ready process has the higher
        int comparePriority = (readyProcess.getPriority() - readyQueue.top().getPriority());
        int burstTime;
        // Check if processes have different arrival times
        if (arrivalTime < 0) {
            // Ready process arrived first
            doProcessing(threads, readyProcess);
            // Get process burst time
            burstTime = readyProcess.getBurstTime();
            // Set readyProcess to next process in the ready queue
            readyProcess = readyQueue.dequeue();
        } else {
            if (comparePriority < 0) {
                // Ready process has the higher priority
                doProcessing(threads, readyProcess);
                // Get process burst time
                burstTime = readyProcess.getBurstTime();
                // Set readyProcess to next process in the ready queue
                readyProcess = readyQueue.dequeue();
            } else if (comparePriority > 0) {
                // Next process has the higher priority
                System.out.println("[PROCESS ID: " + readyQueue.top().getProcessId() + "] " +
                        "has higher priority, swapping with [PROCESS ID: " + readyProcess.getProcessId() + "] ...");
                // Create a copy of readyProcess
                Process tempProcess = readyProcess;
                // Replace readyProcess with waiting process
                readyProcess = readyQueue.dequeue();
                doProcessing(threads, readyProcess);
                // Get process burst time
                burstTime = readyProcess.getBurstTime();
                // Set readyProcess to copied process
                readyProcess = tempProcess;
            } else {
                // Priorities are the same
                System.out.println("[PROCESS ID: " + readyProcess.getProcessId() +
                        "] has same priority as [PROCESS ID: " + readyQueue.top().getProcessId() + "]");
                doProcessing(threads, readyProcess);
                // Get process burst time
                burstTime = readyProcess.getBurstTime();
                // Set readyProcess to next process in the ready queue
                readyProcess = readyQueue.dequeue();
            }
        }
        return new Object[]{readyProcess, burstTime};
    }

    public static void doProcessing(Threads threads, Process readyProcess) {
        // Get all performable tasks
        Runnable[] tasks = readyProcess.getTasks();
        // random value between 0 and 3
        int index = random.nextInt(4);
        boolean lock = false;

        // Check if process should be given mutual exclusion to shared resource
        if (index == 0 || index == 1) {
            System.out.println("[PROCESS ID: " + readyProcess.getProcessId() +
                    "] Will get Mutual exclusion to shared resource.");
            lock = true;
        }

        // New thread for process
        Thread thread = new Thread(tasks[index]);
        // Check if shared resource lock should be locked
        if (lock) {
            int blockedTime = 0;

            System.out.println("[PROCESS ID: " + readyProcess.getProcessId() +
                    "] Waiting on processes to finish...");
            // Allow all currently running process to finish execution
            do {
                blockedTime++;
                threads.checkThreads();
            } while (threads.getNumberOfThreads() != 0);

            System.out.println("- Processes finished.");
            System.out.println("[PROCESS ID: " + readyProcess.getProcessId() +
                    "] Blocked time: " + blockedTime);

            // Set process blocked time
            readyProcess.setBlockedTime(blockedTime);

            // Lock system if no processes are running
            if (threads.getNumberOfThreads() == 0) {
                System.out.println("- Shared resource locked.");
                System.out.println(readyProcess);
                // Add thread to CPU
                threads.addThread(thread);

                System.out.println("[PROCESS ID: " + readyProcess.getProcessId() +
                        "] Processing...");
                // Wait until process is finished
                do {
                    threads.checkThreads();
                } while (threads.getNumberOfThreads() == 1);
            }
        } else {
            // Check if less than 2 process are running
            if (threads.getNumberOfThreads() < 2) {
                System.out.println(readyProcess);
                threads.addThread(thread);
            }
            // Allow a maximum of 2 processes to run at a time
            do {
                threads.checkThreads();
            } while (threads.getNumberOfThreads() == 2);
        }
    }
}