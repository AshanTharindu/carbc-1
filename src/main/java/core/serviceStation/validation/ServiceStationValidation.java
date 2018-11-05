package core.serviceStation.validation;

import core.blockchain.Block;
import core.consensus.Consensus;
import core.serviceStation.dao.ServiceJDBCDAO;

import java.sql.SQLException;

public class ServiceStationValidation {

    public static void validateBlock(Block block) {
        System.out.println("inside ServiceStationValidation/validateBlock");
        try {
            String serviceData = ServiceJDBCDAO.getInstance().getLastServiceRecord(block.getBlockBody().getTransaction().getAddress()).toString();
            if(block.getBlockBody().getTransaction().getData().equals(serviceData)) {
                Consensus.getInstance().sendAgreementForBlock(block.getBlockHeader().getHash());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
