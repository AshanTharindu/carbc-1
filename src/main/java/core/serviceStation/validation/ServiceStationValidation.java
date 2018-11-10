package core.serviceStation.validation;

import core.blockchain.Block;
import core.consensus.Consensus;
import core.serviceStation.dao.ServiceJDBCDAO;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;

public class ServiceStationValidation {

    public static boolean validateBlock(Block block) {
        System.out.println("inside ServiceStationValidation/validateBlock");
        try {
            String serviceData = ServiceJDBCDAO.getInstance().getLastServiceRecord(block.getBlockBody().getTransaction().getAddress()).toString();

            System.out.println(block.getBlockBody().getTransaction().getData());
            System.out.println(serviceData);

            System.out.println("just before sending agreement");

            JSONObject dataFromBlock = new JSONObject(block.getBlockBody().getTransaction().getData());
            JSONObject dataFromServiceStation = new JSONObject(block.getBlockBody().getTransaction().getData());

            System.out.println("--------------------------------------------------------------------------");
            System.out.println(dataFromBlock.getString("vehicle_id"));
            System.out.println(dataFromServiceStation.getString("vehicle_id"));
            System.out.println("--------------------------------------------------------------------------");
            System.out.println(dataFromBlock.getJSONArray("services"));
            System.out.println(dataFromServiceStation.getJSONArray("services"));
            System.out.println("--------------------------------------------------------------------------");

            if (dataFromBlock.getString("serviced_date").equals(dataFromServiceStation.getString("serviced_date")) &&
                    dataFromBlock.getInt("cost")== (dataFromServiceStation.getInt("cost")) &&
                    dataFromBlock.getString("vehicle_id").equals(dataFromServiceStation.getString("vehicle_id")) &&
                    (dataFromBlock.getJSONArray("services").toString()).equals((dataFromServiceStation.getJSONArray("services")).toString())){


                System.out.println("service data is true; validated by service station");
                Consensus.getInstance().sendAgreementForBlock(block.getBlockHeader().getHash());

//                JSONArray servicesFromBlock = dataFromBlock.getJSONArray("services");
//                JSONArray servicesFromStation = dataFromBlock.getJSONArray("services");
//
//                for (int i = 0; i < servicesFromBlock.length(); i++){
//                    JSONObject serviceItemFromBlock = servicesFromBlock.getJSONObject(i);
//                    JSONObject serviceItemFromStation = servicesFromBlock.getJSONObject(i);
//
//                    if (serviceItemFromBlock.getString("serviceType").equals(serviceItemFromStation.getString("serviceType")))
//                }

                return true;

            }

//            if(block.getBlockBody().getTransaction().getData().equals(serviceData)) {
//                Consensus.getInstance().sendAgreementForBlock(block.getBlockHeader().getHash());

//            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;


    }

}
