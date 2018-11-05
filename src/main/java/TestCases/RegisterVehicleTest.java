package TestCases;

import controller.Controller;
import network.communicationHandler.MessageSender;

public class RegisterVehicleTest {
    public static void main(String[] args) {
        startNodeTest();

        MessageSender.requestIP();

    }

    public static void startNodeTest(){
        Controller controller = new Controller();
        controller.startNode();
    }

    public static void ttest(){

    }


}
