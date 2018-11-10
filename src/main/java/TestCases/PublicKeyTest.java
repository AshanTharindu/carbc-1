package TestCases;

import chainUtil.KeyGenerator;
import controller.Controller;
import network.communicationHandler.MessageSender;

import java.io.UnsupportedEncodingException;

public class PublicKeyTest {
    public static void main(String[] args) throws UnsupportedEncodingException {
        Controller controller = new Controller();
        controller.startNode();
        MessageSender.requestIP();

        System.out.println(KeyGenerator.getInstance().getPublicKeyAsString());
    }
}
