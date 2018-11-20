package controller;

import network.communicationHandler.MessageSender;
import org.json.JSONArray;
import org.json.JSONObject;

import chainUtil.ChainUtil;
import chainUtil.KeyGenerator;
import core.blockchain.Block;
import core.blockchain.BlockBody;
import core.blockchain.BlockHeader;
import core.blockchain.Transaction;
import core.consensus.Consensus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockSender extends Thread {

    private JSONObject data;
    private String vehicleID;
    private String event;
    private final Logger log = LoggerFactory.getLogger(BlockSender.class);

    public BlockSender(String event, String vehicleID, JSONObject data) {
        this.vehicleID = vehicleID;
        this.event = event;
        this.data = data;
    }

    public BlockSender(String event, JSONObject data) {
        this.data = data;
        this.event = event;
    }

    public void run() {
        sendTransaction();
//        switch (event) {
//            case "RegisterVehicle":
//                sendRegisterTransaction();
//                break;
//
//            default:
//                sendTransaction();
//                break;
//        }
    }

    public void sendRegisterTransaction() {
        String sender = KeyGenerator.getInstance().getPublicKeyAsString();
        Transaction transaction = new Transaction("V", sender, "RegisterVehicle", data.toString());
        transaction.setAddress();
        BlockBody blockBody = new BlockBody();
        blockBody.setTransaction(transaction);
        String blockHash = ChainUtil.getInstance().getBlockHash(blockBody);
        BlockHeader blockHeader = new BlockHeader(blockHash);
        Block block = new Block(blockHeader, blockBody);
//        MessageSender.broadCastBlockTest(block);
        Consensus.getInstance().broadcastBlock(block, data.toString());
    }

    public void sendTransaction() {
        String sender = KeyGenerator.getInstance().getPublicKeyAsString();
        Transaction transaction = new Transaction("V", sender, event, data.toString(), vehicleID);
        BlockBody blockBody = new BlockBody();
        blockBody.setTransaction(transaction);
        String blockHash = ChainUtil.getInstance().getBlockHash(blockBody);
        BlockHeader blockHeader = new BlockHeader(blockHash);
        Block block = new Block(blockHeader, blockBody);
        log.info("Transaction Created. Event: ", event);
        Consensus.getInstance().broadcastBlock(block, data.toString());
    }

    public String getDataJsonObject(JSONObject data, String event) {
        String jsonData = null;
        switch (event) {
            case "ServiceRepair":
                jsonData = createServiceRepairJson(data);
                break;
        }

        return jsonData;
    }

    public String createServiceRepairJson(JSONObject data) {
        JSONArray services = data.getJSONArray("services");
        JSONArray sparePartProvider = new JSONArray();
        for(int service = 0; service < services.length(); service++) {
            JSONArray serviceData = services.getJSONObject(service).getJSONArray("serviceData");
            for(int part = 0; part < serviceData.length(); part++) {
                sparePartProvider.put(serviceData.getJSONObject(part).get("seller"));
            }
        }
        JSONObject thirdParty = new JSONObject();
        thirdParty.put("SparePartProvider", sparePartProvider);
        data.put("ThirdParty", thirdParty);

        return data.toString();
    }


}
