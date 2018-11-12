package core.serviceStation.validation;

import core.blockchain.Block;
import core.consensus.Consensus;
import core.rmv.validation.RmvValidation;
import core.serviceStation.dao.ServiceJDBCDAO;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class ServiceStationValidation {

    private static final Logger log = LoggerFactory.getLogger(ServiceStationValidation.class);
    public static boolean validateBlock(Block block) {

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


                log.info("RMV successfully validated the transaction");
                log.info("service station sending agreements");
                Consensus.getInstance().sendAgreementForBlock(block.getBlockHeader().getHash());
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        log.info("service station validation failed");
        return false;
    }

}
