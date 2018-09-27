package network.communicationHandler;

import UI.ValidateProposal;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.consensus.Consensus;
import core.consensus.ConsensusOld;
import network.Node;
import org.json.JSONObject;
//import com.google.gson.JsonParser;
import core.blockchain.*;
//import org.codehaus.jackson.map.ObjectMapper;

import javax.swing.*;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Map;

public class RequestHandler {

    private static RequestHandler requestHandler;
    private int listeningPort;

    private RequestHandler() {}

    public static RequestHandler getInstance() {
        if(requestHandler == null) {
            requestHandler = new RequestHandler();
        }
        return requestHandler;
    }

    public void handleRequest(Map headers, String data) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, IOException, SignatureException, InvalidKeyException, ParseException, SQLException {
        System.out.println("********requestHandler*******");
        String messageType = (String)headers.get("messageType");
        String peerID = (String)headers.get("sender");
        switch (messageType) {

            //request of the old protocol
            case "TransactionProposal":
                System.out.println("TransactionProposalRequest");
                handleTransactionProposalRequest(data);
                break;

            case "TransactionValidation":
                System.out.println("TransactionProposalResponse");
                handleTransactionProposalResponse(data);
                break;

            case "AgreementRequest":
                System.out.println("AgreementRequest");
                handleAgreementRequest(data);
                break;

            case "AgreementResponse":
                System.out.println("AgreementResponse");
                handleAgreementResponse(data);
                break;


                //request of the new protocol
            case "BlockBroadcast":
                handleBroadcastBlock(data);
                break;

            case "IPResponse":
                handleIPResponse(data);
                break;

            case "Hello":
                handleHelloRequest(data);
                break;

            case "HelloResponse":
                handleHelloResponse(data);
                break;

            case "BlockChainHashRequest":
                System.out.println("BlockChainHashRequest");
                handleBlockChainHashRequest(data);

            case "BlockChainSign":
                System.out.println("BlockChainSign");
                System.out.println(data);
                handleBlockChainSignRequest(data);

            case "BlockChainRequest":
                System.out.println("BlockChainRequest");
                handleBlockChainRequest(data);

            case "BlockchainSend":
                System.out.println("BlockchainSend");
                handleReceivedBlockchainRequest(data,peerID);

            case "Agreement":
                System.out.println("Agreement");
                handleReceivedAgreement(data,peerID);

            case "RequestAdditionalData":
                System.out.println("RequestAdditionalData");
                handleRequestAdditionalData(data,peerID);

            default:
                System.out.println("default");

        }
    }

    //handlers in the new protocol

    public void handleIPResponse(String data) throws IOException {
        Node.getInstance().joinNetwork(data);
    }

    public void handleHelloRequest(String data) {
        JSONObject clientInfo = new JSONObject(data);
        String ip = clientInfo.getString("ip");
        int listeningPort = clientInfo.getInt("ListeningPort");
        MessageSender.getInstance().sendHelloResponse(Node.getInstance().getNodeConfig().getListenerPort(),ip, listeningPort);
        Node.getInstance().addActiveNeighbour(ip, listeningPort);
    }

    public void handleHelloResponse(String data) {
        JSONObject clientInfo = new JSONObject(data);
        String ip = clientInfo.getString("ip");
        int listeningPort = clientInfo.getInt("ListeningPort");
        Node.getInstance().addActiveNeighbour(ip, listeningPort);
    }

    public void handleBlockChainHashRequest(String data) throws NoSuchAlgorithmException, IOException, SignatureException, NoSuchProviderException, InvalidKeyException, InvalidKeySpecException {
        JSONObject clientInfo = new JSONObject(data);
        String ip = clientInfo.getString("ip");
        int listeningPort = clientInfo.getInt("ListeningPort");
        Consensus.getInstance().handleBlockchainHashRequest(ip,listeningPort);
    }

    public void handleBlockChainSignRequest(String data) throws NoSuchAlgorithmException, IOException, SignatureException, NoSuchProviderException, InvalidKeyException, InvalidKeySpecException {
        JSONObject jsonObject = new JSONObject(data);
        String ip = jsonObject.getString("ip");
        int listeningPort = jsonObject.getInt("ListeningPort");
        String signedBlockchain = jsonObject.getString("signedBlockchain");
        String blockchainHash = jsonObject.getString("blockchainHash");
        String publicKey = jsonObject.getString("publicKey"); //get from header
        Consensus.getInstance().handleReceivedSignedBlockchain(publicKey,ip,listeningPort,signedBlockchain,blockchainHash);
    }

    public void handleBlockChainRequest(String data) {
        JSONObject jsonObject = new JSONObject(data);
        String ip = jsonObject.getString("ip");
        int listeningPort = jsonObject.getInt("ListeningPort");
        Consensus.getInstance().sendBlockchain(ip,listeningPort);
    }

    public void handleReceivedBlockchainRequest(String data,String publicKey) throws ParseException, NoSuchAlgorithmException, IOException, SQLException, NoSuchProviderException, InvalidKeySpecException {
        JSONObject jsonObject = new JSONObject(data);
        String ip = jsonObject.getString("ip");
        int listeningPort = jsonObject.getInt("ListeningPort");
        int blockchainLength = jsonObject.getInt("blockchainLength");
        JSONObject jsonBlockchain = new JSONObject(jsonObject.getString("blockchain"));
        Consensus.getInstance().addReceivedBlockchain(publicKey,jsonBlockchain,blockchainLength);
    }

    public void handleReceivedAgreement(String data, String publickey) throws NoSuchAlgorithmException, IOException, SignatureException, NoSuchProviderException, InvalidKeyException, InvalidKeySpecException {
        JSONObject jsonObject = new JSONObject(data);
        String ip = jsonObject.getString("ip");
        int listeningPort = jsonObject.getInt("ListeningPort");
        String signedBlock = jsonObject.getString("signedBlock");
        String blockHash = jsonObject.getString("blockHash");
        Consensus.getInstance().handleReceivedAgreement(signedBlock,blockHash,publickey);
    }

    public void handleRequestAdditionalData(String data, String publickey) {
        JSONObject jsonObject = new JSONObject(data);
        String ip = jsonObject.getString("ip");
        int listeningPort = jsonObject.getInt("ListeningPort");
        String signedBlock = jsonObject.getString("signedBlock");
        String blockHash = jsonObject.getString("blockHash");

    }


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

    //handlers in the old protocol

    public void handleTransactionProposalRequest(String data) throws IOException, InvalidKeySpecException,
            InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException {
        System.out.println("handleTransactionProposalRequest");
        TransactionProposal proposal = this.JSONToProposal(data);

        JFrame frame = new JFrame("Validate Transaction");

        frame.setContentPane(new ValidateProposal(proposal).getPanel());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();

        frame.setVisible(true);

        //proposal.isValid();
    }

    public void handleTransactionProposalResponse(String data) throws NoSuchAlgorithmException, IOException,
            SignatureException, NoSuchProviderException, InvalidKeyException, InvalidKeySpecException {
        System.out.println(data);
        System.out.println("handleTransactionProposalResponse");
        TransactionResponse response = this.JSONToResponse(data);
        System.out.println("signature in response: "+response.getSignature());
        response.addResponse();
    }

    public void handleAgreementRequest(String data) throws InvalidKeySpecException, NoSuchAlgorithmException,
            NoSuchProviderException, IOException, SignatureException, InvalidKeyException {
        System.out.println("handleAgreementRequest");
        JSONObject receivedJSONObject = new JSONObject(data);
        String JSONBlock = (String) receivedJSONObject.get("block");
        System.out.println("Received Block");
        System.out.println(JSONBlock);
        Block decodedBLock = JSONStringToBlock(JSONBlock);
        ConsensusOld.getInstance().handleAgreementRequest(decodedBLock);
    }

    public void handleAgreementResponse(String data) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException, IOException, SignatureException, InvalidKeyException, ParseException, SQLException {
        System.out.println("handleAgreementResponse");
        JSONObject receivedJSONObject = new JSONObject(data);
        System.out.println(receivedJSONObject.toString());
        String JSONBlock = (String) receivedJSONObject.get("block");
        String agreement = (String) receivedJSONObject.get("agreement");
        String signature = (String) receivedJSONObject.get("signature");
        String publicKey = (String) receivedJSONObject.get("publickey");
        Block decodedBLock = JSONStringToBlock(JSONBlock);
        ConsensusOld.getInstance().handleAgreementResponse(decodedBLock,publicKey,signature,agreement);
    }

    public void handleBroadcastBlock(String data) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, IOException, ParseException, SQLException {
        System.out.println("handleBroadcastBlock");
        JSONObject receivedJSONObject = new JSONObject(data);
        String JSONBlock = (String) receivedJSONObject.get("block");
        Block decodedBLock = JSONStringToBlock(JSONBlock);
        ConsensusOld.getInstance().blockHandler(decodedBLock);

        Consensus.getInstance().handleNonApprovedBlock(decodedBLock);
    }

    public Block JSONStringToBlock(String JSONblock) {
        JSONObject jsonObject = new JSONObject(JSONblock);
        Gson gson = new GsonBuilder().serializeNulls().create();
        Block block = gson.fromJson(JSONblock,Block.class);

        return block;
    }

    public TransactionProposal JSONToProposal(String data) throws IOException {
        JSONObject jsonObject = new JSONObject(data);
        Gson gson = new GsonBuilder().serializeNulls().create();
        TransactionProposal proposal = gson.fromJson(String.valueOf(jsonObject.get("transactionProposal")),TransactionProposal.class);
        return proposal;
    }

    public TransactionResponse JSONToResponse(String data) throws IOException {
        JSONObject jsonObject = new JSONObject(data);
        Gson gson = new GsonBuilder().serializeNulls().create();
        System.out.println(jsonObject.get("transactionResponse"));
        TransactionResponse response = gson.fromJson(String.valueOf(jsonObject.get("transactionResponse")),TransactionResponse.class);
        System.out.println("public key" + response.getValidator().getValidator());
        System.out.println("signature" + response.getSignature());
        System.out.println("proposal id " + response.getProposalID());
        return response;
    }



}
