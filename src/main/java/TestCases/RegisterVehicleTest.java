package TestCases;

import chainUtil.KeyGenerator;
import controller.Controller;

public class RegisterVehicleTest {
    public static void main(String[] args) {
        startNodeTest();
    }

    public static void startNodeTest(){
        Controller controller = new Controller();
        controller.startNode();
    }


}
