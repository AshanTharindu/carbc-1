package DataOwners;

import chainUtil.ChainUtil;
import core.serviceStation.dao.ServiceJDBCDAO;
import network.Protocol.MessageCreator;
import network.communicationHandler.Handler;
import network.communicationHandler.MessageSender;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class ServiceStation {
    private final Logger log = LoggerFactory.getLogger(ServiceStation.class);


    public JSONObject getServiceRecord(String vehicleId, String signature, String signedData, String dataRequester,
                                       String ip, int listeningPort) {

        String customerPublicKey = getCustomerPublicKey(dataRequester);
        if(ChainUtil.signatureVerification(customerPublicKey,signature,signedData)) {
            log.info("Customer signature verified: {}", dataRequester);
            ServiceJDBCDAO serviceJDBCDAO = new ServiceJDBCDAO();
            JSONObject serviceRecord = null;
            try {
                serviceRecord = serviceJDBCDAO.getServiceRecords(vehicleId);
                log.info("service records found for: {}", vehicleId);
                MessageSender.sendTransactionData(serviceRecord,ip, listeningPort,dataRequester);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return serviceRecord;
        }
        return null;
    }

    public String getCustomerPublicKey(String nodeID) {
        ServiceJDBCDAO serviceJDBCDAO = new ServiceJDBCDAO();
        String publicKey = null;
        try {
            publicKey = serviceJDBCDAO.getCustomerPublicKey(nodeID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return publicKey;
    }
}
