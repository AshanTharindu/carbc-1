import core.blockchain.Blockchain;
import network.Node;
import network.communicationHandler.MessageSender;

public class BlockchainHashRequestTest {

    public static void main(String[] args) {
        Node node = Node.getInstance();
        node.startNode("pqr567", 48653);
//        MessageSender.requestIP();
        getHash();

    }

    public static void getHash() {
        try{
            System.out.println(Blockchain.getBlockchainJSON(1).getJSONArray("blockchain").toString());

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
