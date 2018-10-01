package core.consensus;

import chainUtil.ChainUtil;
import core.blockchain.Block;
import network.communicationHandler.MessageSender;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class BlockchainRequester {

    private static BlockchainRequester blockchainRequester;
    private ArrayList<BlockchainShare> blockchainShareDetails;
    private ArrayList<BlockchainReceiver> blockchainReceiveDetails;
    private int blockchainRequest;
    private String requestedBlockchainHash;

    private BlockchainRequester() {
        blockchainShareDetails = new ArrayList<>();
        blockchainReceiveDetails = new ArrayList<>();
        blockchainRequest = 0;
    }

    public static BlockchainRequester getInstance() {
        if (blockchainRequester == null) {
            blockchainRequester = new BlockchainRequester();
        }
        return blockchainRequester;
    }

    //blockchain request methods
    public synchronized void handleBlockchainHashRequest(String ip, int listeningPort) {
//        if(Blockchain.getInstance().getBlockchainArray().size() >=1) {
//            sendSignedBlockChain(ip, listeningPort);
//        }
    }

    public synchronized void sendSignedBlockChain(String ip, int listeningPort) {
        BlockchainShare blockchainShare = new BlockchainShare(ip, listeningPort);
        String blockchainHash = ChainUtil.getInstance().getBlockChainHash(blockchainShare.getBlockChainInstance());
        blockchainShareDetails.add(blockchainShare);
        String signedBlockchainHash = ChainUtil.getInstance().digitalSignature(blockchainHash);
        MessageSender.getInstance().sendSignedBlockChain(ip, listeningPort, signedBlockchainHash, blockchainHash);

    }

    //no need of synchronizing
    public void sendBlockchain(String ip, int listeningPort) throws Exception {
        JSONObject blockchainInfo = ChainUtil.getInstance().getBlockchain(0);
        MessageSender.getInstance().sendBlockchainToPeer(
                ip,
                listeningPort,
                blockchainInfo.getString("blockchain"),
                blockchainInfo.getInt("count"));
    }

    //no need of synchronizing
    public BlockchainShare getBlockchainShareFromIP(String ip, int listeningPort) {
        String id = ip + String.valueOf(listeningPort);
        for (BlockchainShare blockchainShare : blockchainShareDetails) {
            if (id.equals(blockchainShare.getId())) {
                return blockchainShare;
            }
        }
        return null;
    }

    public synchronized void handleReceivedSignedBlockchain(String publicKey, String ip, int listeningPort, String signedBlockchain,
                                                            String blockchainHash) {
//        if(ChainUtil.getInstance().verifyUser())
        if (ChainUtil.getInstance().signatureVerification(publicKey, signedBlockchain, blockchainHash)) {
            BlockchainReceiver blockchainReceiver = new BlockchainReceiver(publicKey, ip, listeningPort, signedBlockchain, blockchainHash);
            blockchainReceiveDetails.add(blockchainReceiver);
            blockchainRequest -= 1;
            if (blockchainRequest == 0) {
                requestBlockchain();
            }
        }
    }

    public synchronized void addReceivedBlockchain(String publicKey, JSONObject blockchain, int blockchainLength) throws SQLException, ParseException {
//        LinkedList<Block> blockchainArray = new LinkedList<>();
//        for(int i = 0; i< blockchainLength; i++) {
//            blockchainArray.add(RequestHandler.getInstance().JSONStringToBlock(blockchain.getString(String.valueOf(i))));
//        }
//
//        String blockchainHash = ChainUtil.getInstance().getBlockChainHash(blockchainArray);
//        if(requestedBlockchainHash.equals(blockchainHash)) {
//            addBlockchain(blockchainArray);
//        }
    }

    public void addBlockchain(LinkedList<Block> blockchain) throws SQLException, ParseException {
//        //add to blockchain
//        for(int i = 1; i< blockchain.size(); i++ ) {
//            Blockchain.getInstance().addBlock(blockchain.get(i));
//        }
    }

    public int getBlockchainRequest() {
        return blockchainRequest;
    }

    public synchronized void setBlockchainRequest(int blockchainRequest) {
        this.blockchainRequest = blockchainRequest;
    }

    public synchronized String findCorrectBlockchain() {
        HashMap<String, Integer> counter = new HashMap<String, Integer>();
        for (BlockchainReceiver blockchainReceiver : blockchainReceiveDetails) {
            if (counter.containsKey(blockchainReceiver.getId())) {
                counter.put(blockchainReceiver.getId(), counter.get(blockchainReceiver.getId()) + 1);
            } else {
                counter.put(blockchainReceiver.getId(), 1);
            }
        }
        System.out.println(counter);
        int max = 0;
        String publicKey = "";
        for (String key : counter.keySet()) {
            if (counter.get(key) > max) {
                max = counter.get(key);
                publicKey = key;
            }
        }
        return publicKey;
    }


    //no need of synchronizing
    public void requestBlockchain() {
        BlockchainReceiver blockchainReceiver = getBlockchainReceiverfromPK(findCorrectBlockchain());
        requestedBlockchainHash = blockchainReceiver.getBlockchainHash();
        MessageSender.getInstance().requestBlockchainFromPeer(blockchainReceiver.getIp(), blockchainReceiver.getListeningPort());
    }

    //no need of synchronizing
    public BlockchainReceiver getBlockchainReceiverfromPK(String publicKey) {
        for (BlockchainReceiver blockchainReceiver : blockchainReceiveDetails) {
            if (publicKey.equals(blockchainReceiver.getId())) {
                return blockchainReceiver;
            }
        }
        return null;
    }
}
