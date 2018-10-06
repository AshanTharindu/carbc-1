import Exceptions.FileUtilityException;
import chainUtil.KeyGenerator;
import config.CommonConfigHolder;
import constants.Constants;
import controller.Controller;
import core.blockchain.*;
import network.communicationHandler.MessageSender;
import network.Node;
import org.json.JSONObject;
import org.slf4j.impl.SimpleLogger;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AndroidTest2 {
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

            Controller controller = new Controller();
            controller.testNetwork("127.0.0.1", 49211, "TestMessage");


        } catch (Exception e) {
            e.getMessage();
        }
    }
}
