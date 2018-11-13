import chainUtil.ChainUtil;
import chainUtil.KeyGenerator;
import controller.Controller;
import core.blockchain.Block;
import core.blockchain.BlockBody;
import core.blockchain.BlockHeader;
import core.blockchain.Transaction;
import core.connection.HistoryDAO;
import core.consensus.Consensus;
import network.Node;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;

public class HistoryTest {
    public static void main(String[] args) throws SQLException {

        testHandlePendingBlocks();

    }

    public static void testcase1() throws SQLException {
        Node.getInstance().startNode();
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObjectNewOwner = new JSONObject();
        JSONObject jsonSecondary = new JSONObject();

        jsonObjectNewOwner.put("name", "Ashan");
        jsonObjectNewOwner.put("publicKey", KeyGenerator.getInstance().getPublicKeyAsString());

        jsonSecondary.put("NewOwner", jsonObjectNewOwner);
        jsonObject.put("SecondaryParty", jsonSecondary);
        jsonObject.put("ThirdParty", new JSONArray());

        System.out.println(jsonObject);

        String sender = KeyGenerator.getInstance().getPublicKeyAsString();
        String nodeID = Node.getInstance().getNodeConfig().getNodeID();
        Transaction transaction = new Transaction("V", sender, "ExchangeOwnership", jsonObject.toString(), nodeID);

        BlockBody blockBody = new BlockBody();
        blockBody.setTransaction(transaction);
        String blockHash = ChainUtil.getInstance().getBlockHash(blockBody);
        BlockHeader blockHeader = new BlockHeader(blockHash);

        Block block = new Block(blockHeader, blockBody);
//        Consensus.getInstance().broadcastBlock(block, jsonObject.toString());
        String blockHash2 = "15fa2368be9f744db05483fcb02724e17a920069c8b2d62a6113f7e23c848303";

        HistoryDAO historyDAO = new HistoryDAO();
        System.out.println(historyDAO.getAdditionalData(blockHash2));
    }

    public static void testCase2() {
        HistoryDAO historyDAO = new HistoryDAO();
        historyDAO.setValidity("3bf6973a6e403e8813e07b18eed9502b919ab004785fb1f0e9e6aa107fe20a91");
    }

    public static void testCase3() throws SQLException {
//        HistoryDAO historyDAO = new HistoryDAO();
//        String previoushHas = historyDAO.getPreviousHash();
//        System.out.println(previoushHas);
    }

    public static void testisItMyBlock() {
        boolean result = Consensus.getInstance().isItMyBlock("01852e57018fcc8022ccbfba27b3a89d859fe0337c0b90b282108e26d4650a3a");
        System.out.println(result);
    }

    public static void testCase4() throws SQLException {
        HistoryDAO historyDAO = new HistoryDAO();
        boolean previoushHas = historyDAO.checkExistence("01852e57018fcc8022ccbfba27b3a89d859fe0337c0b90b282108e26d4650a3a");
        System.out.println(previoushHas);
    }

    public static void testGetFailedBlockDetails() throws SQLException {
        HistoryDAO historyDAO = new HistoryDAO();
        JSONObject jsonObject = historyDAO.getFailedBlockDetails("3bf6973a6e403e8813e07b18eed9502b919ab004785fb1f0e9e6aa107fe20a91");
        System.out.println(jsonObject.length());
        System.out.println(jsonObject.getString("event"));
        System.out.println(jsonObject.getString("vehicleId"));
        System.out.println(jsonObject.getString("data"));

    }

    public static void testHandlePendingBlocks() throws SQLException {
        HistoryDAO historyDAO = new HistoryDAO();
        historyDAO.handlePendingBlocks("e7c5145e03476b7e8469da691698aaa39bd03d5639494bce82078fe2fb896562");
    }
}
