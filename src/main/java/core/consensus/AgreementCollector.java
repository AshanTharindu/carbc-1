package core.consensus;

import chainUtil.ChainUtil;
import chainUtil.KeyGenerator;
import config.EventConfigHolder;
import core.blockchain.Block;
import core.connection.BlockJDBCDAO;
import org.json.JSONArray;
import org.json.JSONObject;

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

    JSONObject mandatoryValidators;

    public AgreementCollector(Block block) {
        agreementCollectorId = generateAgreementCollectorId(block);
        this.block = block;
        agreements = new ArrayList<>();
        mandotaryAgreements = new Agreement[2]; //get from the block
        rating = new Rating(block);

        setMandotaryAgreements();
    }

    public void setMandotaryAgreements(){
        String event = this.block.getBlockBody().getTransaction().getEvent();

        JSONObject eventDetail = EventConfigHolder.getInstance()
                .getEventJson()
                .getJSONObject(event);

        JSONArray mandatoryValidatorArray = eventDetail
                .getJSONArray("mandatoryValidators");
        // InsuranceCompany, LeasingCompany, RMV, ServiceStation

        JSONArray secondaryParties = eventDetail
                .getJSONObject("params")
                .getJSONArray("secondaryParty");

        BlockJDBCDAO blockJDBCDAO = new BlockJDBCDAO();

        for (int i=0; i<mandatoryValidatorArray.length(); i++){
            String validatorRole = mandatoryValidatorArray.getString(i);
            boolean isPresent = false;

            for (int j=0; j < secondaryParties.length(); j++){
                JSONObject jsonObject = secondaryParties.getJSONObject(j);
                String role = jsonObject.getString("role");

                if (role.equals(validatorRole)){
                    String secondaryPartyAddress = jsonObject.getString("address");
                    mandatoryValidators.put(validatorRole, secondaryPartyAddress);
                    isPresent = true;
                    break;
                }
            }
            if (!isPresent){

            }
        }
        //now need to check the relevant part is registered as a mandatory validator
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
    public synchronized boolean addAgreementForBlock(Agreement agreement) {
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

    //no need synchronizing
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
