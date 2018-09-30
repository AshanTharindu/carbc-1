
package core.consensus;

import UI.AgreementRequest;
import UI.BlockStatusUI;
import UI.BlockStatusUI2;
import chainUtil.ChainUtil;
import chainUtil.KeyGenerator;
import core.blockchain.Block;
import network.communicationHandler.MessageSender;

import javax.swing.*;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class ConsensusOld {

    private static ConsensusOld consensusOld;
    public ArrayList<AgreementCollector> agreementCollectors;
    ArrayList<String> agreedTransactiions;
    ArrayList<Block> agreementRequestBlocks;

    private ConsensusOld() {
        agreedTransactiions = new ArrayList<>();
        agreementCollectors = new ArrayList<>();
        agreementRequestBlocks = new ArrayList<>();
    }

    public static ConsensusOld getInstance() {
        if (consensusOld == null) {
            consensusOld = new ConsensusOld();
        }
        return consensusOld;
    }

    public boolean requestAgreementForBlock(Block block) throws NoSuchAlgorithmException {
//        addToAgreementCollectors(block);
//        ArrayList<String> validators = new ArrayList<>();
//        ArrayList<Validation> validations = block.getTransaction().getValidations();
//        for (Validation validation : validations) {
//            validators.add(validation.getValidator().getValidator());
//            MessageSender.getInstance().requestAgreement(block, 1);
//        }
//
//        //send the block to validators in the validators array for agreements
        return true;
    }


    public boolean checkAgreementForBlock(Block block) {
//        System.out.println("inside check agreement");
//        String transID = block.getTransaction().getTransactionID();
//
//        for (String transactionId : agreedTransactiions) {
//            if (transactionId.equals(transID)) {
//                System.out.println("transaction found");
//                return true;
//            }
//        }
        return false;
    }

    public boolean agreedTransaction(String transactionID) {
//        if (!agreedTransactiions.contains(transactionID)) {
//            agreedTransactiions.add(transactionID);
//            System.out.println("Agreed Transaction added, id: " + transactionID);
//            return true;
//        } else {
            return false;
//        }
    }

    public void addRequestAgreementBlock(Block block) {
//        agreementRequestBlocks.add(block);
//        System.out.println("added to agreementRequestBlocks array");
    }

    public boolean handleAgreementResponse(Block block, String agreedNodePublicKey, String signatureString, String data)
            throws NoSuchAlgorithmException,ParseException, SQLException {
        PublicKey agreedNode = KeyGenerator.getInstance().getPublicKey(agreedNodePublicKey);
        byte[] signature = ChainUtil.hexStringToByteArray(signatureString);
        boolean verfied = ChainUtil.verify(agreedNode, signature, data);
        if (verfied) {
            System.out.println("response verified");
            boolean status = addAgreedNodeForBlock(block, agreedNodePublicKey);
            if (status) {
                return true;
            }
        }
        return false;
    }

    public boolean addAgreedNodeForBlock(Block block, String agreedNodePublicKey) throws NoSuchAlgorithmException, ParseException, SQLException {
        if (getAgreementCollectorByBlock(block) != null) {
            boolean status = getAgreementCollectorByBlock(block).addAgreedNode(agreedNodePublicKey);
            if (status) {
                System.out.println("agreement added successfully ");
                checkForEligibilty(block);
                return true;
            } else {
                System.out.println("agreement not added");
                return false;
            }
        } else {
            System.out.println("no Agreement collector found");
            return false;
        }

    }

    public AgreementCollector getAgreementCollectorByBlock(Block block) {
        String id = AgreementCollector.generateAgreementCollectorId(block);
        for(AgreementCollector agreementCollector: this.agreementCollectors) {
            if(agreementCollector.getAgreementCollectorId().equals(id)) {
                return agreementCollector;
            }
        }
        return null;
    }

    public boolean insertBlock(Block block) throws ParseException, SQLException {
//        System.out.println("inside insert block");
//        long receivedBlockNumber = block.getHeader().getBlockNumber();
//        String receivedBlockTimestampString = block.getHeader().getTimestamp();
//
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
//        Date parsedDate = dateFormat.parse(receivedBlockTimestampString);
//        Timestamp receivedBlockTimestamp = new java.sql.Timestamp(parsedDate.getTime());
//
//        if (!blockExistence(block)) {
//            Blockchain.getInstance().addBlock(block);
//            JOptionPane.showMessageDialog(null,"Block Added Successfully");
//            System.out.println("block added successfully");
//            return true;
//        } else {
//            Block existBlock = Blockchain.getInstance().getBlockByNumber(receivedBlockNumber);
//            String existingTimeStampString = existBlock.getHeader().getTimestamp();
//            Date parsedDate1 = dateFormat.parse(existingTimeStampString);
//            Timestamp exsitingTimeStamp = new java.sql.Timestamp(parsedDate1.getTime());
//
//            if (exsitingTimeStamp.after(receivedBlockTimestamp)) {
//                Blockchain.getInstance().rollBack(receivedBlockNumber);
//                Blockchain.getInstance().addBlock(block);
//                JOptionPane.showMessageDialog(null,"Block Added Successfully");
//                System.out.println("block added successfully");
//                return true;
//            } else if (exsitingTimeStamp.equals(receivedBlockTimestamp)) {
//                Blockchain.getInstance().rollBack(receivedBlockNumber);
//                return false;
//            }
            return false;
//        }
    }

    public boolean blockExistence(Block block) {
//        if (Blockchain.getInstance().getBlockchainArray().size() > block.getHeader().getBlockNumber()) {
//            return true;
//        }
        return false;
    }

    public void blockHandler(Block block) throws NoSuchAlgorithmException {
        if (checkAgreementForBlock(block)) {
            String status = "Agreed block";
            BlockStatusUI blockStatusUI = new BlockStatusUI(block);
            blockStatusUI.runBlockStatusUI(status);
            System.out.println("Agreed block");
            //insertBlock(block);
        } else {
            String status = "Not agreed block. requesting agreements";
            BlockStatusUI2 blockStatusUI = new BlockStatusUI2(block);
            blockStatusUI.runBlockStatusUI2();
            System.out.println("Not agreed block. requesting agreements");
            requestAgreementForBlock(block);
        }
    }

    public void checkForEligibilty(Block block) throws ParseException, SQLException {
        int threshold = 1; //get from the predefined rules

        if (getAgreementCollectorByBlock(block).getAgreedNodesCount() == threshold) {
            JOptionPane.showMessageDialog(null,"Agreements received upto threshold level");
            System.out.println("Agreements received upto threshold level");
            insertBlock(block);
        }
    }

    public boolean addToAgreementCollectors(Block block) throws SQLException {
        AgreementCollector agreementCollector = new AgreementCollector(block);
        System.out.println("agreement collector added");
        System.out.println("agreemenCollector id: " + agreementCollector.getAgreementCollectorId());
        return agreementCollectors.add(agreementCollector);
    }

    public boolean responseForBlockAgreement(Block block, String agreed, int neighbourIndex) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException, IOException, SignatureException, InvalidKeyException {
        if (checkAgreementForBlock(block)) {
            sendAgreementForBlock(block, agreed, neighbourIndex);
            return true;
        }
        return false;
    }

    public boolean sendAgreementForBlock(Block block, String agreed, int neighbourIndex) throws
            InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException, IOException, SignatureException{
        MessageSender.getInstance().sendAgreement(block, 1, agreed,
                ChainUtil.sign(KeyGenerator.getInstance().getPrivateKey(), agreed));
        //remove from agreementRequestBlocks array
        return true;
    }

    public void handleAgreementRequest(Block block) throws NoSuchAlgorithmException, IOException, SignatureException, NoSuchProviderException, InvalidKeySpecException {
        AgreementRequest ar = new AgreementRequest(block);
        ar.runAgreementRequestUI();
        Scanner scanner = new Scanner(System.in);
        System.out.println("is this valid? yes/No ");
        String isValid = scanner.next();
        if (isValid.equalsIgnoreCase("yes")){
            sendAgreementForBlock(block,"agreed", 1);
        }
        else if (isValid.equalsIgnoreCase("no")){
            System.out.println("Not Agreed");
            //connection and send
        }else {
            System.out.println("please enter yes or no");
            isValid = scanner.next();
        }
    }

    //new consensusOld protocol



}
