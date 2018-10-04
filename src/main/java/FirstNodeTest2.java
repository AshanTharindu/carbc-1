import Exceptions.FileUtilityException;
import chainUtil.KeyGenerator;
import config.CommonConfigHolder;
import constants.Constants;
import controller.Controller;
import network.communicationHandler.MessageSender;
import network.Node;
import org.json.JSONObject;
import org.slf4j.impl.SimpleLogger;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;

public class FirstNodeTest2 {
    public static void main(String[] args) throws FileUtilityException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, IOException, InterruptedException, ParseException {
//        System.out.println(KeyGenerator.getInstance().getPublicKeyAsString());


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

        MessageSender.getInstance().requestIP(49222);
        Thread.sleep(4000);

        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObjectNewOwner = new JSONObject();

        jsonObjectNewOwner.put("name", "Ashan");
        jsonObjectNewOwner.put("address", Node.getInstance().getNodeConfig().getNodeID());

        jsonObject.put("SecondaryParty", jsonObjectNewOwner);
        jsonObject.put("ThirdParty", new JSONObject());


        Controller controller = new Controller();

        System.out.println(jsonObject);
        controller.sendTransaction("V","ExchangeOwnership", jsonObject);
    }
}
