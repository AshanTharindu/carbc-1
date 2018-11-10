package core.serviceStation.validation;

import core.blockchain.Block;
import core.consensus.Consensus;
import core.serviceStation.dao.ServiceJDBCDAO;

import java.sql.SQLException;

public class ServiceStationValidation {

    public static boolean validateBlock(Block block) {
        System.out.println("inside ServiceStationValidation/validateBlock");
        try {
            String serviceData = ServiceJDBCDAO.getInstance().getLastServiceRecord(block.getBlockBody().getTransaction().getAddress()).toString();

            System.out.println(block.getBlockBody().getTransaction().getData());
            System.out.println(serviceData);

//            if(block.getBlockBody().getTransaction().getData().equals(serviceData)) {
            System.out.println("just before sending agreement");
                Consensus.getInstance().sendAgreementForBlock(block.getBlockHeader().getHash());

//            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;

    }

}
