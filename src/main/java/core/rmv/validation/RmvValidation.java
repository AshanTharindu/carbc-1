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

    public static void validateBlock(Block block) {
        try {
            Boolean succeed = false;
            String event = block.getBlockBody().getTransaction().getEvent();

            switch (event){
                case "ExchangeOwnership":
                    break;

                case "RegisterVehicle":
                    succeed = validateRegistrationBlock(block);
                    break;
            }
            if(succeed) {
                Consensus.getInstance().sendAgreementForBlock(block.getBlockHeader().getHash());
                String blockHash = block.getBlockHeader().getHash();
                String digitalSignature = ChainUtil.digitalSignature(block.getBlockHeader().getHash());
                String signedBlock = digitalSignature;
                Agreement agreement = new Agreement(digitalSignature, signedBlock, blockHash,
                        KeyGenerator.getInstance().getPublicKeyAsString());

                Consensus.getInstance().handleAgreement(agreement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void validateOwnershipExchangeBlock(Block block){

    }

    public static boolean validateRegistrationBlock(Block block) throws SQLException {
        JSONObject blockData = new JSONObject(block.getBlockBody().getTransaction().getData());

        String registration_number = blockData.getString("registration_number");

        JSONObject registrationData = RMVJDBCDAO.getInstance().getRegistrationInfoByRegistrationNumber(registration_number);


        String current_owner = blockData.getString("current_owner");
        String engine_number = blockData.getString("engine_number");
        String vehicle_class = blockData.getString("vehicle_class");
        String condition_and_note = blockData.getString("condition_and_note");
        String make = blockData.getString("make");
        String model = blockData.getString("model");
        String year_of_manufacture = blockData.getString("year_of_manufacture");


        String registrationNumber = registrationData.getString("registrationNumber");
        String currentOwner = registrationData.getString("currentOwner");
        String engineNumber = registrationData.getString("engineNumber");
        String vehicleClass = registrationData.getString("vehicleClass");
        String conditionAndNote = registrationData.getString("conditionAndNote");
        String make1 = registrationData.getString("make");
        String model1 = registrationData.getString("model");
        String yearOfManufacture = registrationData.getString("yearOfManufacture");


        if (registration_number.equals(registrationNumber) && current_owner.equals(currentOwner) && engine_number.equals(engineNumber) && vehicle_class.equals(vehicleClass)
                && condition_and_note.equals(conditionAndNote) && make.equals(make1) && model.equals(model1)
                && year_of_manufacture.equals(yearOfManufacture)){
            return true;
        }

        return false;
    }

}
