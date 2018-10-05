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
import network.Client.RequestMessage;
import network.Node;
import network.Protocol.BlockMessageCreator;
import network.communicationHandler.MessageSender;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Controller {

    public void requestTransactionData(String vehicleID, String date, String peerID) {
        TransactionDataCollector.getInstance().requestTransactionData(vehicleID, date, peerID);
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
