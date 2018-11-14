package core.consensus;

import chainUtil.ChainUtil;
import chainUtil.KeyGenerator;
import config.EventConfigHolder;
import core.blockchain.Block;
import core.connection.BlockJDBCDAO;
import core.connection.IdentityJDBC;
import core.rmv.validation.RmvValidation;
import core.serviceStation.validation.ServiceStationValidation;
import core.serviceStation.webSocketServer.webSocket.WebSocketMessageHandler;
import core.smartContract.OwnershipExchange;
import core.smartContract.Registration;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class AgreementCollector{

    private String agreementCollectorId;
    private Block block;
    private Agreement[] mandatoryAgreements;
    private ArrayList<String> agreedNodes;
    private Rating rating;
    private int mandatoryCount;
    private int secondaryCount;

    private ArrayList<String> mandatoryValidators;
    private ArrayList<String> specialValidators;
    private ArrayList<Agreement> agreements;
    private BlockJDBCDAO blockJDBCDAO;
    private IdentityJDBC identityJDBC;
    private int threshold;
    private final Logger log = LoggerFactory.getLogger(AgreementCollector.class);

    boolean succeed = false;
    private boolean existence = true;


    public AgreementCollector(Block block) throws SQLException {
        this.agreementCollectorId = generateAgreementCollectorId(block);
        this.block = block;
        this.agreements = new ArrayList<>();
        this.rating = new Rating(block.getBlockBody().getTransaction().getEvent());
        this.blockJDBCDAO = new BlockJDBCDAO();
        this.identityJDBC = new IdentityJDBC();
        this.mandatoryValidators = new ArrayList<>();
        this.specialValidators = new ArrayList<>();
        this.threshold = 1;

        setMandatoryAgreements();

        //TODO: Here we have assumed that all the agreements come after creating this agreement collector
        //TODO: I have not handled the other case
    }



    public void setMandatoryAgreements() throws SQLException {
        log.info("setting mandatory validators");
        synchronized (this){
            String event = this.block.getBlockBody().getTransaction().getEvent();
            log.info("block event {}", event);

            JSONObject blockData = new JSONObject(block.getBlockBody().getTransaction().getData());
            log.info("block data {}", blockData);

            JSONObject secondaryParties = blockData.getJSONObject("SecondaryParty");
            JSONObject thirdParties = blockData.getJSONObject("ThirdParty");
            String pubKey;
            secondaryCount = thirdParties.length();
            rating.setSpecialValidators(secondaryCount);
            String myPubKey = KeyGenerator.getInstance().getPublicKeyAsString();

            switch (event){
                case "ExchangeOwnership":
                    String vehicleId = block.getBlockBody().getTransaction().getAddress();
                    String sender = block.getBlockBody().getTransaction().getSender();

                    log.info("executing OwnershipExchange smart contract");
                    OwnershipExchange ownershipExchange = new OwnershipExchange(vehicleId, sender);

                    try{
                        if (ownershipExchange.isAuthorizedToSeller()){
                            log.info("seller is authorized");
                            String newOwnerPubKey = secondaryParties.getJSONObject("NewOwner").getString("publicKey");
                            getMandatoryValidators().add(newOwnerPubKey);
                            log.info("added new owner to mandatory validator array");
                            log.info("no of mandatory validators: {}", getMandatoryValidators().size());

                            JSONObject obj = getIdentityJDBC().getIdentityByRole("RMV");
                            String RmvPubKey = obj.getString("publicKey");
                            getMandatoryValidators().add(RmvPubKey);
                            log.info("added RMV to mandatory validator array");
                            log.info("no of mandatory validators: {}", getMandatoryValidators().size());


                            if(newOwnerPubKey.equals(myPubKey)) {
                                //show notification icon 2

                                //TODO for temporary purpose - remove this
                                Thread.sleep(6000);
                                Consensus.getInstance().sendAgreementForBlock(block.getBlockHeader().getHash());
                                System.out.println("here");
                            }else if(RmvPubKey.equals(myPubKey)) {
                                //show notification in service station
                                log.info("RMV validating ownership exchange transaction");
                                succeed = RmvValidation.validateBlock(block);
                            }else{
                                //show notification icon 1
                                WebSocketMessageHandler.addBlockToNotificationArray(block);
                            }
                        }else{
                            log.info("seller is not authorized");
                            log.info("smart contract failed");
                        }

                    }catch (NullPointerException e){
                        System.out.println("error occurred in smart contract");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        break;
                    }

                case "ServiceRepair":
                    boolean show = true;
                    String serviceStationPubKey = secondaryParties.getJSONObject("serviceStation").getString("publicKey");

                    if (isMandatoryPartyValid("ServiceStation", serviceStationPubKey)){
                        getMandatoryValidators().add(serviceStationPubKey);
                        log.info("added service station to mandatory validator array");
                        log.info("no of mandatory validators: {}", getMandatoryValidators().size());

                        if(serviceStationPubKey.equals(myPubKey)) {
                            System.out.println("service station is validating");
                            show = false;
                            succeed = ServiceStationValidation.validateBlock(block);
                        }

                        JSONArray sparePartProvider = thirdParties.getJSONArray("SparePartProvider");
                        for (int i = 0; i < sparePartProvider.length(); i++){
                            String sparePartPubKey = sparePartProvider.getString(i);
                            getSpecialValidators().add(sparePartPubKey);
                            log.info("added spare part provider to mandatory validator array");
                            log.info("no of special validators: {}", getSpecialValidators().size());

                            if(sparePartPubKey.equals(myPubKey)) {
                                log.info("I am a spare part provider");
                                show = false;
                                //show notification in notification icon 2
                            }
                        }
                        if (show){
                            //show notification in notification icon 1
                            WebSocketMessageHandler.addBlockToNotificationArray(block);
                        }
                    }
                    break;

                case "Insure":
                    pubKey = secondaryParties.getJSONObject("InsuranceCompany")
                            .getString("publicKey");
                    getMandatoryValidators().add(pubKey);
                    if(pubKey.equals(KeyGenerator.getInstance().getPublicKeyAsString())) {
//                        validateBlock();
                    }
                    getMandatoryValidators().add(pubKey);

                    if (isMandatoryPartyValid("InsuranceCompany", pubKey)){
                        WebSocketMessageHandler.addBlockToNotificationArray(block);
                    }
                    break;

                case "Lease":
                    pubKey = secondaryParties.getJSONObject("LeasingCompany")
                            .getString("publicKey");
                    getMandatoryValidators().add(pubKey);
                    if (isMandatoryPartyValid("LeasingCompany", pubKey)){
                        WebSocketMessageHandler.addBlockToNotificationArray(block);
                    }
                    if(pubKey.equals(KeyGenerator.getInstance().getPublicKeyAsString())) {
//                        validateBlock();
                    }
                    break;

                case "BankLoan":
                    pubKey = secondaryParties.getJSONObject("Bank")
                            .getString("publicKey");
                    getMandatoryValidators().add(pubKey);
                    if (isMandatoryPartyValid("Bank", pubKey)){
                        WebSocketMessageHandler.addBlockToNotificationArray(block);
                    }
                    break;

                case "RenewRegistration":
                    pubKey = secondaryParties.getJSONObject("RMV")
                            .getString("publicKey");
                    getMandatoryValidators().add(pubKey);
                    if (isMandatoryPartyValid("RMV", pubKey)){
                        WebSocketMessageHandler.addBlockToNotificationArray(block);
                    }
                    break;

                case "RegisterVehicle":
                    log.info("executing Registration smart contract");
                    Registration registrationSmartContract = new Registration(blockData);

                    if (registrationSmartContract.isAuthorized()){
                        //show notification in notification icon 1
                        log.info("Registration is authorized");

                        JSONObject object = getIdentityJDBC().getIdentityByRole("RMV");
                        String rmvPubKey = object.getString("publicKey");

                        if (isMandatoryPartyValid("RMV", rmvPubKey)){
                            getMandatoryValidators().add(object.getString("publicKey"));
                            WebSocketMessageHandler.addBlockToNotificationArray(block);
                            log.info("added RMV to mandatory validator array");
                            log.info("no of mandatory validators: {}", getMandatoryValidators().size());

                            if(rmvPubKey.equals(myPubKey)) {
                                //show notification in RMV node
                                log.info("RMV validating registration transaction");
                                succeed = RmvValidation.validateBlock(block);
                            }
                        }
                    }else{
                        log.info("Registration is not authorized");
                        existence = false;
                    }

                    break;

                case "RenewInsurance":
                    pubKey = secondaryParties.getJSONObject("InsuranceCompany")
                            .getString("publicKey");
                    getMandatoryValidators().add(pubKey);
                    if (isMandatoryPartyValid("InsuranceCompany", pubKey)){
                        WebSocketMessageHandler.addBlockToNotificationArray(block);
                    }
                    if(pubKey.equals(KeyGenerator.getInstance().getPublicKeyAsString())) {
//                        validateBlock();
                    }
                    break;

                case "BuySpareParts":
                    pubKey = secondaryParties.getJSONObject("SparePartProvider")
                            .getString("publicKey");
                    getMandatoryValidators().add(pubKey);
                    if (isMandatoryPartyValid("SparePartProvider", pubKey)){
                        WebSocketMessageHandler.addBlockToNotificationArray(block);
                    }
                    break;

                case "BuyVehicle":
                    String vehicleNumber = block.getBlockBody().getTransaction().getAddress();
//                    String preOwner = blockData.getString("preOwner");
                    String preOwner = secondaryParties.getJSONObject("PreOwner").getString("publicKey");

                    log.info("initializing OwnershipExchange smart contract");
                    OwnershipExchange ownershipExchge = new OwnershipExchange(vehicleNumber, preOwner);

                    try{
                        if (ownershipExchge.isAuthorizedToSeller()){
                            log.info("seller is authorized");
                            String preOwnerPubKey = secondaryParties.getJSONObject("PreOwner").getString("publicKey");
                            getMandatoryValidators().add(preOwnerPubKey);
                            log.info("added new owner to mandatory validator array");
                            log.info("no of mandatory validators: {}", getMandatoryValidators().size());

                            JSONObject obj = getIdentityJDBC().getIdentityByRole("RMV");
                            String RmvPubKey = obj.getString("publicKey");
                            getMandatoryValidators().add(RmvPubKey);
                            log.info("added RMV to mandatory validator array");
                            log.info("no of mandatory validators: {}", getMandatoryValidators().size());
                            
                            if(preOwnerPubKey.equals(myPubKey)) {
                                //show notification icon 2
                                Thread.sleep(6000);
                                log.info("pre owner sending agreements");
                                Consensus.getInstance().sendAgreementForBlock(block.getBlockHeader().getHash());
                            }else if(RmvPubKey.equals(myPubKey)) {
                                //show notification in service station
//                                Thread.sleep(6000);
                                log.info("RMV validating ownership exchange transaction");
                                succeed = RmvValidation.validateBlock(block);
                            }else{
                                //show notification icon 1
                                WebSocketMessageHandler.addBlockToNotificationArray(block);
                            }
                        }else{
                            log.info("seller is not authorized");
                            log.info("smart contract failed");
                        }

                    }catch (NullPointerException e){
                        System.out.println("error occurred in smart contract");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        break;
                    }

            }
        }
        mandatoryCount = mandatoryValidators.size();
        rating.setMandatory(mandatoryCount);

        if (mandatoryValidators.size()>0){
            for (int i = 0; i<mandatoryValidators.size(); i++){
                System.out.println(mandatoryValidators.get(i));
            }
        }

        if (specialValidators.size()>0){
            for (int i = 0; i<specialValidators.size(); i++){
                System.out.println(specialValidators.get(i));
            }
        }

    }

    public boolean isMandatoryPartyValid(String role, String pubKey) throws SQLException {
        IdentityJDBC identityJDBC = new IdentityJDBC();
        JSONObject jsonObject = identityJDBC.getIdentityByAddress(pubKey);

        if (role.equals(jsonObject.getString("role"))){
            return true;
        }
        return false;

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
        if(!getAgreedNodes().contains(agreedNode)){
            getAgreedNodes().add(agreedNode);
            return true;
        }else {
            return false;
        }
    }

    //adding agreements
    public synchronized boolean addAgreementForBlock(Agreement agreement) {
        System.out.println("Inside addAgreementForBlock method");
        if(agreementCollectorId.equals(agreement.getBlockHash())) {
            if(!isDuplicateAgreement(agreement)) {
                if(ChainUtil.signatureVerification(agreement.getPublicKey(), agreement.getSignedBlock(),
                                agreement.getBlockHash())) {
                    getAgreements().add(agreement);
                    //check for mandatory
                    if (getMandatoryValidators().contains(agreement.getPublicKey())){
                        System.out.println("mandatory validator received");
                        System.out.println(agreement.getPublicKey());
                        System.out.println("mandatory validators size = " + getMandatoryValidators().size());
                        getMandatoryValidators().remove(agreement.getPublicKey());
                        System.out.println("mandatory validators size = " + getMandatoryValidators().size());
                        // add rating
                    }else if (getSpecialValidators().contains(agreement.getPublicKey())){
                        getSpecialValidators().remove(agreement.getPublicKey());
                        // add rating
                    }

                    log.info("agreement added for block: {}", agreement.getBlockHash());
                    return true;
                }
            }
        }
        return false;
    }

    public static String generateAgreementCollectorId(Block block) {
        return block.getBlockHeader().getHash();
    }

    //no need synchronizing
    public boolean isDuplicateAgreement(Agreement agreement) {
        if(getAgreements().contains(agreement)) {
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

    public Agreement[] getMandatoryAgreements() {
        return mandatoryAgreements;
    }

    public ArrayList<Agreement> getAgreements() {
        return agreements;
    }

    public int getAgreedNodesCount() {
        return getAgreedNodes().size();
    }

    public ArrayList<String> getMandatoryValidators() {
        return mandatoryValidators;
    }

    public ArrayList<String> getSpecialValidators() {
        return specialValidators;
    }

    public BlockJDBCDAO getBlockJDBCDAO() {
        return blockJDBCDAO;
    }

    public int getThreshold() {
        return threshold;
    }

    public IdentityJDBC getIdentityJDBC() {
        return identityJDBC;
    }

//    public void validateBlock() {
//        try {
//            String serviceData = ServiceJDBCDAO.getInstance().getLastServiceRecord(block.getBlockBody().getTransaction().getAddress()).toString();
//            if(block.getBlockBody().getTransaction().getData().equals(serviceData)) {
//                Consensus.getInstance().sendAgreementForBlock(block.getBlockHeader().getHash());
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }

    public Rating getRating() {
        return rating;
    }

    public int getMandatoryArraySize() {
        return mandatoryValidators.size();
    }

    public int getSecondaryArraySize() {
        return specialValidators.size();
    }

    public boolean isExistence() {
        return existence;
    }
}
