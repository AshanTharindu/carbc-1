import Exceptions.FileUtilityException;
import config.CommonConfigHolder;
import constants.Constants;
import controller.Controller;
import network.Node;
import org.slf4j.impl.SimpleLogger;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

public class AndroidTest {
    public static void main(String[] args) throws FileUtilityException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, IOException {

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
        node.init();

        /*
         * when we want our node to start listening
         * */
        node.startListening();

        Controller controller = new Controller();
//        controller.startNode();
        controller.testNetwork("192.168.8.102", 42761,"testMessage");
    }
}
