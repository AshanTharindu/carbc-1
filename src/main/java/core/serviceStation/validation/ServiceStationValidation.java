package core.serviceStation.validation;

import core.blockchain.Block;
import core.consensus.Consensus;
import core.serviceStation.dao.ServiceJDBCDAO;
import org.json.JSONObject;

import java.sql.SQLException;

public class ServiceStationValidation {

    public static boolean validateBlock(Block block) {
        System.out.println("----------------------Begin of ServiceStationValidation/validateBlock---------------------------------");

        try {
            String serviceData = ServiceJDBCDAO.getInstance().getLastServiceRecord(block.getBlockBody().getTransaction().getAddress()).toString();

            System.out.println(block.getBlockBody().getTransaction().getData());
            System.out.println(serviceData);

            JSONObject dataFromBlock = new JSONObject(block.getBlockBody().getTransaction().getData());
            JSONObject dataFromServiceStation = new JSONObject(serviceData);

            if (dataFromBlock.getString("serviced_date").equals(dataFromServiceStation.getString("serviced_date")) &&
                    dataFromBlock.getInt("cost")== (dataFromServiceStation.getInt("cost")) &&
                    dataFromBlock.getString("vehicle_id").equals(dataFromServiceStation.getString("vehicle_id")) &&
                    (dataFromBlock.getJSONArray("services").toString()).equals((dataFromServiceStation.getJSONArray("services")).toString())){


                System.out.println("Service data is true. Validated by Service Station");
                Consensus.getInstance().sendAgreementForBlock(block.getBlockHeader().getHash());

                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            System.out.println("----------------------End of ServiceStationValidation/validateBlock---------------------------------");
        }
        return false;
    }

}
