package models;

import controller.Resource;

import java.util.Arrays;
import java.util.Random;

public class Process {
    private final Random random = new Random();
    private int processId;
    private Runnable[] task;
    private int priority;
    private int arrivalTime;
    private long startTime;
    private long endTime;
    private double blockedTime;
    private int burstTime;

    public Process() {
        processId = 0;
        task = new Runnable[0];
        priority = 0;
        arrivalTime = 0;
        startTime = 0L;
        endTime = 0L;
        blockedTime = 0.0;
        burstTime = 0;
    }

    public Process(int processId, Runnable[] task, int priority, int arrivalTime, long startTime,
                   long endTime, double blockedTime, int burstTime) {
        this.processId = processId;
        this.task = task;
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

    public Runnable[] getTask() {
        return task;
    }

    public void setTask(Runnable[] task) {
        this.task = task;
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

    public void initializeProcess() {
        setProcessId((random.nextInt(20) + 1));
        setPriority((random.nextInt(5) + 1));
        setArrivalTime(random.nextInt(30));
        setBurstTime((random.nextInt(5) + 1));
    }

    public void runTask(Resource resource) {
        task = new Runnable[4];

        // initialize array items
        task[0] = resource::addRecord;
        task[1] = resource::removeRecord;
        task[2] = resource::retrieveRecord;
        task[3] = resource::calculateTotalResourceData;

        // random value between 0 and 3
        int index = random.nextInt(4);

        // run the method
        task[index].run();
    }

    @Override
    public String toString() {
        return "Process{" +
                "processId=" + processId +
                ", task=" + Arrays.toString(task) +
                ", priority=" + priority +
                ", arrivalTime=" + arrivalTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", blockedTime=" + blockedTime +
                ", burstTime=" + burstTime +
                '}';
    }
}
