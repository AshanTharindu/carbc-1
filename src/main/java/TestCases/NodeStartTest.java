package TestCases;

import config.CommonConfigHolder;
import constants.Constants;
import controller.Controller;
import core.blockchain.Blockchain;
import network.communicationHandler.MessageSender;
import org.slf4j.impl.SimpleLogger;

public class NodeStartTest {

    public static void main(String[] args) {
//        setPaths();
//        createGensis();
        Controller controller = new Controller();
        controller.startNode();
        MessageSender.requestIP();
    }

    public static void setPaths() {
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
    }

    public static void createGensis() {
        Blockchain.addBlocktoBlockchain(Blockchain.createGenesis());
    }
}
