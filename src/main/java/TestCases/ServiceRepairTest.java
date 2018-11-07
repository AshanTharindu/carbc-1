package TestCases;

import controller.Controller;
import fakeAgreementSender.AgreementSender;
import network.communicationHandler.MessageSender;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.xml.bind.util.JAXBSource;

public class ServiceRepairTest {
    public static void main(String[] args) throws InterruptedException {
        startNodeTest();
        Thread.sleep(5000);
        sendAgreements();
        System.out.println(createServiceJSON());
    }

    public static void startNodeTest(){
        Controller controller = new Controller();
        controller.startNode();
        MessageSender.requestIP();
        controller.sendTransaction("ServiceRepair", "V-23456", createServiceJSON() );
    }

    public static JSONObject createServiceJSON(){
        JSONObject serviceRepair = new JSONObject();
        serviceRepair.put("vehicle_id", "CAR-67890");
        serviceRepair.put("serviced_date", "AAA1111");
        serviceRepair.put("cost", "Car");
        JSONArray services = new JSONArray();
        JSONObject serviceItem1 = new JSONObject();
        serviceItem1.put("serviceType","Change the engine oil");

        JSONArray serviceData = new JSONArray();
        JSONObject serviceDataItem1 = new JSONObject();
        serviceDataItem1.put("seller", "Theshan Ranasinghe");
        serviceDataItem1.put("seller", "Break Oil");

        JSONObject serviceDataItem2 = new JSONObject();
        serviceDataItem2.put("seller", "Madhushika Jayasinghe");
        serviceDataItem2.put("seller", "head lights");

        serviceData.put(serviceDataItem1);
        serviceData.put(serviceDataItem2);

        serviceItem1.put("serviceType", "Change the engine oil");
        serviceItem1.put("serviceData", serviceData);

        services.put(serviceItem1);

        serviceRepair.put("services", services);



        JSONObject secondaryParty = new JSONObject();
        JSONObject thirdParty = new JSONObject();

        JSONObject serviceStation = new JSONObject();
        serviceStation.put("name", "ServiceStation");
        serviceStation.put("publicKey", "3081f03081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca403430002402d0bc188cd1412351212d1a73c6bd13f80ce53c23fec0ee894083f2ba7067024cb75a32a27b727aa2ae5f3e7a672b9128ee0ff091ba6146cb6d3be857e03352f");
        secondaryParty.put("ServiceStation", serviceStation);

        JSONArray sparePart = new JSONArray();
        sparePart.put("3081f03081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca403430002401c967adabaa96d9107d4a8bee79d6cb526f1a6a901228b2a15d72b5076fc3dadfc852d768b4d49b984d6568a56b4778ea2467c8c39b81e11498341df97936174");
        sparePart.put("3081f03081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca4034300024030b4796d93c6d10282c32814a074e7a708c72c9cf9f99f93e2fb11eb7882b489707eda13648db9414ac521fad3104eb90d77da84843c9d10ff86217f74c739fa");
        thirdParty.put("SparePartProvider", sparePart);


        serviceRepair.put("SecondaryParty", secondaryParty);
        serviceRepair.put("ThirdParty", thirdParty);

        return serviceRepair;
    }

    public static void sendAgreements(){
        String[] orgs = {"ServiceStation", "RMV", "SparePartShop"};
        AgreementSender agreementSender = new AgreementSender();
        for( String org : orgs) {
            System.out.println(org);
            agreementSender.sendFakeAgreements(org);
        }
    }
}
