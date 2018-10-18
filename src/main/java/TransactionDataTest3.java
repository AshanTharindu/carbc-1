import Exceptions.FileUtilityException;
import chainUtil.ChainUtil;
import chainUtil.KeyGenerator;
import config.CommonConfigHolder;
import constants.Constants;
import core.blockchain.*;
import core.consensus.AgreementCollector;
import core.consensus.DataCollector;
import network.Client.RequestMessage;
import network.Node;
import network.Protocol.BlockMessageCreator;
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

public class TransactionDataTest3 {
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

            String time = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());

            DataCollector.getInstance().requestTransactionData("repair&service", "12345", time, "12345");

        } catch (Exception e) {
            e.getMessage();
        }
    }
}
