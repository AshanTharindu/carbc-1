import Exceptions.FileUtilityException;
import chainUtil.ChainUtil;
import config.CommonConfigHolder;
import constants.Constants;
import network.Neighbour;
import network.Node;
import network.communicationHandler.MessageSender;
import org.slf4j.impl.SimpleLogger;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

public class BlockchainRequestTest2 {
    public static void main(String[] args) {
        Node node = Node.getInstance();
        node.startNode("pqr567", 48653);

        String blockHash = ChainUtil.getHash("blockchain1");
        String sig = ChainUtil.digitalSignature(blockHash);
        Neighbour neighbour = new Neighbour("b753cf335a016c662e54a2b96267a7727697616b", "127.0.0.1", 49211);

        MessageSender.sendSignedBlockChain(neighbour, sig, blockHash);
    }
}
