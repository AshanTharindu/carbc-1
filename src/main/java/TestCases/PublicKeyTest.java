package TestCases;

import controller.Controller;

import java.io.UnsupportedEncodingException;

public class PublicKeyTest {
    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(java.net.URLEncoder.encode("Hello World", "UTF-8"));

//        Controller controller = new Controller();
//        controller.startNode();
////        System.out.println(KeyGenerator.getInstance().getPublicKeyAsString());
    }
}
