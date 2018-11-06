package TestCases;

import controller.BlockSender;
import org.json.JSONObject;

public class BlockSenderTest {

    public static void main(String[] args) {
        checkCreateServiceRepairJsonMethod();

    }

    public static void checkCreateServiceRepairJsonMethod(){

        JSONObject serviceData = new JSONObject("{\n" +
                "    \"vehicle_id\": \"22222A\",\n" +
                "    \"serviced_date\": \"2018-10-19 13:55:05.0\",\n" +
                "    \"cost\": \"\",\n" +
                "    \"services\": [\n" +
                "      {\n" +
                "        \"serviceType\": \"Change the engine oil\",\n" +
                "        \"serviceData\": [\n" +
                "          {\n" +
                "            \"seller\": \"Theshan Ranasinghe\",\n" +
                "            \"sparePart\": \"Break Oil\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"seller\": \"Madhushika Jayasinghe\",\n" +
                "            \"sparePart\": \"head lights\"\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"serviceType\": \"Check level and refill Automatic/Manual Transmission Fluid\",\n" +
                "        \"serviceData\": [\n" +
                "          {\n" +
                "            \"seller\": \"Ashan Dewasiri\",\n" +
                "            \"sparePart\": \"tire\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"seller\": \"Sajinie Ranasinghe\",\n" +
                "            \"sparePart\": \"AC\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }");
        BlockSender blockSender = new BlockSender("ServiceRepair", serviceData);
        String data = blockSender.getDataJsonObject(serviceData, "ServiceRepair");
        System.out.println(data);
    }
}
