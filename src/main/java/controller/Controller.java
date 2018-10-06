package controller;

import chainUtil.ChainUtil;
import chainUtil.KeyGenerator;
import core.blockchain.Block;
import core.blockchain.BlockBody;
import core.blockchain.BlockHeader;
import core.blockchain.Transaction;
import core.consensus.Consensus;
import core.consensus.DataCollector;
import network.Client.RequestMessage;
import network.Node;
import network.Protocol.MessageCreator;
import org.json.JSONObject;

import java.text.ParseException;

public class Controller {

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

    public void testNetwork(String ip, int listeningPort, String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", message);
        RequestMessage testMessage = MessageCreator.createMessage(jsonObject, "Test");
        Node.getInstance().sendMessageToPeer(ip, listeningPort, testMessage);
    }

}
