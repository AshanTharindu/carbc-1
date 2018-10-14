import Exceptions.FileUtilityException;
import chainUtil.ChainUtil;
import chainUtil.KeyGenerator;
import config.CommonConfigHolder;
import constants.Constants;
import core.blockchain.*;
import network.Node;
import network.communicationHandler.MessageSender;
import org.json.JSONObject;
import org.slf4j.impl.SimpleLogger;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TransactionDataTest4 {
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
            commonConfigHolder.setConfigUsingResource("peer3");

            /*
             * when initializing the network
             * */
            Node node = Node.getInstance();
            node.initTest();
            node.getNodeConfig().setNodeID("12345");

            /*
             * when we want our node to start listening
             * */
            node.startListening();
            MessageSender.getInstance().requestIP();


        } catch (Exception e) {
            e.getMessage();
        }
    }
}
