package core.rmv.validation;

import chainUtil.ChainUtil;
import chainUtil.KeyGenerator;
import core.blockchain.Block;
import core.consensus.Agreement;
import core.consensus.Consensus;
import core.rmv.dao.RMVJDBCDAO;
import core.serviceStation.dao.ServiceJDBCDAO;
import org.json.JSONObject;

import javax.xml.bind.util.JAXBSource;
import java.sql.SQLException;

public class RmvValidation {

    public static boolean validateBlock(Block block) {
        try {
            Boolean succeed = false;
            String event = block.getBlockBody().getTransaction().getEvent();

            switch (event){
                case "ExchangeOwnership":
                    System.out.println("inside ownership exchange switch case in validate block");
                    succeed = validateOwnershipExchangeBlock(block);
                    break;

                case "RegisterVehicle":
                    System.out.println("inside register vehicle switch case in validate block");
                    succeed = validateRegistrationBlock(block);
                    break;
            }
            if(succeed) {
                Consensus.getInstance().sendAgreementForBlock(block.getBlockHeader().getHash());
//                String blockHash = block.getBlockHeader().getHash();
//                String digitalSignature = ChainUtil.digitalSignature(block.getBlockHeader().getHash());
//                String signedBlock = digitalSignature;
//                Agreement agreement = new Agreement(digitalSignature, signedBlock, blockHash,
//                        KeyGenerator.getInstance().getPublicKeyAsString());
//
//                Consensus.getInstance().handleAgreement(agreement);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;

    }

    public static boolean validateOwnershipExchangeBlock(Block block) throws SQLException {
        System.out.println("inside validateBlock/validateOwnershipExchangeBlock");
        JSONObject blockData = new JSONObject(block.getBlockBody().getTransaction().getData());
        String registration_number = blockData.getString("registrationNumber");

        JSONObject ownershipData = RMVJDBCDAO.getInstance().getOwnershipInfo(registration_number);

        String pre_owner = blockData.getString("preOwner");
        String new_owner = blockData.getJSONObject("SecondaryParty").getJSONObject("NewOwner").getString("publicKey");
//        String date = blockData.getString("date");

        String vehicleRegistrationNumber = ownershipData.getString("registrationNumber");
        String preOwner = ownershipData.getString("preOwner");
        String newOwner = ownershipData.getString("newOwner");
//        String date1 = ownershipData.getString("date");

        if (registration_number.equals(vehicleRegistrationNumber) && pre_owner.equals(preOwner) && new_owner.equals(newOwner)){
            System.out.println("succeeded in validateBlock/validateOwnershipExchangeBlock");
            return true;
        }
        return false;
    }

    public static boolean validateRegistrationBlock(Block block) throws SQLException {
        System.out.println("inside validateBlock/validateRegistrationBlock");
        JSONObject blockData = new JSONObject(block.getBlockBody().getTransaction().getData());

        String registration_number = blockData.getString("registrationNumber");

        JSONObject registrationData = RMVJDBCDAO.getInstance().getRegistrationInfoByRegistrationNumber(registration_number);


        String current_owner = blockData.getString("currentOwner");
        String engine_number = blockData.getString("engineNumber");
        String vehicle_class = blockData.getString("vehicleClass");
        String condition_and_note = blockData.getString("conditionAndNote");
        String make = blockData.getString("make");
        String model = blockData.getString("model");
        String year_of_manufacture = blockData.getString("yearOfManufacture");

//        System.out.println(current_owner);
//        System.out.println(engine_number);
//        System.out.println(vehicle_class);
//        System.out.println(condition_and_note);
//        System.out.println(make);
//        System.out.println(model);
//        System.out.println(year_of_manufacture);


        String registrationNumber = registrationData.getString("registrationNumber");
        String currentOwner = registrationData.getString("currentOwner");
        String engineNumber = registrationData.getString("engineNumber");
        String vehicleClass = registrationData.getString("vehicleClass");
        String conditionAndNote = registrationData.getString("conditionAndNote");
        String make1 = registrationData.getString("make");
        String model1 = registrationData.getString("model");
        String yearOfManufacture = registrationData.getString("yearOfManufacture");

//        System.out.println(registrationNumber);
//        System.out.println(currentOwner);
//        System.out.println(engineNumber);
//        System.out.println(vehicleClass);
//        System.out.println(conditionAndNote);
//        System.out.println(make1);
//        System.out.println(model1);
//        System.out.println(yearOfManufacture);


        if (registration_number.equals(registrationNumber) && current_owner.equals(currentOwner) && engine_number.equals(engineNumber) && vehicle_class.equals(vehicleClass)
                && condition_and_note.equals(conditionAndNote) && make.equals(make1) && model.equals(model1)
                && year_of_manufacture.equals(yearOfManufacture)){
            System.out.println("succeeded in validateBlock/validateRegistrationBlock");
            return true;
        }

        return false;
    }

}
