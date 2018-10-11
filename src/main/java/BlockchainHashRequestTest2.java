import network.Node;
import network.communicationHandler.MessageSender;

public class BlockchainHashRequestTest2 {

    public static void main(String[] args) {
        Node node = Node.getInstance();
        node.startNode("abc567", 43562);

        MessageSender.getInstance().requestBlockchainHashTest();
    }
}
