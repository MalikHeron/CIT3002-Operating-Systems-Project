package models;

import controller.Resource;

import java.util.Arrays;
import java.util.Random;

public class Process {
    private final Random random = new Random();
    private int processId;
    private Runnable[] tasks;
    private int priority;
    private int arrivalTime;
    private long startTime;
    private long endTime;
    private double blockedTime;
    private int burstTime;
    private Resource resource;

    public Process() {
        processId = 0;
        tasks = new Runnable[0];
        priority = 0;
        arrivalTime = 0;
        startTime = 0L;
        endTime = 0L;
        blockedTime = 0.0;
        burstTime = 0;
    }

    public Process(int processId, Resource resource) {
        this.resource = resource;
        setProcessId(processId);
        setPriority((random.nextInt(5) + 1));
        setArrivalTime(random.nextInt(30));
        setBurstTime((random.nextInt(5) + 1));
        tasks = new Runnable[0];
        startTime = 0L;
        endTime = 0L;
        blockedTime = 0.0;
    }

    public Process(int processId, Runnable[] tasks, int priority, int arrivalTime, long startTime,
                   long endTime, double blockedTime, int burstTime, Resource resource) {
        this.resource = resource;
        this.processId = processId;
        this.tasks = tasks;
        this.priority = priority;
        this.arrivalTime = arrivalTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.blockedTime = blockedTime;
        this.burstTime = burstTime;
    }

    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public double getBlockedTime() {
        return blockedTime;
    }

    public void setBlockedTime(double blockedTime) {
        this.blockedTime = blockedTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
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

    @Override
    public String toString() {
        return "Process {" +
                "\n\tprocessId = " + processId +
                "\n\ttask = " + Arrays.toString(tasks) +
                "\n\tpriority = " + priority +
                "\n\tarrivalTime = " + arrivalTime +
                "\n\tstartTime = " + startTime +
                "\n\tendTime = " + endTime +
                "\n\tblockedTime = " + blockedTime +
                "\n\tburstTime = " + burstTime +
                "\n}";
    }
}
