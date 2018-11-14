package TestCases;

import controller.Controller;
import network.communicationHandler.MessageSender;
import org.json.JSONObject;

public class BuyVehicle {
    public static void main(String[] args) {
        buyVehicle();
    }

    public static void buyVehicle() {
        Controller controller = new Controller();
        controller.startNode();
        controller.sendTransaction("BuyVehicle", "C", createRegisterJSON());
        MessageSender.requestIP();
    }

    public static JSONObject createRegisterJSON(){
        JSONObject ownership = new JSONObject();
        ownership.put("registrationNumber", "C");
        ownership.put("vehicleId", "C");
        ownership.put("newOwner", "3081f13081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca4034400024100b4e8f0dc4e2d34820dcbb0218437ef914cfcfdb79fc3161ae9a6453381251ddc1c95d11780aff761761a4168b753cf335a016c662e54a2b96267a7727697616b");
        JSONObject secondaryParty = new JSONObject();
        JSONObject newOwner = new JSONObject();
        newOwner.put("name", "Ashan");
        newOwner.put("publicKey", "3081f03081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca4034300024055252e55dd1c1c3c4ea9caa768717d451ede7f550136cab28bbdb46f57d2a670871b77b213f1905e45d5db9c16d0e46ff1f9e7c273a63754fe56cc753eb6bbab");
        JSONObject thirdParty = new JSONObject();
        secondaryParty.put("PreOwner", newOwner);

        ownership.put("SecondaryParty", secondaryParty);
        ownership.put("ThirdParty", thirdParty);
        return ownership;
    }

}
