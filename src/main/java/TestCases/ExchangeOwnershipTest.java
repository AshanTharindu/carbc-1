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
        controller.sendTransaction("ExchangeOwnership", "V -92960", createRegisterJSON() );
    }

    public static JSONObject createRegisterJSON(){
        JSONObject ownership = new JSONObject();
        ownership.put("registrationNumber", "CAR-12345");
        ownership.put("vehicleId", "V -92960");
        ownership.put("newOwner", "3081f13081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca4034400024100b4e8f0dc4e2d34820dcbb0218437ef914cfcfdb79fc3161ae9a6453381251ddc1c95d11780aff761761a4168b753cf335a016c662e54a2b96267a7727697616b");
        JSONObject secondaryParty = new JSONObject();
        JSONObject newOwner = new JSONObject();
        newOwner.put("name", "Ashan");
        newOwner.put("publicKey", "308201b63082012b06072a8648ce3804013082011e02818100ddfda2b7b2c12c13a28662b82ee71ea505f4fa00ea21ff1decf85b825970db43ddf03c428d8ba7dc8aa46733893c92519bd841eef56d838362f1aaa0cc2a07ca5218d9c317a69e204b4ece6b72ac5016f2234af42cecd809f8ff0b194a7c1d63ac4f055381986f4a881c3b8ac611de539b53742fc4e7ea42a73ee2749f91528d02150084ae68c9232e68a1e200544ace2bab38949539f5028180074fb7752b601010f6656ba50fe0e8f2447eb04ca19b72a813ec65987ce9aef999ed15e7c311b344990e7e7611f3832172b715e37fd1b505d3f17df87ffbad80ffbc45c6c32e6e9de5c447c682972f42968b88213c25b3039fe1c6ed89ec6b3b9154d347d802baf468e306e26dc079e94cc1fe06fce0b5d8fabc168de7e05824038184000281800c8dd927494442919bc0df192185135c512e80771bf35d8089b31864676a1bc4d749832c6ee584189b9b654bbef8d9d8a2be1d72d95a72a34796f7a99641ddc91d27a5afa89b4e2980dcddee85bb5790d171d7de9503a30d8dc7f03bfb11488dcf2bfa4cc971291b4ad222b39b0b0d51adf5b447d0a377a19594e1c54024f58a");
        JSONObject thirdParty = new JSONObject();
        secondaryParty.put("PreOwner", newOwner);

        ownership.put("SecondaryParty", secondaryParty);
        ownership.put("ThirdParty", thirdParty);

        return ownership;
    }

    public static void sendAgreements(){
//        String[] orgs = {"ServiceStation", "RMV", "SparePartShop", "GodFather"};
        String[] orgs = {"ServiceStation","SparePartShop", "GodFather"};
        AgreementSender agreementSender = new AgreementSender();
        for( String org : orgs) {
            System.out.println(org);
            agreementSender.sendFakeAgreements(org);
        }
    }

}
