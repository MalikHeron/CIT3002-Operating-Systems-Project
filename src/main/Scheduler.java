package main;

import controller.Queue;
import controller.Resource;
import controller.CPU;
import models.Pairs;
import models.Process;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Random;

public class Scheduler {
    static Resource sharedResource = new Resource();
    private static final Random random = new Random();

    public static void main(String[] args) {
        ArrayList<Process> processList = new ArrayList<>();

        System.out.println("Initial Resource List:");
        for (Pairs resource : sharedResource.getResourceList()) {
            System.out.println("ID: " + resource.id() + ", Data: " + resource.data());
        }

        // Create a list of 20 processes
        for (int processId = 1; processId <= 20; processId++) {
            Process process = new Process(processId, sharedResource);
            processList.add(process);
        }

        System.out.println("\nInitial Process List:");
        for (Process process : processList) {
            System.out.println("PROCESS ID: " + process.getProcessId() +
                    ", ARRIVAL TIME: " + process.getArrivalTime());
        }

        // Sort list by fastest arrival time using method reference operator
        processList.sort(Comparator.comparingInt(Process::getArrivalTime));

        System.out.println("\nProcess List sorted by Arrival Time:");
        for (Process process : processList) {
            System.out.println("PROCESS ID: " + process.getProcessId() +
                    ", ARRIVAL TIME: " + process.getArrivalTime());
        }
        System.out.println();

        // Add process list to the ready queue
        Queue readyQueue = new Queue(processList);

        // CPU for running 2 processes
        CPU cpu = new CPU();
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
                    // Get next process in the ready queue
                    Process waitingProcess = readyQueue.top();
                    // Check if next process arrived before current ready process
                    compareArrivalTime = readyProcess.getArrivalTime() - waitingProcess.getArrivalTime();

                    if (compareArrivalTime == 0 || compareArrivalTime < 0) {
                        // Arrivals times are the same or ready process arrived before waiting
                        Object[] objects = checkPriority(cpu, readyProcess, readyQueue, compareArrivalTime);
                        // Calculate end time of process
                        endTime = currentTime + (int) objects[2];
                        Process doneProcess = (Process) objects[1];
                        System.out.println("[PROCESS ID: " + doneProcess.getProcessId() +
                                "] End time: " + endTime + "\n");
                        // Set next ready process
                        readyProcess = (Process) objects[0];
                        // Set current time to end time
                        currentTime = endTime;
                    } else {
                        // Do last process
                        doProcessing(cpu, readyProcess);
                        // Calculate end time of process
                        endTime = currentTime + readyProcess.getBurstTime();
                        System.out.println("[PROCESS ID: " + readyProcess.getProcessId() +
                                "] End time: " + endTime + "\n");
                        break;
                    }
                }
            }
            // Increase currentTime
            currentTime++;
        }

        while (true) {
            // Check if any processes remain
            if (cpu.getNumberOfProcesses() == 0) {
                // Check if queue is empty
                if (readyQueue.isEmpty()) {
                    System.out.println("\nAll processes are completed.");

                    // Display finished process list details
                    System.out.println("\nFinished Process List:");
                    for (Process process : processList) {
                        System.out.println("[PROCESS ID: " + process.getProcessId() + "] PRIORITY: " + process.getPriority() +
                                ", ARRIVAL TIME: " + process.getArrivalTime() + ", BURST TIME: " + process.getBurstTime() +
                                ", BLOCKED TIME: " + process.getBlockedTime() + "ms");
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

    public static Object[] checkPriority(CPU cpu, Process readyProcess, Queue readyQueue, int arrivalTime) {
        // Check if ready process has the higher priority
        int comparePriority = readyProcess.getPriority() - readyQueue.top().getPriority();
        int burstTime;
        Process doneProcess;
        // Check if processes have different arrival times
        if (arrivalTime < 0) {
            // Ready process arrived first
            doProcessing(cpu, readyProcess);
            // Get process burst time
            burstTime = readyProcess.getBurstTime();
            // Set done process
            doneProcess = readyProcess;
            // Set readyProcess to next process in the ready queue
            readyProcess = readyQueue.dequeue();
        } else {
            if (comparePriority < 0 || comparePriority == 0) {
                // Priorities are the same
                System.out.println("[PROCESS ID: " + readyProcess.getProcessId() +
                        "] has same or higher priority than [PROCESS ID: " + readyQueue.top().getProcessId() + "]");
                // Ready process has the higher priority
                doProcessing(cpu, readyProcess);
                // Get process burst time
                burstTime = readyProcess.getBurstTime();
                // Set done process
                doneProcess = readyProcess;
                // Set readyProcess to next process in the ready queue
                readyProcess = readyQueue.dequeue();
            } else {
                // Next process has the higher priority
                System.out.println("[PROCESS ID: " + readyQueue.top().getProcessId() + "] " +
                        "has higher priority, swapping with [PROCESS ID: " + readyProcess.getProcessId() + "]");
                // Create a copy of readyProcess
                Process tempProcess = readyProcess;
                // Replace readyProcess with waiting process
                readyProcess = readyQueue.dequeue();
                doProcessing(cpu, readyProcess);
                // Get process burst time
                // Set done process
                doneProcess = readyProcess;
                burstTime = readyProcess.getBurstTime();
                // Set readyProcess to copied process
                readyProcess = tempProcess;
            }
        }
        return new Object[]{readyProcess, doneProcess, burstTime};
    }

    public static void doProcessing(CPU cpu, Process readyProcess) {
        // Get all performable tasks
        Runnable[] tasks = readyProcess.getTasks();
        // random value between 0 and 3
        int index = random.nextInt(4);
        boolean lock = false;
        int numProcesses;

        // Check if process should be given mutual exclusion to shared resource
        if (index == 0 || index == 1) {
            System.out.println("[PROCESS ID: " + readyProcess.getProcessId() +
                    "] Will get Mutual exclusion to shared resource.");
            lock = true;
        }

        // New process
        Thread process = new Thread(tasks[index]);
        process.setName("[PROCESS ID: " + readyProcess.getProcessId() + "]");
        // Check if shared resource should be locked
        if (lock) {
            // Get current system time
            long startTime = Calendar.getInstance().getTimeInMillis();
            System.out.println("[PROCESS ID: " + readyProcess.getProcessId() +
                    "] Waiting on processes to finish...");
            // Allow all currently running processes to finish execution
            do {
                numProcesses = cpu.getNumberOfProcesses();
            } while (numProcesses != 0);

            // Get current system time
            long endTime = Calendar.getInstance().getTimeInMillis();
            // Calculate blocked time
            long blockedTime = endTime - startTime;

            System.out.println("- Processes finished.");
            System.out.println("[PROCESS ID: " + readyProcess.getProcessId() +
                    "] Blocked time: " + blockedTime + "ms");

            // Set process blocked time
            readyProcess.setBlockedTime(blockedTime);

            // Lock system if no processes are running
            if (cpu.getNumberOfProcesses() == 0) {
                System.out.println("- Shared resource locked.");
                System.out.println(readyProcess);
                // Add process to CPU
                cpu.addProcess(process);

                // Wait until process is finished
                do {
                    numProcesses = cpu.getNumberOfProcesses();
                } while (numProcesses >= 1);
            }
        } else {
            // Check if less than 2 process are running
            if (cpu.getNumberOfProcesses() < 2) {
                System.out.println(readyProcess);
                cpu.addProcess(process);
            }
            // Allow a maximum of 2 processes to run at a time
            do {
                numProcesses = cpu.getNumberOfProcesses();
            } while (numProcesses >= 2);
        }
    }
}