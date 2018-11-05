package TestCases;

import controller.Controller;
import network.communicationHandler.MessageSender;
import chainUtil.KeyGenerator;
import com.google.gson.JsonObject;
import controller.Controller;
import org.json.JSONArray;
import org.json.JSONObject;

public class RegisterVehicleTest {
    public static void main(String[] args) {
        startNodeTest();

    }

    public static void startNodeTest(){
        Controller controller = new Controller();
        controller.startNode();
        MessageSender.requestIP();
        controller.sendTransaction("RegisterVehicle", null, createRegisterJSON() );
    }

    public static JSONObject createRegisterJSON(){
        JSONObject registration = new JSONObject();
        registration.put("registration_number", "CAR-12345");
        registration.put("current_owner", "3081f13081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca4034400024100b4e8f0dc4e2d34820dcbb0218437ef914cfcfdb79fc3161ae9a6453381251ddc1c95d11780aff761761a4168b753cf335a016c662e54a2b96267a7727697616b");
        registration.put("engine_number", "AAA1111");
        registration.put("vehicle_class", "Car");
        registration.put("condition_and_note", "Full condition");
        registration.put("make", "Vitz");
        registration.put("model", "Toyota");
        registration.put("year_of_manufacture", "2012");
        JSONObject secondaryParty = new JSONObject();
        JSONObject thirdParty = new JSONObject();

        registration.put("SecondaryParty", secondaryParty);
        registration.put("ThirdParty", thirdParty);

        return registration;
    }

    public static void ttest(){

    }


}
