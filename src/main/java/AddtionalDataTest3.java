import Exceptions.FileUtilityException;
import chainUtil.ChainUtil;
import chainUtil.KeyGenerator;
import config.CommonConfigHolder;
import constants.Constants;
import controller.Controller;
import core.blockchain.*;
import core.connection.HistoryDAO;
import core.consensus.AgreementCollector;
import network.Client.RequestMessage;
import network.Node;
import network.Protocol.BlockMessageCreator;
import network.communicationHandler.MessageSender;
import org.json.JSONArray;
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

public class AddtionalDataTest3 {
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

            JSONObject jsonObject = new JSONObject();
            JSONObject jsonObjectNewOwner = new JSONObject();
            JSONObject jsonSecondary = new JSONObject();

            jsonObjectNewOwner.put("name", "Ashan");
            jsonObjectNewOwner.put("publicKey", KeyGenerator.getInstance().getPublicKeyAsString());

            jsonSecondary.put("NewOwner", jsonObjectNewOwner);
            jsonObject.put("SecondaryParty", jsonSecondary);
            jsonObject.put("ThirdParty", new JSONArray());


            Controller controller = new Controller();

            System.out.println(jsonObject);
//
            String sender = KeyGenerator.getInstance().getPublicKeyAsString();
            String nodeID = Node.getInstance().getNodeConfig().getNodeID();
            Transaction transaction = new Transaction("V",sender,"ExchangeOwnership", ChainUtil.getHash(jsonObject.toString()), nodeID);

            BlockBody blockBody = new BlockBody();
            blockBody.setTransaction(transaction);
            String blockHash = ChainUtil.getInstance().getBlockHash(blockBody);
            BlockHeader blockHeader = new BlockHeader(blockHash);

            Block block = new Block(blockHeader, blockBody);

            HistoryDAO historyDAO = new HistoryDAO();
            System.out.println(blockHash);
//            historyDAO.saveBlockWithAdditionalData(block, jsonObject);
            System.out.println(historyDAO.getAdditionalData("d71c392a44627ef5417acb20582eba26accc9845623d8f74c3d1c5f82659229f"));

        } catch (Exception e) {
            e.getMessage();
        }
    }
}
