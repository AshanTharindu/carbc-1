import constants.Constants;
import core.blockchain.Blockchain;
import network.Node;
import org.slf4j.impl.SimpleLogger;

public class BlockchainTest2 {
    public static void main(String[] args) {
//        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");
//
//        /*
//         * Set the main directory as home
//         * */
//        System.setProperty(Constants.CARBC_HOME, System.getProperty("user.dir"));
//
//        Node node = Node.getInstance();
//        node.startNode("pqr567", 48653);
//        Blockchain.runBlockChain();

        Node node = Node.getInstance();
        node.startNode();
    }
}
