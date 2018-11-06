package TestCases;

import fakeAgreementSender.AgreementSender;
import network.Node;

public class AgreementSenderTest {
    public static void main(String[] args) {

        Node.getInstance().startNode("12345", 45362);
        String[] orgs = {"ServiceStation", "RMV", "SparePartShop"};
        AgreementSender agreementSender = new AgreementSender();
        for( String org : orgs) {
            System.out.println(org);
            agreementSender.sendFakeAgreements(org);
        }
    }
}
