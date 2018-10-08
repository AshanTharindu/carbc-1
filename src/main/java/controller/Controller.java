package controller;

import Exceptions.FileUtilityException;
import chainUtil.ChainUtil;
import chainUtil.KeyGenerator;
import config.CommonConfigHolder;
import constants.Constants;
import core.blockchain.Block;
import core.blockchain.BlockBody;
import core.blockchain.BlockHeader;
import core.blockchain.Transaction;
import core.consensus.Consensus;
import core.consensus.DataCollector;
import network.Client.RequestMessage;
import network.Neighbour;
import network.Node;
import network.Protocol.MessageCreator;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.SimpleLogger;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;

public class Controller {

    private final Logger log = LoggerFactory.getLogger(Controller.class);


    public void requestTransactionData(String vehicleID, String date, String peerID) {
        DataCollector.getInstance().requestTransactionData(vehicleID, date, peerID);
    }

    public void sendTransaction(String transactionType, String event, String data) throws ParseException {
        String sender = KeyGenerator.getInstance().getPublicKeyAsString();
        String nodeID = Node.getInstance().getNodeConfig().getNodeID();
        Transaction transaction = new Transaction(transactionType,sender,event, data, nodeID);

        BlockBody blockBody = new BlockBody();
        blockBody.setTransaction(transaction);
        String blockHash = ChainUtil.getInstance().getBlockHash(blockBody);
        String previousHash = ChainUtil.getInstance().getPreviousHash();
        BlockHeader blockHeader = new BlockHeader(blockHash, previousHash);

        Block block = new Block(blockHeader, blockBody);
        System.out.println(new JSONObject(block));
        Consensus.getInstance().broadcastBlock(block);
    }

    public void sendConfirmation(Block block) {
        Consensus.getInstance().sendAgreementForBlock(block);
    }

    public void requestAdditionalData(Block block) {
        DataCollector.getInstance().requestAdditionalData(block);
    }

    public String getCarBCno(String vehicleID) {
        return vehicleID;
    }

    public void sendAdditonalData() {

    }


    //test methods
    public void testNetwork(String ip, int listeningPort, String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", message);
        RequestMessage testMessage = MessageCreator.createMessage(jsonObject, "Test");
        Node.getInstance().sendMessageToPeer(ip, listeningPort, testMessage);
    }

    public void startNode() throws InvalidKeySpecException, FileUtilityException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
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
        node.initTest();

        /*
         * when we want our node to start listening
         * */
        node.startListening();
    }

    public void handleAdditionalDataRequest(String blockHash, Neighbour dataRequester) {
        log.info("Additional Data Request for block: {} From: {} ",blockHash, dataRequester.getPeerID());
    }

    public void sendAddtionalDataForRequester(String blockHash, Neighbour dataRequester) {
        DataCollector.getInstance().sendAdditionalData(blockHash, dataRequester);
    }

    public void notifyReceivedAdditionalData() {
        log.info("Additional Data Received");
    }

}
