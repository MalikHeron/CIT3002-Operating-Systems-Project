import controller.Queue;
import controller.Resource;
import controller.Scheduling;
import models.Pairs;
import models.Process;

import java.util.ArrayList;
import java.util.Comparator;

public class Driver {
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

        // Start process scheduling
        Scheduling scheduling = new Scheduling();
        Process readyProcess = readyQueue.dequeue();
        Thread thread = new Thread();

        while (readyProcess != null) {
            thread = new Thread(readyProcess);
            thread.setPriority(readyProcess.getPriority());
            if (scheduling.getNumberOfThreads() < 5) {
                scheduling.addThread(thread);
            }
            do {
                scheduling.checkThreads();
            } while (scheduling.getNumberOfThreads() == 5);
            readyProcess = readyQueue.dequeue();
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
}