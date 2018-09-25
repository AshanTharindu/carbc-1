package utils;

import chainUtil.ChainUtil;
import chainUtil.KeyGenerator;
import core.blockchain.*;
import org.json.JSONObject;

import java.security.*;
import java.sql.Timestamp;
import java.util.ArrayList;

public class MessageSenderTest {

    public static void main(String[] args) {

        try {
            byte[] prevhash = ChainUtil.hexStringToByteArray("1234");
            byte[] hash = ChainUtil.hexStringToByteArray("5678");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            byte[] data = ChainUtil.hexStringToByteArray("1456");
            byte[] signatue1 = ChainUtil.hexStringToByteArray("3332");
            byte[] signatue2 = ChainUtil.hexStringToByteArray("3442");
            PublicKey publicKey = KeyGenerator.getInstance().getPublicKey();
            Validator validator1 = new Validator("val1pubkey","owner","true",3);
            Validator validator2 = new Validator("val2pubkey","seller","true",4);
            ArrayList<Validation> validations = new ArrayList<>();
            validations.add(new Validation(validator1,"3332"));
            validations.add(new Validation(validator2,"3442"));
            BlockHeader blockHeader = new BlockHeader("101","1234","",
                    "senderPubkey",123,true);

            Transaction transaction = new Transaction("senderpubkey",validations,"1456",
                    new TransactionInfo());

            Block block = new Block(blockHeader,transaction);
            JSONObject jsonObject = new JSONObject(block);
            String myJson = jsonObject.toString();
            System.out.println(myJson);

        } catch (Exception e) {
            e.getMessage();
        }


    }
}
