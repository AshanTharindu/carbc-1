package core.consensus;

import chainUtil.ChainUtil;
import core.blockchain.Block;
import network.Neighbour;
import network.Node;
import network.communicationHandler.MessageSender;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.ArrayList;

public class TransactionDataCollector {

    private static TransactionDataCollector transactionDataCollector;
    private ArrayList<DataRequester> requestedTransactionDataDetails;
    private final Logger log = LoggerFactory.getLogger(TransactionDataCollector.class);



    private TransactionDataCollector() {
        requestedTransactionDataDetails = new ArrayList<>();
    }

    public static TransactionDataCollector getInstance() {
        if (transactionDataCollector == null) {
            transactionDataCollector = new TransactionDataCollector();
        }
        return transactionDataCollector;
    }

    public void requestTransactionData(String vehicleID, Timestamp date, String peerID) {
        Neighbour dataOwner = Node.getInstance().getPeerDetails(peerID);
        if (dataOwner != null) {
            DataRequester dataRequester = new DataRequester(peerID, vehicleID, dataOwner, date);
            requestedTransactionDataDetails.add(dataRequester);
            requestTransactionDataFromDataOwner(vehicleID, date, dataOwner);
        } else {
            log.info("No Peer Details found for: {}", peerID);
            DataRequester dataRequester = new DataRequester(peerID, vehicleID, date);
            requestedTransactionDataDetails.add(dataRequester);
            MessageSender.getInstance().requestPeerDetails(peerID);
        }
    }

    public void requestTransactionDataFromDataOwner(String vehicleID, Timestamp date, Neighbour dataOwner) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("vehicleID", vehicleID);
        jsonObject.put("dataOwner", dataOwner.getPeerID());
        jsonObject.put("date", date);
        String nodeID = Node.getInstance().getNodeConfig().getNodeID();
        jsonObject.put("signature", ChainUtil.getInstance().digitalSignature(nodeID));
        jsonObject.put("signedData", nodeID);
        MessageSender.getInstance().requestTransactionData(jsonObject, dataOwner);
    }


    public void handleRequestedPeerDetails(JSONObject peerDetails, String signature, String signedData, String publicKey) {
        String ip = peerDetails.getString("ip");
        String peerID = peerDetails.getString("nodeID");
        int listeningPort = peerDetails.getInt("port");

        if (ChainUtil.getInstance().signatureVerification(publicKey, signature, signedData)) {
            Neighbour dataOwner = new Neighbour(peerID, ip, listeningPort);
            DataRequester dataRequester = getDataRequester(peerID);
            dataRequester.setDataOwner(dataOwner);
            requestTransactionDataFromDataOwner(dataRequester.getVehicleID(), dataRequester.getDate(), dataOwner);
        }
    }


    public DataRequester getDataRequester(String peerID) {
        for (DataRequester dataRequester : requestedTransactionDataDetails) {
            if (peerID.equals(dataRequester.getPeerID())) {
                log.info("data requester found for : {}", peerID);
                return dataRequester;
            }
        }
        log.info("No data requester found for : {}", peerID);
        return null;
    }

}
