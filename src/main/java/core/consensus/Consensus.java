package core.consensus;

import chainUtil.ChainUtil;
import chainUtil.KeyGenerator;
import controller.Controller;
import core.blockchain.Block;
import core.blockchain.BlockInfo;
import core.blockchain.Transaction;
import core.connection.BlockJDBCDAO;
import core.connection.HistoryDAO;
import core.connection.Identity;
import core.serviceStation.webSocketServer.webSocket.WebSocketMessageHandler;
import core.smartContract.BlockValidity;
import core.smartContract.TimeKeeper;
import network.communicationHandler.MessageSender;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.text.BadLocationException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

public class Consensus extends Observable {

    private static Consensus consensus;
    private final Logger log = LoggerFactory.getLogger(Consensus.class);
    private ArrayList<Block> nonApprovedBlocks;
    private ArrayList<AgreementCollector> agreementCollectors;
    private ArrayList<Block> approvedBlocks;
    //to automate agreement process
    private ArrayList<Transaction> addedTransaction;
    private String blockBroadcasted = null;

    private Consensus() {
        nonApprovedBlocks = new ArrayList<>();
        agreementCollectors = new ArrayList<>();
        approvedBlocks = new ArrayList<>();
    }

    public static Consensus getInstance() {
        if (consensus == null) {
            consensus = new Consensus();
        }
        return consensus;
    }

    //block broadcasting and sending agreements

    public void broadcastBlock(Block block, String data) {
        HistoryDAO historyDAO = new HistoryDAO();
        try {
            historyDAO.saveBlockWithAdditionalData(block, data);
            handleNonApprovedBlock(block);
            MessageSender.broadCastBlock(block);
            setBlockBroadcasted(block.getBlockHeader().getHash());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized void testHandleNonApprovedBlock(Block block){
        this.nonApprovedBlocks.add(block);
        WebSocketMessageHandler.addBlockToNotificationArray(block);
    }

    public synchronized void handleNonApprovedBlock(Block block) throws SQLException {
        log.info("Handling non approved blocks: Inside Consensus/handleNonApprovedBlock() method");

        if (!isDuplicateBlock(block)) {
            if(ChainUtil.signatureVerification(block.getBlockBody().getTransaction().getSender(),
                    block.getBlockHeader().getSignature(),block.getBlockHeader().getHash())) {

                log.info("signature verification successful: {}", block.getBlockHeader().getBlockNumber());

//                boolean isPresent = false;
//                if(getNonApprovedBlocks().size()>0) {
//                    for (Block b : this.getNonApprovedBlocks()) {
//                        if (b.getBlockHeader().getPreviousHash().equals(block.getBlockHeader().getPreviousHash())) {
//                            isPresent = true;
//                            break;
//                        }
//                    }
//                }


                this.nonApprovedBlocks.add(block);
                log.info("Received block id added to nonApprovedBlocks array");
                log.info("size of nonApprovedBlocks: {}", nonApprovedBlocks.size());

                TimeKeeper timeKeeper = new TimeKeeper(block.getBlockHeader().getPreviousHash());
                timeKeeper.start();

//                if (!isPresent) {
//
//                }

                AgreementCollector agreementCollector = new AgreementCollector(block);
                System.out.println("agreement colletor ID: "+agreementCollector.getAgreementCollectorId());

                if (agreementCollector.isExistence()){
                    agreementCollectors.add(agreementCollector);

                    if (agreementCollector.succeed){
                        log.info("Sending agreements from me");
                        String blockHash = block.getBlockHeader().getHash();
                        String digitalSignature = ChainUtil.digitalSignature(block.getBlockHeader().getHash());
                        String signedBlock = digitalSignature;
                        Agreement agreement = new Agreement(digitalSignature, signedBlock, blockHash,
                                KeyGenerator.getInstance().getPublicKeyAsString());

                        Consensus.getInstance().handleAgreement(agreement);
                    }

                    log.info("agreement Collector added, size: {}", agreementCollectors.size());

                }

            }

        }
    }

    public void checkAgreementsForBlock(String preBlockHash) throws SQLException, ParseException {
        log.info("checking agreements for block: {}", preBlockHash);

        ArrayList<Block> qualifiedBlocks = new ArrayList<>();
        log.info("size of qualified blocks array: {}", qualifiedBlocks.size());

        for (Block b : this.getNonApprovedBlocks()) {
            log.info("checking non approved blocks array. size: {}", getNonApprovedBlocks().size());

            if (b.getBlockHeader().getPreviousHash().equals(preBlockHash)) {
                log.info("matching block found for block hash: {}", preBlockHash);
                String blockHash = b.getBlockHeader().getHash();
                AgreementCollector agreementCollector = getAgreementCollector(blockHash);

                synchronized (agreementCollectors) {
                    log.info("remaining mandatory validators for this transaction: {}", agreementCollector.getMandatoryValidators().size());
                    if (agreementCollector.getMandatoryValidators().size() == 0) {
                        int agreementCount = agreementCollector.getAgreements().size();
                        log.info("total agreements received for block: {}", agreementCount);
                        if (agreementCount >= agreementCollector.getThreshold()) {
                            log.info("consensus achieved");
                            qualifiedBlocks.add(b);
                            log.info("size of the qualified block array: {}", qualifiedBlocks.size());

                            //rating calculations
                            log.info("calculating rating");
                            agreementCollector.getRating().setAgreementCount(agreementCount);
                            double rating = agreementCollector.getRating().calRating(agreementCollector.
                                    getMandatoryArraySize(),agreementCollector.getSecondaryArraySize());
                            b.getBlockHeader().setRating(rating);
                            log.info("rating of the block:{}", b.getBlockHeader().getRating());

                            log.info("removing agreement collector: {}", agreementCollector);
                            this.agreementCollectors.remove(agreementCollector);
                            log.info("size of the agreement collector array: {}", agreementCollectors.size());
                        }
                    } else {
                        log.info("block has insufficient agreements");
                    }
                }
            }
        }
        addBlockToBlockchain(qualifiedBlocks);
    }

    public Block selectQualifiedBlock(ArrayList<Block> qualifiedBlocks) throws SQLException, ParseException {
        log.info("no of qualified blocks: {}", qualifiedBlocks.size());
        log.info("selecting the qualified block to be added to the blockchain");
        Block qualifiedBlock = null;

        if (qualifiedBlocks.size() != 0) {
            System.out.println("inside if block; qualifiedBlocks.size() != 0");
            qualifiedBlock = qualifiedBlocks.get(0);

            Timestamp blockTimestamp = ChainUtil.convertStringToTimestamp(qualifiedBlock.getBlockHeader().getBlockTime());

            synchronized (getNonApprovedBlocks()) {
                for (Block b : qualifiedBlocks) {
                    if (blockTimestamp.after(ChainUtil.convertStringToTimestamp(b.getBlockHeader().getBlockTime()))) {
                        log.info("not the qualified block: {}", qualifiedBlock);
                        removeBlockFromNonApprovedBlocks(qualifiedBlock);
                        qualifiedBlock = b;
                        blockTimestamp = ChainUtil.convertStringToTimestamp(b.getBlockHeader().getBlockTime());
                    } else {
                        log.info("not the qualified block: {}", b);
                        removeBlockFromNonApprovedBlocks(b);
                    }
                }
            }
            //TODO: for now we discard all delayed blocks. only consider blocks that got enough agreements within the specific time period
        } else {
            //need to restart the timer again
            log.info("no qualified blocks");
        }
        return qualifiedBlock;
    }

    public void addBlockToBlockchain(ArrayList<Block> qualifiedBlocks) throws SQLException, ParseException {
        log.info("adding block to blockchain: inside Consensus/addBlockToBlockchain(");
        Block block = selectQualifiedBlock(qualifiedBlocks);
        log.info("qualified block: {}", block);

        if (block != null) {
            BlockJDBCDAO blockJDBCDAO = new BlockJDBCDAO();
            JSONObject addedBlock = blockJDBCDAO.checkPossibilityToAddBlock(block.getBlockHeader().getPreviousHash());

            BlockInfo blockInfo = new BlockInfo();
            Identity identity = null;
            blockInfo.setValidity(true);

            if (addedBlock.length() != 0){
                String blockHashInDB = addedBlock.getString("blockHash");

//                Timestamp blockTimestamp = ChainUtil.convertStringToTimestamp(block.getBlockHeader().getBlockTime());
//                Timestamp blockTimestampInDB = ChainUtil.convertStringToTimestamp(addedBlock.getString("blockTimeStamp"));
//
//                if (blockTimestampInDB.after(blockTimestamp)) {
//                    //timestamp in block in db > timestamp in this block
//                    //set validity = 0 in block in db
//                    blockJDBCDAO.setValidity(false, blockHashInDB);
//                    //add this block with validity = 1
//                    blockInfo.setValidity(true);
//
//                    if (block.getBlockBody().getTransaction().getTransactionId().substring(0, 1).equals("I")) {
//                        log.info("identity related transaction.");
//                        JSONObject body = new JSONObject(block.getBlockBody().getTransaction().getData());
//                        String publicKey = body.getString("publicKey");
//                        String role = body.getString("role");
//                        String name = body.getString("name");
//                        String location = body.getString("location");
//
//                        identity = new Identity(block.getBlockHeader().getHash(), publicKey, role, name, location);
//                    }
//
//                }else{
//                    //timestamp in block in db < timestamp in this block
//                    //add this block with validity = 0
//                    blockInfo.setValidity(false);
//
//                }
            }

            log.info("creating block info object");
            blockInfo.setPreviousHash(block.getBlockHeader().getPreviousHash());
            blockInfo.setHash(block.getBlockHeader().getHash());
            blockInfo.setBlockTime(ChainUtil.convertStringToTimestamp(block.getBlockHeader().getBlockTime()));
            blockInfo.setBlockNumber(block.getBlockHeader().getBlockNumber());
            blockInfo.setTransactionId(block.getBlockBody().getTransaction().getTransactionId());
            blockInfo.setSender(block.getBlockBody().getTransaction().getSender());
            blockInfo.setEvent(block.getBlockBody().getTransaction().getEvent());
            blockInfo.setData(block.getBlockBody().getTransaction().getData().toString());
            blockInfo.setAddress(block.getBlockBody().getTransaction().getAddress());
            blockInfo.setRating(block.getBlockHeader().getRating());


            //TODO: need to check that this is the right block to add based on the previous hash
            blockJDBCDAO.addBlockToBlockchain(blockInfo, identity);

            //updating in history table
            manageStatus(block.getBlockHeader().getHash());
        }
    }

    //no need of synchronizing
    public boolean isDuplicateBlock(Block block) {
        if (getNonApprovedBlocks().contains(block)) {
            log.info("This is a duplicate block");
            return true;
        }
        log.info("This is not a duplicate block");
        return false;
    }

    //no need of synchronizing
    public void sendAgreementForBlock(String blockHash) {
        String signedBlock = ChainUtil.digitalSignature(blockHash);
        MessageSender.sendAgreement(signedBlock, blockHash);
        log.info("Agreement Sent for: {}", blockHash);
    }

    public void sendAgreementForBlockTest(Block block) {
        String blockHash = block.getBlockHeader().getHash();
        String signedBlock = ChainUtil.getInstance().digitalSignature(blockHash);
        MessageSender.sendAgreementTest(signedBlock, blockHash);
        log.info("Agreement Sent for: {}", block.getBlockHeader().getHash());
    }

    //no need of synchronizing
    public void handleAgreement(Agreement agreement) {
        System.out.println("agreement.getBlockHash()" + agreement.getBlockHash());
        System.out.println();

        if (getAgreementCollector(agreement.getBlockHash()) != null){
            getAgreementCollector(agreement.getBlockHash()).addAgreementForBlock(agreement);
        }
    }

    //no need of synchronizing
    private AgreementCollector getAgreementCollector(String id) {
        log.info("size of the agreement collector array: {}", agreementCollectors.size());
        log.info("searching for matching agreement collector with id: {}", id);

        for (AgreementCollector agreementCollector : this.agreementCollectors) {
            if (agreementCollector.getAgreementCollectorId().equals(id)) {
                log.info("found agreement collector");
                return agreementCollector;
            }
        }
        return null;
    }

    //no need of synchronizing
    public void handleReceivedAgreement(String signature, String signedBlock, String blockHash, String publicKey) {
        handleAgreement(new Agreement(signature, signedBlock, blockHash, publicKey));
    }

    public void handleReceivedAdditionalData(String blockHash, String additionalData) {
        Block block = getBlockByBlockHash(blockHash);
        if(block != null) {
            String data = block.getBlockBody().getTransaction().getData();
            JSONObject jsonData = new JSONObject(data);
            String additionalDataField = jsonData.getString("additionalData");
            if(additionalDataField.equals(ChainUtil.getHash(additionalData))) {
                jsonData.put("additionalData", additionalData);
                block.getBlockBody().getTransaction().setData(jsonData.toString());
                Controller controller = new Controller();
                controller.notifyReceivedAdditionalData();
                log.info("Additional Data added to the block");
            }
        } else {
            log.info("No block found for blockHash: {} ", blockHash);
        }

    }

    public Block getBlockByBlockHash(String blockHash) {
        for(Block block: getNonApprovedBlocks()) {
            if(blockHash.equals(block.getBlockHeader().getHash())) {
                return block;
            }
        }
        return null;
    }


    public JSONObject getAdditionalDataForBlock(String blockHash) {
        return new JSONObject();
    }

    public ArrayList<Block> getBlocksToBeAdded() {
        return approvedBlocks;
    }


    public ArrayList<Block> getNonApprovedBlocks() {
        return nonApprovedBlocks;
    }

    public void addBlockToNonApprovedBlocks(Block nonApprovedBlock) {
        this.nonApprovedBlocks.add(nonApprovedBlock);
        setChanged();
        notifyObservers();
    }

    public void removeBlockFromNonApprovedBlocks(Block nonApprovedBlock) {
        this.nonApprovedBlocks.remove(nonApprovedBlock);
        setChanged();
        notifyObservers();
    }

    public boolean isItMyBlock(String blockHash) {
        boolean myBlock = false;
        try {
            HistoryDAO historyDAO = new HistoryDAO();
            boolean exist = historyDAO.checkExistence(blockHash);
            myBlock = exist;
        }catch (Exception e) {
            e.printStackTrace();
        }

        return myBlock;
    }

    public boolean isItMyBlock2(String blockHash) {
        if(blockHash.equals(isBlockBroadcasted())) {
            return true;
        }
        return false;
    }

    public void updateHistory(String blockHash) {
        HistoryDAO historyDAO = new HistoryDAO();
        historyDAO.setValidity(blockHash);
        log.info("History Updated for: ", blockHash);
    }

    public void setBlockBroadcasted(String blockHash) {
        this.blockBroadcasted = blockHash;
    }

    public String isBlockBroadcasted() {
        return blockBroadcasted;
    }

    public void manageStatus(String blockHash) {
        String blockStatus = isBlockBroadcasted();
        try{
            if(blockStatus != null) {
                if(isItMyBlock2(blockHash)) {
                    updateHistory(blockHash);
                    setBlockBroadcasted(null);
                }else {
                    //block failed
                    Thread.sleep(5000);
                    HistoryDAO historyDAO = new HistoryDAO();
                    JSONObject failedBlockDetails = historyDAO.getFailedBlockDetails(blockHash);
                    if(failedBlockDetails.length() != 0) {
                        Controller controller = new Controller();
                        controller.sendTransaction(failedBlockDetails.getString("event"),
                                failedBlockDetails.getString("vehicleId"),
                                new JSONObject(failedBlockDetails.getString("data")));
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void manageStatus2(String blockHash) {
        try{
            if(isItMyBlock(blockHash)) {
                updateHistory(blockHash);
                setBlockBroadcasted(null);
            }else {
                //block failed
                Thread.sleep(5000);
                HistoryDAO historyDAO = new HistoryDAO();
                JSONObject failedBlockDetails = historyDAO.getFailedBlockDetails(blockHash);
                if(failedBlockDetails.length() != 0) {
                    Controller controller = new Controller();
                    controller.sendTransaction(failedBlockDetails.getString("event"),
                            failedBlockDetails.getString("vehicleId"),
                            new JSONObject(failedBlockDetails.getString("data")));
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }


    }
}
