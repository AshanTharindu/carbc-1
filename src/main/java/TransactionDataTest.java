import Exceptions.FileUtilityException;
import config.CommonConfigHolder;
import constants.Constants;
import core.consensus.Consensus;
import core.consensus.TransactionDataCollector;
import core.serviceStation.Service;
import core.serviceStation.ServiceRecord;
import core.serviceStation.dao.ServiceJDBCDAO;
import network.Node;
import org.slf4j.impl.SimpleLogger;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TransactionDataTest {
    public static void main(String[] args) throws FileUtilityException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, IOException, SQLException {
        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");

        /*
         * Set the main directory as home
         * */
        System.setProperty(Constants.CARBC_HOME, System.getProperty("user.dir"));

        /*
         * At the very beginning
         * A Config common to all: network, blockchain, etc.
         * */
        CommonConfigHolder commonConfigHolder = CommonConfigHolder.getInstance();
        commonConfigHolder.setConfigUsingResource("peer1");
        /*
         * when initializing the network
         * */
        Node node = Node.getInstance();
        node.initTest();

        /*
         * when we want our node to start listening
         * */
        node.startListening();

//        String time = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
//        ServiceJDBCDAO serviceJDBCDAO = new ServiceJDBCDAO();
//
//        Service service = new Service();
//        service.setSparePart("8888");
//        service.setService_id(14);
//
//        ServiceRecord serviceRecord = new ServiceRecord();
//        serviceRecord.setVehicle_id("23456");
//        serviceRecord.setServiced_date(new Timestamp(System.currentTimeMillis()));
//        serviceRecord.setCost(100000);
//        serviceRecord.setService(service);
//
//        serviceJDBCDAO.addServiceRecord(serviceRecord);

//        TransactionDataCollector.getInstance().requestTransactionData("ca83838", time,"abcd1234");
    }
}
