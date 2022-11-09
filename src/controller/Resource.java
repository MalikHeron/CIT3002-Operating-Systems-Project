package controller;

import models.Pairs;

import java.util.ArrayList;
import java.util.Random;

public class Resource {
    private final Random random = new Random();
    private final ArrayList<Pairs> resourceList;

    public Resource() {
        resourceList = new ArrayList<>();
    }

    public void initializeResourceList() {
        for (int index = 0; index < 20; index++) {
            Pairs resource = new Pairs((index + 1), random.nextInt(100) + 1);
            resourceList.add(resource);
        }
    }

    public void addRecord() {
        int id = random.nextInt(20) + 1;
        int data = random.nextInt(100) + 1;
        int index = id - 1;
        Pairs resource = new Pairs(id, data);
        resourceList.set(index, resource);
    }

    public void removeRecord() {
        int id = random.nextInt(20) + 1;
        int index = id - 1;
        Pairs resource = new Pairs(id, 0);
        resourceList.set(index, resource);
    }

    public void retrieveRecord() {
        int id = random.nextInt(20) + 1;
        int index = id - 1;
        Pairs resource = resourceList.get(index);
        System.out.println("ID: " + resource.id() + ", Data: " + resource.data());
    }

    public int getTotalResourceData() {
        int index = 0;
        int sum = 0;

        while (index < 20) {
            Pairs resource = resourceList.get(index);
            sum += resource.data();
            index++;
        }
        return sum;
    }

    public ArrayList<Pairs> getResourceList() {
        return resourceList;
    }
}
