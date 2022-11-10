package controller;

import models.Process;

import java.util.ArrayList;

public class Queue {
    private final int maxSize = 20;
    private final Process[] processes;
    private int front;
    private int back;
    private int currentSize;

    public Queue(ArrayList<Process> processList) {
        processes = new Process[maxSize];
        front = 0;
        back = -1;
        currentSize = 0;
        populateProcesses(processList);
    }

    private void populateProcesses(ArrayList<Process> processList) {
        for (Process process : processList) {
            enqueue(process);
        }
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    public boolean isEmpty() {
        return currentSize == 0;
    }

    public boolean isFull() {
        return currentSize == maxSize;
    }

    public Process top() {
        return processes[front];
    }

    public void enqueue(Process process) {
        if (isFull())
            return;

        back = (back + 1) % maxSize; //to keep the index in range
        processes[back] = process;
        currentSize++;
    }

    public Process dequeue() {
        if (isEmpty())
            return null;

        Process readyProcess = processes[front];
        front = (front + 1) % maxSize; //to keep the index in range
        currentSize--;

        return readyProcess;
    }
}