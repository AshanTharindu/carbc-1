package TestCases;

import controller.Controller;
import fakeAgreementSender.AgreementSender;
import network.communicationHandler.MessageSender;
import org.json.JSONObject;

public class ExchangeOwnershipTest {
    public static void main(String[] args) throws InterruptedException {
        startNodeTest();
        Thread.sleep(3000);
        sendAgreements();

    }

    public static void startNodeTest(){
        Controller controller = new Controller();
        controller.startNode();
//        MessageSender.requestIP();
        controller.sendTransaction("ExchangeOwnership", "V-1234", createRegisterJSON() );
    }

    public static JSONObject createRegisterJSON(){
        JSONObject ownership = new JSONObject();
        ownership.put("registrationNumber", "CAR-12345");
        ownership.put("vehicleId", "CAR-12345");
        ownership.put("preOwner", "3081f13081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca4034400024100b4e8f0dc4e2d34820dcbb0218437ef914cfcfdb79fc3161ae9a6453381251ddc1c95d11780aff761761a4168b753cf335a016c662e54a2b96267a7727697616b");
        JSONObject secondaryParty = new JSONObject();
        JSONObject newOwner = new JSONObject();
        newOwner.put("name", "Ashan");
        newOwner.put("publicKey", "3081f03081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca4034300024056938402db3b8e488ebecc7c30f1776485b1daaed992ee02a09fe7a9ce9e5697693e0ca83e43fdfd0b0d63dc6219beca77dff284c2c9dec425ffca68b8a94cf9");
        JSONObject thirdParty = new JSONObject();
        secondaryParty.put("NewOwner", newOwner);

        ownership.put("SecondaryParty", secondaryParty);
        ownership.put("ThirdParty", thirdParty);

        return ownership;
    }

    public static void sendAgreements(){
        String[] orgs = {"ServiceStation", "RMV", "SparePartShop", "GodFather"};
        AgreementSender agreementSender = new AgreementSender();
        for( String org : orgs) {
            System.out.println(org);
            agreementSender.sendFakeAgreements(org);
        }
    }

}
