import controller.Resource;
import models.Pairs;
import models.Process;

import java.util.ArrayList;

public class Driver {
    public static void main(String[] args) {
        Resource sharedResource = new Resource();
        sharedResource.initializeResourceList();
        ArrayList<Pairs> resourceList = sharedResource.getResourceList();

        /*System.out.println("Initial Resource List:");
        for (Pairs resource : resourceList) {
            System.out.println("ID: " + resource.id() + ", Data: " + resource.data());
        }*/

        Process process = new Process();
        process.initializeProcess();
        System.out.println(process);
        process.runTask(sharedResource);

        /*System.out.println("\nList after process: ");
        for (Pairs resource : resourceList) {
            System.out.println("ID: " + resource.id() + ", Data: " + resource.data());
        }
        //sharedResource.removeRecord();
        //sharedResource.retrieveRecord();*/
    }
}