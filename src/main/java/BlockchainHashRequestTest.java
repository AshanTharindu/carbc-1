import network.Node;
import network.communicationHandler.MessageSender;

public class BlockchainHashRequestTest {

    public static void main(String[] args) {
        Node node = Node.getInstance();
        node.startNode("pqr567", 48653);
        MessageSender.getInstance().requestIP();

    }
}
