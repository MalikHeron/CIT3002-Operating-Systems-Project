package models;

public class Process {
    private int PID;
    private String[] task;
    private int priority;
    private int arrivalTime;
    private long startTime;
    private long endTime;
    private double blockedTime;
    private int burstTime;

    public Process() {
        PID = 0;
    }
}
