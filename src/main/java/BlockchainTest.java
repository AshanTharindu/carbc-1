import chainUtil.ChainUtil;
import chainUtil.KeyGenerator;
import controller.Controller;
import core.blockchain.*;
import core.connection.BlockJDBCDAO;
import network.Node;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;

public class BlockchainTest {
    public static void main(String[] args) throws SQLException, InterruptedException {
        Node.getInstance().startNode("abc123", 45673);

        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObjectNewOwner = new JSONObject();
        JSONObject jsonSecondary = new JSONObject();

        jsonObjectNewOwner.put("name", "Ashan");
        jsonObjectNewOwner.put("publicKey", KeyGenerator.getInstance().getPublicKeyAsString());

        jsonSecondary.put("NewOwner", jsonObjectNewOwner);
        jsonObject.put("SecondaryParty", jsonSecondary);
        jsonObject.put("ThirdParty", new JSONArray());


        Controller controller = new Controller();

//
        String sender = KeyGenerator.getInstance().getPublicKeyAsString();
        String nodeID = Node.getInstance().getNodeConfig().getNodeID();
        Block block = Blockchain.createGenesis();

        BlockJDBCDAO blockJDBCDAO = new BlockJDBCDAO();
        Blockchain.addBlocktoBlockchain(block);
        for(int i = 0; i< 6; i++) {
            Transaction transaction = new Transaction("V",sender,"ExchangeOwnership", jsonObject.toString(), nodeID);
            BlockBody blockBody = new BlockBody();
            blockBody.setTransaction(transaction);
            String blockHash = ChainUtil.getInstance().getBlockHash(blockBody);
            BlockHeader blockHeader = new BlockHeader(blockHash);

            Block block1 = new Block(blockHeader, blockBody);
            System.out.println(new JSONObject(blockHeader));
            Blockchain.addBlocktoBlockchain(block1);
            Thread.sleep(1000);
        }

//        System.out.println(blockJDBCDAO.getPreviousHash());

//        long blocknumber = blockJDBCDAO.getRecentBlockNumber();
//        System.out.println(blocknumber);
    }
}
