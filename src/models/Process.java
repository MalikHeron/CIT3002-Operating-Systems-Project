package models;

import controller.Resource;

import java.util.Random;

public class Process {
    private int processId;
    private Runnable[] tasks;
    private int priority;
    private int arrivalTime;
    private long blockedTime;
    private int burstTime;
    private Resource resource;

    public Process() {
        processId = 0;
        tasks = new Runnable[0];
        priority = 0;
        arrivalTime = 0;
        blockedTime = 0;
        burstTime = 0;
    }

    public Process(int processId, Resource resource) {
        this.resource = resource;
        setProcessId(processId);
        Random random = new Random();
        setPriority((random.nextInt(5) + 1));
        setArrivalTime(random.nextInt(30));
        setBurstTime((random.nextInt(5) + 1));
        tasks = new Runnable[0];
        blockedTime = 0;
    }

    public Process(int processId, Runnable[] tasks, int priority, int arrivalTime,
                   int blockedTime, int burstTime, Resource resource) {
        this.resource = resource;
        this.processId = processId;
        this.tasks = tasks;
        this.priority = priority;
        this.arrivalTime = arrivalTime;
        this.blockedTime = blockedTime;
        this.burstTime = burstTime;
    }

    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }

    public Runnable[] getTasks() {
        tasks = new Runnable[4];

        // initialize array items
        tasks[0] = resource::addRecord;
        tasks[1] = resource::removeRecord;
        tasks[2] = resource::retrieveRecord;
        tasks[3] = resource::calculateTotalResourceData;

        return tasks;
    }

    public void setTasks(Runnable[] tasks) {
        this.tasks = tasks;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public long getBlockedTime() {
        return blockedTime;
    }

    public void setBlockedTime(long blockedTime) {
        this.blockedTime = blockedTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    @Override
    public String toString() {
        return "Process {" +
                "\n\tprocessId = " + processId +
                //"\n\ttask = " + Arrays.toString(tasks) +
                "\n\tpriority = " + priority +
                "\n\tarrivalTime = " + arrivalTime +
                "\n\tblockedTime = " + blockedTime +
                "\n\tburstTime = " + burstTime +
                "\n}";
    }
}
