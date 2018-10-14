import Exceptions.FileUtilityException;
import chainUtil.KeyGenerator;
import config.CommonConfigHolder;
import constants.Constants;
import core.blockchain.*;
import core.consensus.DataCollector;
import network.Node;
import network.communicationHandler.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.SimpleLogger;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class AdditionalDataRequestTest {
    public static void main(String[] args) throws FileUtilityException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, IOException {
        Logger log = LoggerFactory.getLogger(DataCollector.class);

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
        node.getNodeConfig().setNodeID("5678");
        /*
        * when we want our node to start listening
        * */
        node.startListening();

        MessageSender.getInstance().requestIP();

    }
}
