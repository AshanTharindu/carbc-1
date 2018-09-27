package core.consensus;

import chainUtil.ChainUtil;
import chainUtil.KeyGenerator;
import core.blockchain.Block;

import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

public class AgreementCollector {

    private String agreementCollectorId;
    private Block block;
    private Agreement[] mandotaryAgreements;
    private ArrayList<Agreement> agreements;
    ArrayList<String> agreedNodes;
    private Rating rating;

    public AgreementCollector(Block block) {
        agreementCollectorId = generateAgreementCollectorId(block);
        this.block = block;
        agreements = new ArrayList<>();
        mandotaryAgreements = new Agreement[2]; //get from the block
        rating = new Rating(block);
    }

    public boolean addAgreedNode(String agreedNode) {
        if(!agreedNodes.contains(agreedNode)){
            agreedNodes.add(agreedNode);
            return true;
        }else {
            return false;
        }
    }

    //adding agreements
    public boolean addAgreementForBlock(Agreement agreement) {
        if(agreementCollectorId.equals(agreement.getBlockHash())) {
            //check for mandotory
            if(!isDuplicateAgreement(agreement)) {
                PublicKey publicKey = KeyGenerator.getInstance().getInstance().getPublicKey(agreement.getPublicKey());
                if(ChainUtil.getInstance().signatureVerification(agreement.getPublicKey(),agreement.getDigitalSignature(),
                        agreement.getBlockHash())){
                    agreements.add(agreement);
                    // add rating
                    System.out.println("agreement added successfully");
                }

            }
        }
        return false;
    }

    public static String generateAgreementCollectorId(Block block) {
        return ChainUtil.getInstance().getBlockHash(block);
    }

    public boolean isDuplicateAgreement(Agreement agreement) {
        if(agreements.contains(agreement)) {
            return true;
        }
        return false;
    }

    public Block getBlock() {
        return block;
    }

    public ArrayList<String> getAgreedNodes() {
        return agreedNodes;
    }

    public String getAgreementCollectorId() {
        return agreementCollectorId;
    }

    public Agreement[] getMandotaryAgreements() {
        return mandotaryAgreements;
    }

    public ArrayList<Agreement> getAgreements() {
        return agreements;
    }

    public int getAgreedNodesCount() {
        return agreedNodes.size();
    }



}
