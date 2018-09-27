import Exceptions.FileUtilityException;
import chainUtil.ChainUtil;
import chainUtil.KeyGenerator;
import config.CommonConfigHolder;
import constants.Constants;
import core.blockchain.*;
import core.consensus.AgreementCollector;
import network.Client.RequestMessage;
import network.Node;
import network.Protocol.BlockMessageCreator;
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

public class   TestSendBlock2 {
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
        commonConfigHolder.setConfigUsingResource("peer2");

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
            byte[] prevhash = ChainUtil.hexStringToByteArray("1234");
            byte[] hash = ChainUtil.hexStringToByteArray("5678");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            byte[] data = ChainUtil.hexStringToByteArray("1456");
            byte[] signatue1 = ChainUtil.hexStringToByteArray("3332");
            byte[] signatue2 = ChainUtil.hexStringToByteArray("3442");
            PublicKey publicKey = KeyGenerator.getInstance().getPublicKey();
            Validator validator1 = new Validator("val1pubkey","owner","true",3);
//            Validator validator2 = new Validator("val2pubkey","seller","true",4);
            ArrayList<Validation> validations = new ArrayList<>();
            validations.add(new Validation(validator1,"3332"));
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
            System.out.println("timestamp: "+timeStamp);
            BlockHeader blockHeader = new BlockHeader("101","1234",timeStamp,
                    "senderPubkey",123,true);
            Transaction transaction = new Transaction("senderpubkey",validations,"1456",
                    new TransactionInfo());

            Block block = new Block(blockHeader,transaction);
            JSONObject jsonObject = new JSONObject(block);
            String myJson = jsonObject.toString();

            String agreement = "agreed";
            byte[] signatureRaw = ChainUtil.sign(KeyGenerator.getInstance().getPrivateKey(),agreement);
            String signature = ChainUtil.bytesToHex(ChainUtil.sign(KeyGenerator.getInstance().getPrivateKey(),agreement));
            JSONObject test = new JSONObject();
            test.put("block",jsonObject.toString());
            test.put("agreement",agreement);
            test.put("signature",signature);
            String testString = test.toString();

            JSONObject convertedTest = new JSONObject(testString);

            JSONObject receivedJSONObject = new JSONObject(testString);
            String jblock = (String)receivedJSONObject.get("block");
            JSONObject receivedJSONObject1 = new JSONObject(jblock);

            System.out.println("agreement collector id:");
            System.out.println(AgreementCollector.generateAgreementCollectorId(block));
;
            String msg = "secrectmessage";
            String h1 = ChainUtil.bytesToHex(ChainUtil.getHashByteArray(msg));
//            System.out.println("generated hash: "+h1);

            JSONObject ourBlock1 = new JSONObject();
            ourBlock1.put("string1",h1);
            RequestMessage blockMessage = BlockMessageCreator.createBlockMessage(ourBlock1);
            blockMessage.addHeader("keepActive", "false");
            blockMessage.addHeader("messageType", "test");

//            ConsensusOld.getInstance().requestAgreementForBlock(block);
//            MessageSender.getInstance().BroadCastBlock(block);
            Node.getInstance().sendMessageToNeighbour(1, blockMessage);
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
