import com.google.gson.Gson;
import core.blockchain.*;
import network.communicationHandler.RequestHandler;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;

public class RequestHandlerTest {
    public static void main(String[] args) {
        //json string to block convertion test
        try {
//            //block creation
//            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//            Validator validator1 = new Validator("val1pubkey", "owner", "true", 3);
//            Validator validator2 = new Validator("val2pubkey", "seller", "true", 4);
//            ArrayList<RmvValidation> validations = new ArrayList<>();
//            validations.add(new RmvValidation(validator1, "3332"));
//            validations.add(new RmvValidation(validator2, "3442"));
//            BlockHeader blockHeader = new BlockHeader("101", "1234", "",
//                    "senderPubkey", 123, true);
//            Transaction transaction = new Transaction("senderpubkey", validations, "1456",
//                    new TransactionInfo());
//
//            Block block = new Block(blockHeader, transaction);
//            JSONObject jsonObject = new JSONObject(block);
//            System.out.println("json block:");
//            Gson gson = new Gson();
//            String jsonString = gson.toJson(block);
//            System.out.println(jsonString);
//
//            Block convertedBlock  = RequestHandler.getInstance().JSONStringToBlock(jsonString);
//            System.out.println("converted block: ");
//            JSONObject jsonObjectconnverted = new JSONObject(convertedBlock);
//            System.out.println(jsonObjectconnverted.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
