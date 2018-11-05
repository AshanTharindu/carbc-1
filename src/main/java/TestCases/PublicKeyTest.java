package TestCases;

import chainUtil.KeyGenerator;
import controller.Controller;

public class PublicKeyTest {
    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.startNode();
//        System.out.println(KeyGenerator.getInstance().getPublicKeyAsString());
    }
}
