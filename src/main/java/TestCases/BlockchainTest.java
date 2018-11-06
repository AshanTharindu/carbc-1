package TestCases;

import core.blockchain.Blockchain;
import network.Node;
import org.json.JSONObject;

import java.sql.SQLException;

public class BlockchainTest {
    public static void main(String[] args) throws SQLException {
        requestBlockchain();
    }

    public static void testGetBlockchain() throws SQLException {
        JSONObject blockchain = Blockchain.getBlockchainJSON(1);
        System.out.println(blockchain);
    }

    public static void requestBlockchain() {
        Node.getInstance().startNode();
    }
}
