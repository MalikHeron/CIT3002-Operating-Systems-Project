import controller.Resource;
import models.Pairs;

import java.util.ArrayList;

public class Driver {
    public static void main(String[] args) {
        Resource sharedResource = new Resource();
        ArrayList<Pairs> resourceList = sharedResource.getResourceList();

        System.out.println("Initial Resource List:");
        for (Pairs resource : resourceList) {
            System.out.println("ID: " + resource.id() + ", Data: " + resource.data());
        }
        //System.out.println("Total resource data: " + sharedResource.getTotalResourceData());
        //sharedResource.removeRecord();
        //sharedResource.retrieveRecord();
    }
}