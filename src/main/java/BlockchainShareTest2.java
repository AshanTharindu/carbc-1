import chainUtil.ChainUtil;
import network.Neighbour;
import network.Node;
import network.communicationHandler.MessageSender;

public class BlockchainShareTest2 {

    public static void main(String[] args) {
        Node node = Node.getInstance();
        node.startNode("abc567", 48765);

        String blockHash = ChainUtil.getHash("blockchain2");

        String sig = ChainUtil.digitalSignature(blockHash);
        Neighbour neighbour = new Neighbour("b753cf335a016c662e54a2b96267a7727697616b", "127.0.0.1", 49211);

        MessageSender.sendSignedBlockChain(neighbour, sig, blockHash);
    }
}
