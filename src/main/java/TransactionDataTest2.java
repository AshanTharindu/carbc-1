import Exceptions.FileUtilityException;
import chainUtil.ChainUtil;
import chainUtil.KeyGenerator;
import config.CommonConfigHolder;
import constants.Constants;
import core.blockchain.*;
import core.consensus.TransactionDataCollector;
import core.serviceStation.dao.ServiceJDBCDAO;
import network.Neighbour;
import network.Node;
import org.json.JSONObject;
import org.slf4j.impl.SimpleLogger;

import javax.swing.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TransactionDataTest2 {
    public static void main(String[] args) throws FileUtilityException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, IOException {
        try {
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
            commonConfigHolder.setConfigUsingResource("peer2");

            /*
             * when initializing the network
             * */
            Node node = Node.getInstance();
            node.initTest();

            /*
             * when we want our node to start listening
             * */
            node.startListening();

//            ServiceJDBCDAO serviceJDBCDAO = new ServiceJDBCDAO();
//            System.out.println(serviceJDBCDAO.getServiceRecords("12345"));
//            System.out.println(Node.getInstance().getNodeId());
////            System.out.println(KeyGenerator.getInstance().getPublicKeyAsString());
//            System.out.println(serviceJDBCDAO.getCustomerPublicKey("3081f13081a806072a8648ce38040130819c0241"));
            String time = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
            TransactionDataCollector.getInstance().requestTransactionDataFromDataOwner("12345",time, new Neighbour("3081f13081a806072a8648ce38040130819c0241","127.0.0.1", 49211));
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
