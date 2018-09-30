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
import java.sql.SQLException;
import java.util.ArrayList;

public class AgreementCollector extends Thread{

    private String agreementCollectorId;
    private Block block;
    private Agreement[] mandotaryAgreements;
    ArrayList<String> agreedNodes;
    private Rating rating;

    ArrayList<String> mandatoryValidators;
    ArrayList<String> specialValidators;
    private ArrayList<Agreement> agreements;
    BlockJDBCDAO blockJDBCDAO;

    public AgreementCollector(Block block) throws SQLException {
        agreementCollectorId = generateAgreementCollectorId(block);
        this.block = block;
        agreements = new ArrayList<>();
        mandotaryAgreements = new Agreement[2]; //get from the block
        rating = new Rating(block);
        blockJDBCDAO = new BlockJDBCDAO();

        setMandatoryAgreements();

        //TODO: Here we have assumed that all the agreements come after creating this agreement collector
        //TODO: I have not handled the other case
    }

    public void setMandatoryAgreements() throws SQLException {

        synchronized (this){
            String event = this.block.getBlockBody().getTransaction().getEvent();
            JSONObject blockData = block.getBlockBody().getTransaction().getData();
            JSONObject secondaryParties = blockData.getJSONObject("SecondaryParty");
            JSONArray thirdParties = blockData.getJSONArray("ThirdParty");

            switch (event){
                case "ExchangeOwnership":
                    mandatoryValidators.add(secondaryParties.getJSONObject("NewOwner")
                            .getString("address"));

                    JSONObject obj = blockJDBCDAO.getIdentityByRole("RMV");
                    mandatoryValidators.add(obj.getString("publicKey"));
                    break;

                case "ServiceRepair":
                    mandatoryValidators.add(secondaryParties.getJSONObject("ServiceStation")
                            .getString("address"));
                    for (int i = 0; i < thirdParties.length(); i++){
                        specialValidators.add(thirdParties.getString(i));
                    }
                    break;

                case "Insure":
                    mandatoryValidators.add(secondaryParties.getJSONObject("InsuranceCompany")
                            .getString("address"));
                    break;

                case "Lease":
                    mandatoryValidators.add(secondaryParties.getJSONObject("LeasingCompany")
                            .getString("address"));
                    break;

                case "BankLoan":
                    mandatoryValidators.add(secondaryParties.getJSONObject("Bank")
                            .getString("address"));
                    break;

                case "RenewRegistration":
                    mandatoryValidators.add(secondaryParties.getJSONObject("RMV")
                            .getString("address"));
                    break;

                case "RegisterVehicle":
                    mandatoryValidators.add(secondaryParties.getJSONObject("RMV")
                            .getString("address"));
                    break;

                case "RenewInsurance":
                    mandatoryValidators.add(secondaryParties.getJSONObject("InsuranceCompany")
                            .getString("address"));
                    break;

                case "BuySpareParts":
                    mandatoryValidators.add(secondaryParties.getJSONObject("SparePartProvider")
                            .getString("address"));
                    break;

            }
        }
    }

    public void setMandotaryAgreementsOld(){
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

                    synchronized (this) {
//                        mandatoryValidators.put(validatorRole, secondaryPartyAddress);
                    }

                    isPresent = true;
                    break;
                }
            }
            if (!isPresent){

            }
        }
        //now need to check the relevant part is registered as a mandatory validator
    }

    public synchronized boolean addAgreedNode(String agreedNode) {
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
                if(ChainUtil.getInstance()
                        .signatureVerification(agreement.getPublicKey(),
                                agreement.getDigitalSignature(),
                                agreement.getBlockHash()))
                {

                    agreements.add(agreement);
                    if (mandatoryValidators.contains(agreement.getPublicKey())){
                        mandatoryValidators.remove(agreement.getPublicKey());
                        // add rating
                    }else if (specialValidators.contains(agreement.getPublicKey())){
                        specialValidators.remove(agreement.getPublicKey());
                        // add rating
                    }

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
