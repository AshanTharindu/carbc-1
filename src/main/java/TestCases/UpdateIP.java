package TestCases;

import controller.Controller;
import network.communicationHandler.MessageSender;

public class UpdateIP {
    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.startNode();
        MessageSender.requestIP();
    }
}
