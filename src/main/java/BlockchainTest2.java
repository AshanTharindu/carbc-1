import constants.Constants;
import core.blockchain.Blockchain;
import network.Node;
import org.slf4j.impl.SimpleLogger;

public class BlockchainTest2 {
    public static void main(String[] args) {

        Node node = Node.getInstance();
        node.startNode();
    }
}
