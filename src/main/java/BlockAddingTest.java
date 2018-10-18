import Exceptions.FileUtilityException;
import config.CommonConfigHolder;
import constants.Constants;
import core.blockchain.Block;
import core.blockchain.Blockchain;
import core.connection.BlockJDBCDAO;
import core.serviceStation.webSocketServer.BroadcastServer;
import network.Node;
import org.slf4j.impl.SimpleLogger;

import javax.swing.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BlockAddingTest {
    public static void main(String[] args) throws FileUtilityException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, IOException, InterruptedException {

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

//        Block block = Blockchain.createGenesis();
//        BlockJDBCDAO blockJDBCDAO = new BlockJDBCDAO();
//        System.out.println(block.getBlockHeader().getBlockTime());
//        Blockchain.addBlocktoBlockchain(block);


        /*
         * starting UI web socket communication
         * */
        BroadcastServer broadcastServer = new BroadcastServer();
        broadcastServer.start();

    }
}
