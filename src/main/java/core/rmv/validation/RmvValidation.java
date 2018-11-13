package core.rmv.validation;

import core.blockchain.Block;
import core.consensus.Consensus;
import core.rmv.dao.RMVJDBCDAO;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.SQLException;

public class RmvValidation {
    private static final Logger log = LoggerFactory.getLogger(RmvValidation.class);
    public static boolean validateBlock(Block block) {
        log.info("Inside validateBlock by RMV method");
        try {
            Boolean succeed = false;
            String event = block.getBlockBody().getTransaction().getEvent();

            switch (event){
                case "ExchangeOwnership":
                    succeed = validateOwnershipExchangeBlock(block);
                    break;

                case "RegisterVehicle":
                    succeed = validateRegistrationBlock(block);
                    break;

                case "BuyVehicle":
                    succeed = validateBuyVehicleBlock(block);
            }
            if(succeed) {
                log.info("sending agreements by RMV for block {}", block.getBlockHeader().getHash());
                Thread.sleep(5000);
                Consensus.getInstance().sendAgreementForBlock(block.getBlockHeader().getHash());
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("RMV validation failed");
        return false;

    }

    public static boolean validateOwnershipExchangeBlock(Block block) throws SQLException {
        JSONObject blockData = new JSONObject(block.getBlockBody().getTransaction().getData());
        String vehicleId = blockData.getString("vehicleId");

        String pre_owner = blockData.getString("preOwner");
        String new_owner = blockData.getJSONObject("SecondaryParty").getJSONObject("NewOwner").getString("publicKey");

        JSONObject ownershipData = RMVJDBCDAO.getInstance().getOwnershipInfo(vehicleId);

        if (ownershipData.length() == 0){
            JSONObject registrationData = RMVJDBCDAO.getInstance().getRegistrationInfo(vehicleId);
            String currentOwner = registrationData.getString("currentOwner");
            String vid = registrationData.getString("vehicleId");

            if (vehicleId.equals(vid) && pre_owner.equals(currentOwner)){
                log.info("RMV successfully validated the transaction");
                return true;
            }

        }else{
            String preOwner = ownershipData.getString("preOwner");
            String newOwner = ownershipData.getString("newOwner");
            String vid = ownershipData.getString("vehicleId");

            if (vehicleId.equals(vid) && pre_owner.equals(preOwner) && new_owner.equals(newOwner)){
                log.info("RMV successfully validated the transaction");
                return true;
            }
        }
        log.info(".....RMV validation failed");
        return false;
    }

    public static boolean validateBuyVehicleBlock(Block block) throws SQLException {
        JSONObject blockData = new JSONObject(block.getBlockBody().getTransaction().getData());
        String vehicleId = blockData.getString("vehicleId");

        String new_owner = blockData.getString("newOwner");
        String pre_owner = blockData.getJSONObject("SecondaryParty").getJSONObject("PreOwner").getString("publicKey");

        JSONObject ownershipData = RMVJDBCDAO.getInstance().getOwnershipInfo(vehicleId);

        if (ownershipData.length() == 0){
            JSONObject registrationData = RMVJDBCDAO.getInstance().getRegistrationInfo(vehicleId);
            String currentOwner = registrationData.getString("currentOwner");
            String vid = registrationData.getString("vehicleId");

            if (vehicleId.equals(vid) && pre_owner.equals(currentOwner)){
                log.info("RMV successfully validated the transaction");
                return true;
            }

        }else{
            String preOwner = ownershipData.getString("preOwner");
            String newOwner = ownershipData.getString("newOwner");
            String vid = ownershipData.getString("vehicleId");

            if (vehicleId.equals(vid) && pre_owner.equals(preOwner) && new_owner.equals(newOwner)){
                log.info("RMV successfully validated the transaction");
                return true;
            }
        }
        log.info(".....RMV validation failed");
        return false;
    }

    public static boolean validateRegistrationBlock(Block block) throws SQLException {
        System.out.println("inside validateBlock/validateRegistrationBlock");
        JSONObject blockData = new JSONObject(block.getBlockBody().getTransaction().getData());

        String registration_number = blockData.getString("registrationNumber");
        String current_owner = blockData.getString("currentOwner");
        String engine_number = blockData.getString("engineNumber");
        String chassis_number = blockData.getString("chassisNumber");
        String make = blockData.getString("make");
        String model = blockData.getString("model");

        JSONObject registrationData = RMVJDBCDAO.getInstance().getRegistrationInfoByRegistrationNumber(registration_number);

        String registrationNumber ="";
        String currentOwner="";
        String engineNumber="";
        String chassisNumber="";
        String make1="";
        String model1="";

        if(registrationData.length() > 0){
             registrationNumber = registrationData.getString("registrationNumber");
             currentOwner = registrationData.getString("currentOwner");
             engineNumber = registrationData.getString("engineNumber");
             chassisNumber = registrationData.getString("chassisNumber");
             make1 = registrationData.getString("make");
             model1 = registrationData.getString("model");
        }

//        if (registration_number.equals(registrationNumber) &&
//                current_owner.equals(currentOwner) &&
//                engine_number.equals(engineNumber) &&
//                chassis_number.equals(chassisNumber)
//                && make.equals(make1) && model.equals(model1)){
//            System.out.println("succeeded in validateBlock/validateRegistrationBlock");
//            return true;
//        }

        if (!registration_number.equals(registrationNumber)){
            log.info("registration not number matched");
            return false;
        }
        if (!current_owner.equals(currentOwner)){
            log.info("current owner not matched");
            return false;
        }
        if (!engine_number.equals(engineNumber)){
            log.info("engine number not matched");
            return false;
        }
        if (!chassis_number.equals(chassisNumber)){
            log.info("chassis number not matched");
            return false;
        }
        if (!make.equals(make1)){
            log.info("make not matched");
            return false;
        }
        if (!model.equals(model1)){
            log.info("model not matched");
            return false;
        }

        return true;
    }

}
