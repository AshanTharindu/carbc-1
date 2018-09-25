import Exceptions.FileUtilityException;
import config.CommonConfigHolder;
import constants.Constants;
import network.Node;
import network.communicationHandler.MessageSender;
import org.json.JSONObject;
import org.slf4j.impl.SimpleLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BlockchainRequestTest {
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
        //node.init();

        /*
         * when we want our node to start listening
         * */
        //node.startListening();

        ArrayList<String> blockchain = new ArrayList<>();
        blockchain.add("block1");
        blockchain.add("block2");
        blockchain.add("block3");
        JSONObject jsonObject = new JSONObject(blockchain);
        System.out.println(jsonObject.toString());

        Map<Integer, Integer> h = new HashMap<Integer, Integer>();
        int arr[] = new int[]{2, 2, 1, 3, 5, 5, 5, 9, 9, 0};
        for (int i = 0; i < arr.length; i++) {
            if (h.containsKey(arr[i])) {
                h.put(arr[i], h.get(arr[i]) + 1);
            } else {
                h.put(arr[i], 1);
            }
        }
        System.out.println(h);

        int max = 0;
        int key2 = 0;
        for(int key : h.keySet()) {
            if(h.get(key) > max) {
                max = h.get(key);
                key2 = key;
            }
        }
        System.out.println("max is: "+max);
        System.out.println("key is: "+key2);

    }
}
