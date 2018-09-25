package core.communicationHandler;

import chainUtil.ChainUtil;
import com.google.gson.Gson;
import core.blockchain.*;
import chainUtil.KeyGenerator;
import core.blockchain.Block;
import core.blockchain.BlockHeader;
import core.blockchain.Transaction;
import core.blockchain.TransactionProposal;
import network.Client.RequestMessage;
import network.Node;
import network.Protocol.BlockMessageCreator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

public class MessageSender {

    private static  MessageSender messageSender;

    private MessageSender() {};

    public static MessageSender getInstance() {
        if(messageSender == null) {
            messageSender = new MessageSender();
        }
        return messageSender;
    }

    public void requestIP(int ListeningPort) throws IOException {

        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
        String ip = in.readLine(); //you get the IP as a String

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ip",ip);
        jsonObject.put("ListeningPort",ListeningPort);

        RequestMessage blockMessage = BlockMessageCreator.createBlockMessage(jsonObject);
        blockMessage.addHeader("keepActive", "false");
        blockMessage.addHeader("messageType", "RequestIP");
//        Node.getInstance().sendMessageToNeighbour(1, blockMessage);
        Node.getInstance().sendMessageToPeer("127.0.0.1", 49211,blockMessage);
    }

    public void BroadCastBlock(Block block) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("block",blockToJSON(block));
        RequestMessage blockMessage = BlockMessageCreator.createBlockMessage(jsonObject);
        blockMessage.addHeader("keepActive", "false");
        blockMessage.addHeader("messageType", "BlockBroadcast");
        for(int i = 1; i< 2; i++) {
            Node.getInstance().sendMessageToNeighbour(i, blockMessage);
        }
    }

    public void requestAgreement(Block block, int neighbourIndex) {
        System.out.println("Agreement request send");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("block",blockToJSON(block).toString());
        RequestMessage blockMessage = BlockMessageCreator.createBlockMessage(jsonObject);
        blockMessage.addHeader("keepActive", "false");
        blockMessage.addHeader("messageType", "AgreementRequest");
        Node.getInstance().sendMessageToNeighbour(neighbourIndex, blockMessage);
    }

    public void sendAgreement(Block block, int neighbourIndex, String agreement, byte[] signature) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("block",blockToJSON(block).toString());
        jsonObject.put("agreement",agreement);
        jsonObject.put("signature",ChainUtil.bytesToHex(signature));
        jsonObject.put("publickey",KeyGenerator.getInstance().getPublicKeyAsString());
        RequestMessage blockMessage = BlockMessageCreator.createBlockMessage(jsonObject);
        blockMessage.addHeader("keepActive", "false");
        blockMessage.addHeader("messageType", "AgreementResponse");
        Node.getInstance().sendMessageToNeighbour(neighbourIndex, blockMessage);
    }

    public void reqestTransactionValidation(TransactionProposal transactionProposal, int neighbourIndex) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("transactionProposal",new JSONObject(transactionProposal).toString());
        RequestMessage blockMessage = BlockMessageCreator.createBlockMessage(jsonObject);
        blockMessage.addHeader("keepActive", "false");
        blockMessage.addHeader("messageType", "TransactionProposal");
        Node.getInstance().sendMessageToNeighbour(neighbourIndex, blockMessage);
    }

    public void sendTransactionValidation(TransactionResponse transactionResponse, int neighbourIndex) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("transactionResponse",new Gson().toJson(transactionResponse));
        //jsonObject.put("signature",ChainUtil.bytesToHex(signature));
        RequestMessage blockMessage = BlockMessageCreator.createBlockMessage(jsonObject);
        blockMessage.addHeader("keepActive", "false");
        blockMessage.addHeader("messageType", "TransactionValidation");
        Node.getInstance().sendMessageToNeighbour(neighbourIndex, blockMessage);
    }

    public String blockToJSON(Block block) {
        Gson gson = new Gson();
        return gson.toJson(block);
    }

    public String transactionToJSON(Transaction transaction) {
        Gson gson = new Gson();
        return gson.toJson(transaction);
    }
}
