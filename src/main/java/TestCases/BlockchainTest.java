package TestCases;

import core.blockchain.Blockchain;
import core.connection.BlockJDBCDAO;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class BlockchainTest {
    public static void main(String[] args) throws SQLException, UnsupportedEncodingException {
        testGetBlockchain();
    }

    public static void testGetBlockchain() throws SQLException, UnsupportedEncodingException {
        JSONObject blockchainJSON = Blockchain.getBlockchainJSON(1);

        System.out.println(blockchainJSON);

        JSONArray blockchain = blockchainJSON.getJSONArray("blockchain");

        BlockJDBCDAO blockJDBCDAO = new BlockJDBCDAO();
        blockJDBCDAO.saveBlockchain(blockchain);
    }
}
