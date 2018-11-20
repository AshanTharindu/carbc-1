package network.communicationHandler;

import DataOwners.ServiceStation;
import chainUtil.ChainUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.consensus.BlockchainRequester;
import core.consensus.Consensus;
import core.blockchain.Block;
import core.consensus.DataCollector;
import network.Neighbour;
import network.Node;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.text.ParseException;

public class Handler extends Thread{
    private final Logger log = LoggerFactory.getLogger(Handler.class);
    String messageType;
    String data;
    String peerID;


    public Handler(String messageType, String data, String peerID){
        this.messageType = messageType;
        this.data = data;
        this.peerID = peerID;
    }

    @Override
    public void run() {
        try{
            switch (messageType) {
                //request of the new protocol
                case "BlockBroadcast":
                    log.info("Handler invoked: Block Broadcast");
                    handleBroadcastBlock();
                    break;

                case "IPResponse":
                    log.info("Handler invoked: IPResponse");
                    handleIPResponse();
                    break;

                case "Hello":
                    log.info("Handler invoked: Hello Request");
                    handleHelloRequest();
                    break;

                case "HelloResponse":
                    log.info("Handler invoked: Hello Response");
                    handleHelloResponse();
                    break;

                case "BlockChainHashRequest":
                    log.info("Handler invoked: Blockchain Request");
                    handleBlockChainHashRequest();
                    break;

                case "BlockChainSign":
                    log.info("Handler invoked: Blockchain Sign");
                    handleReceivedSignedBlockchain();
                    break;

                case "BlockChainRequest":
                    log.info("Handler invoked: Blockchain Request");
                    handleBlockChainRequest();
                    break;


                case "BlockchainSend":
                    log.info("Handler invoked: Blockchain Send");
                    handleReceivedBlockchainRequest();
                    break;


                case "Agreement":
                    log.info("Handler invoked: Agreement");
                    handleReceivedAgreement();
                    break;


                case "RequestedPeerDetails":
                    log.info("Handler invoked: Request Peer Details");
                    handleRequestedPeerDetails(data);
                    break;

                case "RequestTransactionData":
                    log.info("Handler invoked: Request Transaction Data");
                    handleRequestTransactionData(data);
                    break;

                case "TransactionDetails":
                    log.info("Handler invoked: Transaction Details");
                    handleReceivedTransactionData(data);
                    break;

                case "RequestAdditionalData":
                    log.info("Handler invoked: Request Additional Data");
                    handleAdditionalDataRequest(data);
                    break;

                case "AdditionalData":
                    log.info("Handler invoked: Additional Data");
                    handleReceivedAdditionalData(data);
                    break;
                case "Test":
                    log.info("Handler invoked: Test");
                    break;

                case "TestSignature":
                    log.info("Handler invoked: Test Signature");
                    checkSignatureVerfication(data);
                    break;

                default:
                    log.info("Handler invoked: Default Request");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void handleIPResponse() throws IOException {
        Node.getInstance().joinNetwork(data);
    }

    public void handleHelloRequest() {
        JSONObject clientInfo = new JSONObject(data);
        String ip = clientInfo.getString("ip");
        String peerID = clientInfo.getString("nodeID");
        int listeningPort = clientInfo.getInt("ListeningPort");
        MessageSender.sendHelloResponse(Node.getInstance().getNodeConfig().getListenerPort(),ip, listeningPort, peerID);
        Node.getInstance().addActiveNeighbour(peerID, ip, listeningPort);
    }


    public void handleHelloResponse() {
        JSONObject clientInfo = new JSONObject(data);
        String ip = clientInfo.getString("ip");
        String peerID = clientInfo.getString("nodeID");
        int listeningPort = clientInfo.getInt("ListeningPort");
        Node.getInstance().addActiveNeighbour(peerID, ip, listeningPort);
    }

    public void handleBlockChainHashRequest() throws SQLException {
        JSONObject clientInfo = new JSONObject(data);
        String ip = clientInfo.getString("ip");
        int listeningPort = clientInfo.getInt("ListeningPort");
        Neighbour blockchainRequester = new Neighbour(peerID, ip, listeningPort);
        BlockchainRequester.getInstance().handleBlockchainHashRequest(blockchainRequester);
    }

    public void handleReceivedSignedBlockchain() {
        JSONObject jsonObject = new JSONObject(data);
        String ip = jsonObject.getString("ip");
        int listeningPort = jsonObject.getInt("listeningPort");
        String signedBlockchain = jsonObject.getString("signedBlockchain");
        String blockchainHash = jsonObject.getString("blockchainHash");
        String publicKey = jsonObject.getString("publicKey");
        Neighbour sender = new Neighbour(peerID, ip, listeningPort, publicKey);
        BlockchainRequester.getInstance().handleReceivedSignedBlockchain(sender, signedBlockchain, blockchainHash);
    }

    public void handleBlockChainRequest() throws Exception {
        JSONObject jsonObject = new JSONObject(data);
        String ip = jsonObject.getString("ip");
        int listeningPort = jsonObject.getInt("ListeningPort");
        BlockchainRequester.getInstance().sendBlockchain(ip,listeningPort);
    }

    public void handleReceivedBlockchainRequest() throws ParseException, NoSuchAlgorithmException, IOException, SQLException, NoSuchProviderException, InvalidKeySpecException {
        JSONObject jsonObject = new JSONObject(data);
        String ip = jsonObject.getString("ip");
        JSONArray jsonBlockchain = jsonObject.getJSONArray("blockchain");
        BlockchainRequester.getInstance().addReceivedBlockchain(peerID,jsonBlockchain);
    }

    public void handleReceivedAgreement(){
        log.info("inside handleReceivedAgreement method");
        JSONObject jsonObject = new JSONObject(data);
        String signedBlock = jsonObject.getString("signedBlock");
        String blockHash = jsonObject.getString("blockHash");
        String publicKey = jsonObject.getString("publicKey");
        Consensus.getInstance().handleReceivedAgreement("sign", signedBlock, blockHash, publicKey);
    }

    // 0-> block comes
    public void handleBroadcastBlock() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, IOException, ParseException, SQLException {
        log.info("Handling block broadcast: Inside Handler/handleBroadcastBlock");
        JSONObject receivedJSONObject = new JSONObject(data);
        String JSONBlock = (String)receivedJSONObject.get("block");
        Block decodedBlock = JSONStringToBlock(JSONBlock);
        Consensus.getInstance().handleNonApprovedBlock(decodedBlock);
    }

    public Block JSONStringToBlock(String JSONblock){
        Gson gson = new GsonBuilder().serializeNulls().create();
        Block block = gson.fromJson(JSONblock, Block.class);
        return block;
    }

    public void handleRequestedPeerDetails(String data) {
        if(peerID.equals("Bootstrap Node")) {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject peerDetails = jsonObject.getJSONObject("peerDetails");
            String peerID = peerDetails.getString("nodeID");
            String signature = jsonObject.getString("signature");
            String signedData = jsonObject.getString("signedData");
            String publicKey = jsonObject.getString("publicKey");
            log.info("Peer Details Received");
            DataCollector.getInstance().handleRequestedPeerDetails(peerDetails,signature,signedData);
        }
    }

    public void handleRequestTransactionData(String data) {
        JSONObject jsonObject = new JSONObject(data);
        String dataOwner = jsonObject.getString("dataOwner");
        String transactionType = jsonObject.getString("transactionType");
        String dataRequester = jsonObject.getString("nodeID");
        String vehicleID = jsonObject.getString("vehicleID");
        String date = jsonObject.getString("date");
        String signature = jsonObject.getString("signature");
        String signedData = jsonObject.getString("signedData");
        String ip = jsonObject.getString("ip");
        int listeningPort = jsonObject.getInt("listeningPort");

        if(transactionType.equals("ServiceRepair")) {
            ServiceStation serviceStation = new ServiceStation();
            serviceStation.getServiceRecord(vehicleID, signature, signedData, dataRequester, ip, listeningPort);
        }

    }

    public void handleReceivedTransactionData(String data) {
        JSONObject jsonObject = new JSONObject(data);
        JSONObject transactionData = jsonObject.getJSONObject("transactionDetails");
        String signature = jsonObject.getString("digitalSignature");
        String signedData = jsonObject.getString("signedData");
        DataCollector.getInstance().handleReceivedTransactionData(transactionData, signature, signedData, peerID);
    }

    public void handleAdditionalDataRequest(String data) throws SQLException {
        JSONObject jsonObject = new JSONObject(data);
        String dataOwner = jsonObject.getString("dataOwner");
        String ip = jsonObject.getString("ip");
        int listeningPort = jsonObject.getInt("listeningPort");

        if(dataOwner.equals(Node.getInstance().getNodeId())) {
            Neighbour dataRequester = new Neighbour(peerID, ip, listeningPort);
            String blockHash = jsonObject.getString("blockHash");
            String signature = jsonObject.getString("signature");
            String publicKey = jsonObject.getString("publicKey");
            dataRequester.setPublicKey(publicKey);
            DataCollector.getInstance().handleReceivedAdditionalData(blockHash, signature, dataRequester);
        } else {
            log.info("Invalid Additional Data Request");
        }
    }

    public void handleReceivedAdditionalData(String data) {
        JSONObject jsonObject = new JSONObject(data);
        String blockHash = jsonObject.getString("blockHash");
        String additionalData = jsonObject.getString("additionalData");
        Consensus.getInstance().handleReceivedAdditionalData(blockHash, additionalData);
    }

    //test method
    public void checkSignatureVerfication(String data) {
        JSONObject jsonObject = new JSONObject(data);
        String pk = jsonObject.getString("pk");
        String signature = jsonObject.getString("signature");
        String signedData = jsonObject.getString("signedData");
        boolean verifiction = ChainUtil.signatureVerification(pk, signature, signedData);
        log.info("signature verified");
    }

}
