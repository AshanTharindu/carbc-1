
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
import core.serviceStation.webSocketServer.BroadcastServer;
import core.serviceStation.webSocketServer.Test;
import network.Node;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.impl.SimpleLogger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;


public class Peer3Test {
    public static void main(String[] args) throws Exception {

        InetAddress inetAddress = InetAddress.getLocalHost();
        System.out.println("IP Address:- " + inetAddress.getHostAddress());
        System.out.println("Host Name:- " + inetAddress.getHostName());

        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");

        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                        whatismyip.openStream()));

        String ip = in.readLine(); //you get the IP as a String
        System.out.println(ip);
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
        node.initTest();

        /*
         * when we want our node to start listening
         * */
        node.startListening();
//        MessageSender.getInstance().requestIP();


        /*
         * starting UI web socket communication
         * */
        BroadcastServer broadcastServer = new BroadcastServer();
        broadcastServer.start();


        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObjectNewOwner = new JSONObject();
        JSONObject jsonSecondary = new JSONObject();

        jsonObjectNewOwner.put("name", "Ashan");
        jsonObjectNewOwner.put("publicKey", KeyGenerator.getInstance().getPublicKeyAsString());

        jsonSecondary.put("NewOwner", jsonObjectNewOwner);
        jsonObject.put("SecondaryParty", jsonSecondary);
        jsonObject.put("ThirdParty", new JSONArray());

        System.out.println(jsonObject);

        String sender = KeyGenerator.getInstance().getPublicKeyAsString();
        String nodeID = Node.getInstance().getNodeConfig().getNodeID();
        Transaction transaction = new Transaction("V",sender,"ExchangeOwnership", jsonObject.toString(), nodeID);

        BlockBody blockBody = new BlockBody();
        blockBody.setTransaction(transaction);
        String blockHash = ChainUtil.getInstance().getBlockHash(blockBody);
        BlockHeader blockHeader = new BlockHeader(blockHash);

        System.out.println(blockHeader.getBlockTime());
        Block block = new Block(blockHeader, blockBody);

        Consensus.getInstance().testHandleNonApprovedBlock(block);

        Test test = new Test();
        test.start();

    }
}
