import Exceptions.FileUtilityException;
import chainUtil.ChainUtil;
import chainUtil.KeyGenerator;
import config.CommonConfigHolder;
import constants.Constants;
import controller.Controller;
import core.blockchain.Block;
import core.blockchain.BlockBody;
import core.blockchain.BlockHeader;
import core.blockchain.Transaction;
import core.consensus.Consensus;
import network.communicationHandler.MessageSender;
import network.Node;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.impl.SimpleLogger;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;

public class FirstNodeTest2 {
    public static void main(String[] args) throws InterruptedException {
//        System.out.println(KeyGenerator.getInstance().getPublicKeyAsString());


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
        node.initTest();

        /*
         * when we want our node to start listening
         * */
        node.startListening();

//        MessageSender.getInstance().requestIP(49222);
//        Thread.sleep(4000);

        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObjectNewOwner = new JSONObject();
        JSONObject jsonSecondary = new JSONObject();

        jsonObjectNewOwner.put("name", "Ashan");
        jsonObjectNewOwner.put("publicKey", KeyGenerator.getInstance().getPublicKeyAsString());

        jsonSecondary.put("NewOwner", jsonObjectNewOwner);
        jsonObject.put("SecondaryParty", jsonSecondary);
        jsonObject.put("ThirdParty", new JSONArray());


        Controller controller = new Controller();

        System.out.println(jsonObject);
//
        String sender = KeyGenerator.getInstance().getPublicKeyAsString();
        String nodeID = Node.getInstance().getNodeConfig().getNodeID();
        Transaction transaction = new Transaction("V",sender,"ExchangeOwnership", jsonObject.toString(), nodeID);

        BlockBody blockBody = new BlockBody();
        blockBody.setTransaction(transaction);
        String blockHash = ChainUtil.getInstance().getBlockHash(blockBody);
        BlockHeader blockHeader = new BlockHeader(blockHash);

        Block block = new Block(blockHeader, blockBody);
        MessageSender.getInstance().broadCastBlockTest(block);
        Thread.sleep(4000);
        Consensus.getInstance().sendAgreementForBlockTest(block);
    }
}
