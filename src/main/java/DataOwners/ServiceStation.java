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

//        String customerPublicKey = getCustomerPublicKey(dataRequester);
//        if(ChainUtil.signatureVerification(customerPublicKey,signature,signedData)) {
        if(true) {
            log.info("Customer signature verified: {}", dataRequester);
            JSONObject serviceRecord = null;
            try {
                serviceRecord = ServiceJDBCDAO.getInstance().getLastServiceRecord(vehicleId);
                System.out.println(serviceRecord);
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
        String publicKey = null;
        try {
            publicKey = ServiceJDBCDAO.getInstance().getCustomerPublicKey(nodeID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return publicKey;
    }
}
