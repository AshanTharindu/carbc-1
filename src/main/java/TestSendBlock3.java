import Exceptions.FileUtilityException;
import chainUtil.ChainUtil;
import chainUtil.KeyGenerator;
import config.CommonConfigHolder;
import constants.Constants;
import core.blockchain.*;
import network.Node;
import org.json.JSONObject;
import org.slf4j.impl.SimpleLogger;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TestSendBlock3 {
    public static void main(String[] args) throws FileUtilityException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, IOException {
        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");

        /*
         * Set the main directory as home
         * */
        System.setProperty(Constants.CARBC_HOME, System.getProperty("user.dir"));

        /*
         * At the very beginning
         * A Config common to all: network, blockchain, etc.
         * */
        CommonConfigHolder commonConfigHolder = CommonConfigHolder.getInstance();
        commonConfigHolder.setConfigUsingResource("peer1");

        /*
         * when initializing the network
         * */
        Node node = Node.getInstance();
        node.init();

        /*
         * when we want our node to start listening
         * */
        node.startListening();

        /*
         * when we want to send a block
         * */
//        JSONObject ourBlock = new JSONObject();
//        JSONObject ourBlock1 = new JSONObject();
//        ourBlock1.put("firstName", "Ashan");
//        ourBlock1.put("lastName", "Tharindu");
//        ourBlock.put("personDetails",ourBlock1);
//        RequestMessage blockMessage = BlockMessageCreator.createBlockMessage(ourBlock);
//        blockMessage.addHeader("keepActive", "false");
//        blockMessage.addHeader("messageType", "AgreementRequest");
//        node.sendMessageToNeighbour(1, blockMessage);

        try {
//            byte[] prevhash = ChainUtil.hexStringToByteArray("1234");
//            byte[] hash = ChainUtil.hexStringToByteArray("5678");
//            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//            byte[] data = ChainUtil.hexStringToByteArray("1456");
//            byte[] signatue1 = ChainUtil.hexStringToByteArray("3332");
//            byte[] signatue2 = ChainUtil.hexStringToByteArray("3442");
//            PublicKey publicKey = KeyGenerator.getInstance().getPublicKey();
//            Validator validator1 = new Validator("val1pubkey","owner","true",3);
//            Validator validator2 = new Validator("val2pubkey","seller","true",4);
//            ArrayList<Validation> validations = new ArrayList<>();
//            validations.add(new Validation(validator1,"3332"));
//            validations.add(new Validation(validator2,"3442"));
//            BlockHeader blockHeader = new BlockHeader("101","1234","",
//                    "senderPubkey",123,true);
//            Transaction transaction = new Transaction("senderpubkey",validations,
//                    "tran1",new TransactionInfo());
//
//            Block block = new Block(blockHeader,transaction);
//            JSONObject jsonObject = new JSONObject(block);
//            String myJson = jsonObject.toString();
//            System.out.println(myJson);
//            //  MessageSender.getInstance().requestAgreement(block,1);
//
//            Validator validator3 = new Validator(KeyGenerator.getInstance().getEncodedPublicKeyString(KeyGenerator.getInstance().getPublicKey()),"owner","true",3);
//            Validator validator4 = new Validator("3081f13081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca4034400024100d1f13f9b315e6fa41e1920ae2d875f28f7129ab4f8e29eb12783d238430585c225e7d05f1e84c2218abb65a9dc5bc7b8df03012dffc5dececb18f76a64440335","seller","true",4);
//            ArrayList<Validator> validators = new ArrayList<>();
//            validators.add(validator4);
//            //validators.add(validator2);
//            Calendar calendar = Calendar.getInstance();
//            java.util.Date now = calendar.getTime();
//
//            //java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
//            Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
//            String timeStampStr = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
//            System.out.println("timestamp:  "+timeStampStr);
//            Object[] parameters = new Object[2];
//            parameters[0] = "para1";
//            parameters[1] = "5";
//
//            TransactionInfo transactionInfo = new TransactionInfo();
//            transactionInfo.setEvent("registration");
//            transactionInfo.setSmartContractSignature("aaaaa");
//            transactionInfo.setSmartContractMethod("registerVehicle");
//            transactionInfo.setData("bbbbb");
//            transactionInfo.setParameters(parameters);
//
//            TransactionProposal proposal = new TransactionProposal(KeyGenerator.getInstance().getEncodedPublicKeyString(KeyGenerator.getInstance().getPublicKey()),validators,
//                    "data","proposal1",timeStampStr,transactionInfo);
//
//
//
//            System.out.println(new JSONObject(proposal).toString());
//            System.out.println(KeyGenerator.getInstance().getEncodedPublicKeyString(KeyGenerator.getInstance().getPublicKey()));
////            proposal.sendProposal();



        } catch (Exception e) {
            e.getMessage();
        }
    }
}
