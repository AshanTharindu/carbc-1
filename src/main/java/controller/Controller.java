package controller;

import chainUtil.ChainUtil;
import chainUtil.KeyGenerator;
import core.blockchain.Block;
import core.blockchain.BlockBody;
import core.blockchain.BlockHeader;
import core.blockchain.Transaction;
import core.consensus.Consensus;
import core.consensus.DataRequester;
import core.consensus.TransactionDataCollector;
import network.Node;
import network.communicationHandler.MessageSender;
import org.json.JSONObject;

import java.sql.Timestamp;

public class Controller {

    public void requestTransactionData(String vehicleID, Timestamp date, String peerID) {
        TransactionDataCollector.getInstance().requestTransactionData(vehicleID, date, peerID);
    }

    public void sendTransaction(String transactionType, String event, JSONObject data) {
        String sender = KeyGenerator.getInstance().getPublicKeyAsString();
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String nodeID = Node.getInstance().getNodeConfig().getNodeID();
        Transaction transaction = new Transaction(transactionType,sender,event, data, nodeID, time);

        BlockBody blockBody = new BlockBody();
        blockBody.setTransaction(transaction);
        String blockHash = ChainUtil.getInstance().getBlockHash(blockBody);
        String previousHash = ChainUtil.getInstance().getPreviousHash();
        BlockHeader blockHeader = new BlockHeader(blockHash, previousHash,time);

        Block block = new Block(blockHeader, blockBody);
        MessageSender.getInstance().broadCastBlock(block);
    }

    public void sendConfirmation(Block block) {
        Consensus.getInstance().sendAgreementForBlock(block);
    }

    public void requestAdditionalData(Block block) {

    }

    public String getCarBCno(String vehicleID) {
        return vehicleID;
    }

}
