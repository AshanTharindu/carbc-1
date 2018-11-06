package TestCases;

import core.blockchain.Blockchain;
import org.json.JSONObject;

import java.sql.SQLException;

public class BlockchainTest {
    public static void main(String[] args) throws SQLException {
        testGetBlockchain();
    }

    public static void testGetBlockchain() throws SQLException {
        JSONObject blockchain = Blockchain.getBlockchainJSON(1);
        System.out.println(blockchain);
    }
}
