package core.consensus;

import chainUtil.ChainUtil;
import core.blockchain.Block;
import core.blockchain.BlockInfo;
import core.blockchain.Transaction;
import core.connection.BlockJDBCDAO;
import core.connection.Identity;
import core.smartContract.BlockValidity;
import core.smartContract.TimeKeeper;
import network.communicationHandler.MessageSender;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.Timestamp;
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
    private ArrayList<Block> approvedBlocks;
    private int blockchainRequest;
    private String requestedBlockchainHash;

    //to automate agreement process
    private ArrayList<Transaction> addedTransaction;

    private Consensus() {
        nonApprovedBlocks = new ArrayList<>();
        agreementCollectors = new ArrayList<>();
        blockchainShareDetails = new ArrayList<>();
        blockchainReceiveDetails = new ArrayList<>();
        approvedBlocks = new ArrayList<>();
        blockchainRequest = 0;
    }

    public static Consensus getInstance() {
        if (consensus == null) {
            consensus = new Consensus();
        }
        return consensus;
    }

    //block broadcasting and sending agreements
    public synchronized void handleNonApprovedBlock(Block block) throws NoSuchAlgorithmException, SQLException {

        if(!isDuplicateBlock(block)) {
            nonApprovedBlocks.add(block);
            boolean isPresent = false;
            for (Block b: this.nonApprovedBlocks){
                if (b.getBlockHeader().getPreviousHash().equals(block.getBlockHeader().getPreviousHash())){
                    isPresent = true;
                    break;
                }
            }
            if (!isPresent){
                TimeKeeper timeKeeper = new TimeKeeper(block.getBlockHeader().getPreviousHash());
                timeKeeper.start();
            }

            AgreementCollector agreementCollector = new AgreementCollector(block);
            agreementCollectors.add(agreementCollector);
            agreementCollector.start();

            //TODO: there is a problem here. Regardless of the block validity we anyway append the agreement collector to the arraylist. so whats the point of doing block validity below??

            //now need to check the relevant party is registered as with desired roles
            //if want, we can check the validity of the block creator/transaction creator
            BlockValidity blockValidity = new BlockValidity(block);
            if (blockValidity.isSecondaryPartyValid()){
                //pop up notification to confirm
            }
        }
    }

    public void checkAgreementsForBlock(String preBlockHash) throws SQLException {
        ArrayList<Block> qualifiedBlocks = new ArrayList<>();
        for (Block b: this.nonApprovedBlocks){
            if (b.getBlockHeader().getPreviousHash().equals(preBlockHash)){
                String blockHash = b.getBlockHeader().getHash();
                AgreementCollector agreementCollector = getAgreementCollector(blockHash);

                synchronized (agreementCollectors){
                    if (agreementCollector.getMandatoryValidators().size() == 0){
                        if (agreementCollector.getAgreements().size() >= agreementCollector.getThreshold()){
                            qualifiedBlocks.add(b);
                            this.agreementCollectors.remove(agreementCollector);
                        }
                    }else{
                        //blocks with insufficient agreements
                    }
                }
            }
        }
        addBlockToBlockchain(qualifiedBlocks);
    }

    public Block selectQualifiedBlock(ArrayList<Block> qualifiedBlocks) throws SQLException {
        Block qualifiedBlock = null;

        if (qualifiedBlocks.size() != 0){
            qualifiedBlock = qualifiedBlocks.get(0);
            Timestamp blockTimestamp = qualifiedBlock.getBlockHeader().getBlockTime();

            synchronized (nonApprovedBlocks){
                for (Block b: qualifiedBlocks){
                    if (blockTimestamp.after(b.getBlockHeader().getBlockTime())){
                        this.nonApprovedBlocks.remove(qualifiedBlock);
                        qualifiedBlock = b;
                        blockTimestamp = b.getBlockHeader().getBlockTime();
                    }else{
                        this.nonApprovedBlocks.remove(b);
                    }
                }
            }
            //TODO: for now we discard all delayed blocks. only consider blocks that got enough agreements within the specific time period
//            this.approvedBlocks.add(qualifiedBlock);
        }else{
            //need to restart the timer again
        }
        return qualifiedBlock;
    }

    public void addBlockToBlockchain(ArrayList<Block> qualifiedBlocks) throws SQLException {
        Block block = selectQualifiedBlock(qualifiedBlocks);

        if (block != null){
            BlockInfo blockInfo = new BlockInfo();
            blockInfo.setPreviousHash(block.getBlockHeader().getPreviousHash());
            blockInfo.setHash(block.getBlockHeader().getHash());
            blockInfo.setBlockTime(block.getBlockHeader().getBlockTime());
            blockInfo.setBlockNumber(block.getBlockHeader().getBlockNumber());
            blockInfo.setTransactionId(block.getBlockBody().getTransaction().getTransactionId());
            blockInfo.setSender(block.getBlockBody().getTransaction().getSender());
            blockInfo.setEvent(block.getBlockBody().getTransaction().getEvent());
            blockInfo.setData(block.getBlockBody().getTransaction().getData().toString());
            blockInfo.setAddress(block.getBlockBody().getTransaction().getAddress());

            Identity identity= null;
            if (block.getBlockBody().getTransaction().getTransactionId().substring(0, 1).equals("I")){
                JSONObject body = block.getBlockBody().getTransaction().getData();
                String publicKey = body.getString("publicKey");
                String role = body.getString("role");
                String name = body.getString("name");

                identity = new Identity(block.getBlockHeader().getHash(), publicKey, role, name);
            }
            //TODO: need to check that this is the right block to add based on the previous hash
            BlockJDBCDAO blockJDBCDAO = new BlockJDBCDAO();
            blockJDBCDAO.addBlockToBlockchain(blockInfo, identity);
        }
    }

    //no need of synchronizing
    public boolean isDuplicateBlock(Block block) {
        if(nonApprovedBlocks.contains(block)) {
            return true;
        }
        return false;
    }

    //no need of synchronizing
    public void sendAgreementForBlock(Block block) {
        String blockHash = ChainUtil.getInstance().getBlockHash(block);
        String signedBlock = ChainUtil.getInstance().digitalSignature(blockHash);
        MessageSender.getInstance().sendAgreement(signedBlock,blockHash);
        System.out.println("agreement sent");
    }

    //no need of synchronizing
    public void handleAgreement(Agreement agreement) {
        getAgreementCollector(agreement.getBlockHash()).addAgreementForBlock(agreement);
    }

    //no need of synchronizing
    private AgreementCollector getAgreementCollector(String id) {
        for(AgreementCollector agreementCollector: this.agreementCollectors) {
            if(agreementCollector.getAgreementCollectorId().equals(id)) {
                return agreementCollector;
            }
        }
        return null;
    }

    //no need of synchronizing
    public void handleReceivedAgreement(String signature, String signedBlock, String blockHash, String publicKey) {
        handleAgreement(new Agreement(signature, blockHash, signedBlock, publicKey));
    }


    //blockchain request methods
    public synchronized void  handleBlockchainHashRequest(String ip, int listeningPort) {
//        if(Blockchain.getInstance().getBlockchainArray().size() >=1) {
//            sendSignedBlockChain(ip, listeningPort);
//        }
    }

    public synchronized void sendSignedBlockChain(String ip, int listeningPort) {
        BlockchainShare blockchainShare = new BlockchainShare(ip, listeningPort);
        String blockchainHash = ChainUtil.getInstance().getBlockChainHash(blockchainShare.getBlockChainInstance());
        blockchainShareDetails.add(blockchainShare);
        String signedBlockchainHash = ChainUtil.getInstance().digitalSignature(blockchainHash);
        MessageSender.getInstance().sendSignedBlockChain(ip,listeningPort,signedBlockchainHash,blockchainHash);

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


    //no need of synchronizing
    public void requestBlockchain() {
        BlockchainReceiver blockchainReceiver = getBlockchainReceiverfromPK(findCorrectBlockchain());
        requestedBlockchainHash = blockchainReceiver.getBlockchainHash();
        MessageSender.getInstance().requestBlockchainFromPeer(blockchainReceiver.getIp(),blockchainReceiver.getListeningPort());
    }

    //no need of synchronizing
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

    public void requestTransactionData(String vehicleID, String location, Timestamp date, String peerID) {

    }

    public ArrayList<Block> getBlocksToBeAdded() {
        return approvedBlocks;
    }
}
