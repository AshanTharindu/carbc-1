import Exceptions.FileUtilityException;
import config.CommonConfigHolder;
import constants.Constants;
import core.blockchain.Block;
import core.consensus.Consensus;
import network.Node;
import org.json.JSONObject;
import org.slf4j.impl.SimpleLogger;

import java.util.jar.JarEntry;

public class AdditionalDataTest4 {

    public static void main(String[] args) throws FileUtilityException {
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

    }
}
