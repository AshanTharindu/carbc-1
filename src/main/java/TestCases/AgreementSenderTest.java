package TestCases;

import fakeAgreementSender.AgreementSender;
import network.Node;
import network.communicationHandler.MessageSender;

public class AgreementSenderTest {
    public static void main(String[] args) throws InterruptedException {
        startNode();
        Thread.sleep(20000);
        sendAgreements();
//        generateOrgsKeyPairs();
    }

    public static void sendAgreements() throws InterruptedException {
//        String[] orgs = {"RMV", "GodFather"};
        String[] orgs = {"RMV"};
        AgreementSender agreementSender = new AgreementSender();
        for( String org : orgs) {
            System.out.println(org);
            Thread.sleep(1000);
            agreementSender.sendFakeAgreements(org);
        }
    }

    public static void startNode() {
        Node.getInstance().startNode("12345", 45362);
        MessageSender.requestIP();
    }

    public static void generateOrgsKeyPairs() {
        AgreementSender agreementSender = new AgreementSender();
//        agreementSender.generateKeyPair("ServiceStation");
//        agreementSender.generateKeyPair("RMV");
//        agreementSender.generateKeyPair("SparePartShop");
//        agreementSender.generateKeyPair("GodFather");

        System.out.println("service Station: "+agreementSender.getPublicKeyAsString("ServiceStation"));
        System.out.println("rmv :" + agreementSender.getPublicKeyAsString("RMV"));
        System.out.println("spare parts: " +agreementSender.getPublicKeyAsString("SparePartShop"));
        System.out.println("GodFather: " +agreementSender.getPublicKeyAsString("GodFather"));
    }
}
