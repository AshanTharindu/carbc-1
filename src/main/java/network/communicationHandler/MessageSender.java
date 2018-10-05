package network.communicationHandler;

import chainUtil.ChainUtil;
import com.google.gson.Gson;
import chainUtil.KeyGenerator;
import core.blockchain.Block;
import core.blockchain.Transaction;
import core.consensus.BlockchainRequester;
import core.consensus.Consensus;
import network.Client.RequestMessage;
import network.Neighbour;
import network.Node;
import network.Protocol.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;

public class MessageSender {

    private static  MessageSender messageSender;
    private final Logger log = LoggerFactory.getLogger(MessageSender.class);


    private MessageSender() {};

    public static MessageSender getInstance() {
        if(messageSender == null) {
            messageSender = new MessageSender();
        }
        return messageSender;
    }

    //messages of new protocol
    public void requestIP(int ListeningPort) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ListeningPort",ListeningPort);
        jsonObject.put("nodeID", Node.getInstance().getNodeConfig().getNodeID());
//        RequestMessage requestIPMessage = RequestIPMessageCreator.createRequestIPMessage(jsonObject);
        RequestMessage requestIPMessage = MessageCreator.createSpecificMessage(jsonObject, "Register", "0");
        requestIPMessage.addHeader("keepActive", "false");
        Node.getInstance().sendMessageToPeer("127.0.0.1", 49154,requestIPMessage);
    }

    public void sendHelloResponse(int listeningPort, String clientIP, int clientPort, String peerID) {
        JSONObject portInfo = new JSONObject();
        portInfo.put("ListeningPort", listeningPort);
        portInfo.put("nodeID", Node.getInstance().getNodeConfig().getNodeID());
//        RequestMessage helloResponse = HelloResponseCreator.createHelloResponseMessage(portInfo);
        RequestMessage helloResponse = MessageCreator.createSpecificMessage(portInfo,"HelloResponse", peerID);
        Node.getInstance().sendMessageToPeer(clientIP, clientPort, helloResponse);
    }

    public void requestBlockchainHash() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ListeningPort",Node.getInstance().getNodeConfig().getListenerPort());
//        RequestMessage blockChainRequest = BlockChainHashRequestCreator.createBlockChainHashRequest(jsonObject);
        RequestMessage blockChainRequest = MessageCreator.createMessage(jsonObject,"BlockChainHashRequest");
        blockChainRequest.addHeader("keepActive", "false");
        BlockchainRequester.getInstance().setBlockchainRequest(Node.getInstance().getNodeConfig().getNeighbours().size());
        Node.getInstance().broadcast(blockChainRequest);
        log.info("requestBlockchainHash");
    }

    public void requestBlockchain() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ListeningPort",Node.getInstance().getNodeConfig().getListenerPort());
        RequestMessage blockChainRequest = BlockChainRequestCreator.createBlockChainRequest(jsonObject);
        blockChainRequest.addHeader("keepActive", "false");
        Node.getInstance().broadcast(blockChainRequest);
    }

    public void requestBlockchainFromPeer(String ip, int listeningPort) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ListeningPort",Node.getInstance().getNodeConfig().getListenerPort());
//        RequestMessage blockChainRequest = BlockChainRequestCreator.createBlockChainRequest(jsonObject);
        RequestMessage blockChainRequest = MessageCreator.createMessage(jsonObject,"BlockChainRequest");
        blockChainRequest.addHeader("keepActive", "false");
        Node.getInstance().sendMessageToPeer(ip,listeningPort,blockChainRequest);
    }

    public void sendSignedBlockChain(String ip, int listeningPort, String signedBlockchain, String blockchainHash) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("signedBlockchain", signedBlockchain);
        jsonObject.put("blockchainHash", blockchainHash);
        jsonObject.put("publicKey",KeyGenerator.getInstance().getPublicKeyAsString());
        RequestMessage blockSignMessage = BlockChainSignCreator.createBlockChainSignRequest(jsonObject);
        Node.getInstance().sendMessageToPeer(ip, listeningPort, blockSignMessage);
        log.info("sendSignedBlockChain");
        System.out.println("sendSignedBlockChain");
    }

    public void sendBlockchainToPeer(String ip, int listeningPort, String jsonBlockchain, int blockchainLength) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("blockchain", jsonBlockchain);
        jsonObject.put("blockchainLength", blockchainLength);
        RequestMessage blockchainSendMessage = BlockchainSendMessageCreator.createBlockchainSendMessage(jsonObject);
        Node.getInstance().sendMessageToPeer(ip,listeningPort,blockchainSendMessage);
        log.info("blockchain sent");
        System.out.println("blockchain sent");
    }


    public void broadCastBlock(Block block) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("block",blockToJSON(block));
//        RequestMessage blockMessage = BlockMessageCreator.createBlockMessage(jsonObject);
        RequestMessage blockMessage = MessageCreator.createMessage(jsonObject,"BlockBroadcast");
        blockMessage.addHeader("keepActive", "false");
        Node.getInstance().broadcast(blockMessage);
    }

    public void broadCastBlockTest(Block block) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("block",blockToJSON(block));
//        RequestMessage blockMessage = BlockMessageCreator.createBlockMessage(jsonObject);
        RequestMessage blockMessage = MessageCreator.createMessage(jsonObject,"BlockBroadcast");
        blockMessage.addHeader("keepActive", "false");
        blockMessage.addHeader("messageType", "BlockBroadcast");
        Node.getInstance().sendMessageToPeer("192.168.8.100",49211,blockMessage);
    }

    public void sendAgreement(String signedBlock, String blockHash) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("signedBlock", signedBlock);
        jsonObject.put("blockHash", blockHash);
        jsonObject.put("publicKey", KeyGenerator.getInstance().getPublicKeyAsString());
        RequestMessage agreementMessage = MessageCreator.createMessage(jsonObject, "Agreement");
        agreementMessage.addHeader("keepActive", "false");
        Node.getInstance().broadcast(agreementMessage);
    }

    public void sendAgreementTest(String signedBlock, String blockHash) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("signedBlock", signedBlock);
        jsonObject.put("blockHash", blockHash);
        jsonObject.put("publicKey", KeyGenerator.getInstance().getPublicKeyAsString());
//        RequestMessage agreementMessage = AgreementCreator.createAgreementRequest(jsonObject);
        RequestMessage agreementMessage = MessageCreator.createMessage(jsonObject, "Agreement");
        agreementMessage.addHeader("keepActive", "false");
//        Node.getInstance().broadcast(agreementMessage);
        Node.getInstance().sendMessageToPeer("192.168.8.100", 49211, agreementMessage);
    }

    public void requestAdditionalData(String ip, int listeningPort, String blockHash, String signedBlock) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("signedBlock", signedBlock);
        jsonObject.put("blockHash", blockHash);
        jsonObject.put("ListeningPort",Node.getInstance().getNodeConfig().getListenerPort());
        jsonObject.put("publicKey", KeyGenerator.getInstance().getPublicKeyAsString());
        RequestMessage dataRequestMessage = MessageCreator.createMessage(jsonObject,"RequestAdditionalData");
        dataRequestMessage.addHeader("keepActive", "false");
        Node.getInstance().sendMessageToPeer(ip,listeningPort, dataRequestMessage);
        System.out.println("Additional Data Requested");
    }

    public void requestPeerDetails(String peerID) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("peerID", peerID);
        jsonObject.put("listeningPort",Node.getInstance().getNodeConfig().getListenerPort());
        RequestMessage peerDetailsRequestMessage = MessageCreator.createMessage(jsonObject, "RequestPeerDetails");
        Node.getInstance().sendMessageToPeer("127.0.0.1", 49154, peerDetailsRequestMessage);
        log.info("Peer Details Requested");
    }

    public void requestTransactionData(JSONObject requestDetails, Neighbour dataOwner) {
        System.out.println(requestDetails);
        requestDetails.put("listeningPort", Node.getInstance().getNodeConfig().getListenerPort());
        requestDetails.put("nodeID", Node.getInstance().getNodeId());
        RequestMessage transactionDataRequestMessage = MessageCreator.createSpecificMessage(requestDetails
                , "RequestTransactionData", dataOwner.getPeerID());
        transactionDataRequestMessage.addHeader("keepActive", "false");
        Node.getInstance().sendMessageToPeer(dataOwner.getIp(), dataOwner.getPort(), transactionDataRequestMessage);
        log.info("Transaction Data Requested from: {}", dataOwner.getPeerID());
    }

    public void sendTransactionData(JSONObject transactionDetails, String ip, int listeningPort, String dataRequester) {
        String signedData = ChainUtil.getInstance().getHash(transactionDetails.toString());
        String digitalSignature = ChainUtil.digitalSignature(signedData);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("digitalSignature", digitalSignature);
        jsonObject.put("signedData", signedData);
        jsonObject.put("transactionDetails", transactionDetails);
        RequestMessage transactionDetailsMessage = MessageCreator.createSpecificMessage(jsonObject,
                "TransactionDetails", dataRequester);
        Node.getInstance().sendMessageToPeer(ip, listeningPort, transactionDetailsMessage);
        log.info("Transaction Details Sent to:  {}", dataRequester);
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
