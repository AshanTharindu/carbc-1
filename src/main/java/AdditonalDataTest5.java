import Exceptions.FileUtilityException;
import config.CommonConfigHolder;
import constants.Constants;
import core.consensus.DataCollector;
import network.Neighbour;
import network.Node;
import network.Protocol.MessageCreator;
import network.communicationHandler.MessageSender;
import org.json.JSONObject;
import org.slf4j.impl.SimpleLogger;

public class AdditonalDataTest5 {

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
//
//        String blockHash = "d71c392a44627ef5417acb20582eba26accc9845623d8f74c3d1c5f82659229f";
//        Neighbour dataRequester = new Neighbour("3081f13081a806072a8648ce38040130819c0241", "127.0.0.1", 49222);
//
//        DataCollector.getInstance().sendAdditionalData(blockHash, dataRequester);

        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject.put("test1", "Test1");
        jsonObject.put("test2", "Test2");
        jsonObject1.put("additionalData", jsonObject.toString());
        jsonObject1.put("blockHash", "23###%#$$^");
        MessageSender.sendAdditionalData(jsonObject1, new Neighbour("3081f13081a806072a8648ce38040130819c0241", "127.0.0.1", 49222));

    }
}
