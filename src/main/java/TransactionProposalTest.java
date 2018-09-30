import chainUtil.KeyGenerator;
import com.google.gson.Gson;
import core.blockchain.*;
//import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Calendar;

public class TransactionProposalTest {
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, SignatureException, NoSuchProviderException, InvalidKeyException, InvalidKeySpecException {
//        Validator validator1 = new Validator(KeyGenerator.getInstance().getEncodedPublicKeyString(KeyGenerator.getInstance().getPublicKey()),"owner","true",3);
//        Validator validator2 = new Validator("v2","seller","true",4);
//        ArrayList<Validator> validators = new ArrayList<>();
//        validators.add(validator1);
//        //validators.add(validator2);
//        Calendar calendar = Calendar.getInstance();
//        java.util.Date now = calendar.getTime();
//
//        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
//
//        TransactionInfo transactionInfo = new TransactionInfo();
//
//        TransactionProposal proposal = new TransactionProposal(KeyGenerator.getInstance().getEncodedPublicKeyString(KeyGenerator.getInstance().getPublicKey()),validators,
//                "data","proposal-1",currentTimestamp.toString(),transactionInfo);
//
//        //proposal.sendProposal();
//
//       // proposal.isValid();
//
//       // System.out.println(TempResponsePool.getResponsePool());
//
//
//        TransactionResponse response  = proposal.signProposal();
//        String pro = new JSONObject(proposal).toString();
//        System.out.println("response in json form: " + pro);
//
//        TransactionProposal response1 = new Gson().fromJson(pro,TransactionProposal.class);
////        System.out.println(response1.getpID());
//        System.out.println("converted from string response: " + new JSONObject(response1).toString() );
////        response.addResponse();
////
////        TransactionResponse response2  = proposal.signProposal();
////
////        response2.addResponse();
////
////        TransactionResponse response3  = proposal.signProposal();
////
////        response3.addResponse();
////
////        System.out.println(ChainUtil.verify(KeyGenerator.getInstance().getPublicKey(),ChainUtil.hexStringToByteArray("302c021418c6767de06a101ffc30009f0a09057d820db9d4021438e3e78e540ee7a8e164d704f32b15eb05d00d11"),"gdghsg"));
////        System.out.println(ChainUtil.verify(KeyGenerator.getInstance().getPublicKey(),ChainUtil.hexStringToByteArray("302c0214248cff72c8acd3e07b78712c94e66c53e02f57dd021460b14afa9c9721b02c28524948c63d51d07ebcf1"),"gdghsg"));
////        System.out.println(ChainUtil.verify(KeyGenerator.getInstance().getPublicKey(),ChainUtil.hexStringToByteArray("302c02147f2164527241ac1fa25627326b4e63ec97148d8302142bd9854da0857e727d276624815b2b7bee5dad13"),"gdghsg"));
    }
}
