package controller;

public class Queue {
    private final int maxSize;
    private final Object[] array;
    private int front;
    private int back;
    private int currentSize;

    public Queue(int maxSize) {
        this.maxSize = maxSize;
        array = new Object[maxSize];
        front = 0;
        back = -1;
        currentSize = 0;
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

    public Object top() {
        return array[front];
    }

    public void enqueue(Object value) {
        if (isFull())
            return;

        back = (back + 1) % maxSize; //to keep the index in range
        array[back] = value;
        currentSize++;
    }

    public Object dequeue() {
        if (isEmpty())
            return null;

        Object temp = array[front];
        front = (front + 1) % maxSize; //to keep the index in range
        currentSize--;

        return temp;
    }
}