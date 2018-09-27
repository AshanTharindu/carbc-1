package core.consensus;

import chainUtil.ChainUtil;
import core.blockchain.Block;
import core.blockchain.Blockchain;
import core.blockchain.Transaction;
import network.communicationHandler.MessageSender;
import network.communicationHandler.RequestHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Consensus {

    private static Consensus consensus;
    private ArrayList<Block> nonApprovedBlocks;
    private ArrayList<AgreementCollector> agreementCollectors;
    private ArrayList<BlockchainShare> blockchainShareDetails;
    private ArrayList<BlockchainReceiver> blockchainReceiveDetails;
    private int blockchainRequest;
    private String requestedBlockchainHash;

    //to automate agreement process
    private ArrayList<Transaction> addedTransaction;

    private Consensus() {
        nonApprovedBlocks = new ArrayList<>();
        agreementCollectors = new ArrayList<>();
        blockchainShareDetails = new ArrayList<>();
        blockchainReceiveDetails = new ArrayList<>();
        blockchainRequest = 0;
    }

    public static Consensus getInstance() {
        if (consensus == null) {
            consensus = new Consensus();
        }
        return consensus;
    }

    //block broadcasting and sending agreements

    public void handleNonApprovedBlock(Block block) {
        if(!isDuplicateBlock(block)) {
            //notify if relevant
            nonApprovedBlocks.add(block);
            agreementCollectors.add(new AgreementCollector(block));
        }
    }

    public boolean isDuplicateBlock(Block block) {
        if(nonApprovedBlocks.contains(block)) {
            return true;
        }
        return false;
    }

    public void sendAgreementForBlock(Block block) {
        String blockHash = ChainUtil.getInstance().getBlockHash(block);
        String signedBlock = ChainUtil.getInstance().digitalSignature(blockHash);
        MessageSender.getInstance().sendAgreement(signedBlock,blockHash);
        System.out.println("agreement sent");
    }

    public void handleAgreement(Agreement agreement) {
        getAgreementCollector(agreement.getBlockHash()).addAgreementForBlock(agreement);
    }

    private AgreementCollector getAgreementCollector(String id) {
        for(AgreementCollector agreementCollector: this.agreementCollectors) {
            if(agreementCollector.getAgreementCollectorId().equals(id)) {
                return agreementCollector;
            }
        }
        return null;
    }

    public void handleReceivedAgreement(String signedBlock, String blockHash, String publicKey) {
        handleAgreement(new Agreement(blockHash,signedBlock,publicKey));
    }


    //blockchain request methods
    public synchronized void  handleBlockchainHashRequest(String ip, int listeningPort) {
        if(Blockchain.getInstance().getBlockchainArray().size() >=1) {
            sendSignedBlockChain(ip, listeningPort);
        }
    }

    public void sendSignedBlockChain(String ip, int listeningPort) {
        BlockchainShare blockchainShare = new BlockchainShare(ip, listeningPort);
        String blockchainHash = ChainUtil.getInstance().getBlockChainHash(blockchainShare.getBlockChainInstance());
        blockchainShareDetails.add(blockchainShare);
        String signedBlockchainHash = ChainUtil.getInstance().digitalSignature(blockchainHash);
        MessageSender.getInstance().sendSignedBlockChain(ip,listeningPort,signedBlockchainHash,blockchainHash);

    }

    public void sendBlockchain(String ip, int listeningPort) {
        BlockchainShare blockchainShare = getBlockchanSharefromIP(ip,listeningPort);
        String jsonBlockchain = ChainUtil.getInstance().getBlockchainAsJsonString(blockchainShare.getBlockChainInstance());
        MessageSender.getInstance().sendBlockchainToPeer(ip,listeningPort,jsonBlockchain, blockchainShare.getBlockChainInstance().size());
    }

    public BlockchainShare getBlockchanSharefromIP(String ip, int listeningPort) {
        String id = ip+String.valueOf(listeningPort);
        for(BlockchainShare blockchainShare : blockchainShareDetails) {
            if(id.equals(blockchainShare.getId())) {
                return blockchainShare;
            }
        }
        return null;
    }

    public synchronized void handleReceivedSignedBlockchain(String publicKey, String ip,int listeningPort,String signedBlockchain,
                                               String blockchainHash) {
//        if(ChainUtil.getInstance().verifyUser())
        if(ChainUtil.getInstance().signatureVerification(publicKey,signedBlockchain,blockchainHash)){
            BlockchainReceiver blockchainReceiver = new BlockchainReceiver(publicKey,ip,listeningPort,signedBlockchain,blockchainHash);
            blockchainReceiveDetails.add(blockchainReceiver);
            blockchainRequest -=1;
            if(blockchainRequest == 0) {
                requestBlockchain();
            }
        }

    }

    public void addReceivedBlockchain(String publicKey, JSONObject blockchain, int blockchainLength) throws SQLException, ParseException {
        LinkedList<Block> blockchainArray = new LinkedList<>();
        for(int i = 0; i< blockchainLength; i++) {
            blockchainArray.add(RequestHandler.getInstance().JSONStringToBlock(blockchain.getString(String.valueOf(i))));
        }

        String blockchainHash = ChainUtil.getInstance().getBlockChainHash(blockchainArray);
        if(requestedBlockchainHash.equals(blockchainHash)) {
            addBlockchain(blockchainArray);
        }
    }

    public void addBlockchain(LinkedList<Block> blockchain) throws SQLException, ParseException {
        //add to blockchain
        for(int i = 1; i< blockchain.size(); i++ ) {
            Blockchain.getInstance().addBlock(blockchain.get(i));
        }
    }

    public int getBlockchainRequest() {
        return blockchainRequest;
    }

    public void setBlockchainRequest(int blockchainRequest) {
        this.blockchainRequest = blockchainRequest;
    }

    public String findCorrectBlockchain() {
        HashMap<String, Integer> counter = new HashMap<String, Integer>();
        for(BlockchainReceiver blockchainReceiver: blockchainReceiveDetails) {
            if (counter.containsKey(blockchainReceiver.getId())) {
                counter.put(blockchainReceiver.getId(), counter.get(blockchainReceiver.getId()) + 1);
            } else {
                counter.put(blockchainReceiver.getId(), 1);
            }
        }
        System.out.println(counter);

        int max = 0;
        String publicKey = "";

        for(String key : counter.keySet()) {
            if(counter.get(key) > max) {
                max = counter.get(key);
                publicKey = key;
            }
        }
        return publicKey;
    }


    public void requestBlockchain() {
        BlockchainReceiver blockchainReceiver = getBlockchainReceiverfromPK(findCorrectBlockchain());
        requestedBlockchainHash = blockchainReceiver.getBlockchainHash();
        MessageSender.getInstance().requestBlockchainFromPeer(blockchainReceiver.getIp(),blockchainReceiver.getListeningPort());
    }


    public BlockchainReceiver getBlockchainReceiverfromPK(String publicKey) {
        for(BlockchainReceiver blockchainReceiver: blockchainReceiveDetails) {
            if(publicKey.equals(blockchainReceiver.getId())) {
                return blockchainReceiver;
            }
        }
        return null;
    }

    public void requestAdditionalData(Block block) {
        //String sender = block
        
    }

    public void handleAdditionalDataRequest(String ip, int listeningPort, String signedBlock, String blockHash, String peerID ) {
        String data =  getAdditionalDataForBlock(blockHash).toString();
    }

    public JSONObject getAdditionalDataForBlock(String blockHash) {
        return new JSONObject();
    }

}
