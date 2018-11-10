package TestCases;

import fakeAgreementSender.AgreementSender;
import network.Node;
import network.communicationHandler.MessageSender;

public class AgreementSenderTest {
    public static void main(String[] args) throws InterruptedException {
        startNode();
        Thread.sleep(30000);
        sendAgreements();
    }

    public static void sendAgreements() {
        String[] orgs = {"ServiceStation", "RMV", "SparePartShop", "GodFather"};
        AgreementSender agreementSender = new AgreementSender();
        for( String org : orgs) {
            System.out.println(org);
            agreementSender.sendFakeAgreements(org);
        }
    }

    public static void startNode() {
        Node.getInstance().startNode("12345", 45362);
        MessageSender.requestIP();
    }
}
