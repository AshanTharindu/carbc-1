package core.leasingCompany.webSocketServer;

import chainUtil.ChainUtil;
import chainUtil.KeyGenerator;
import core.blockchain.Block;
import core.blockchain.BlockBody;
import core.blockchain.BlockHeader;
import core.blockchain.Transaction;
import core.consensus.Consensus;
import network.Node;
import org.json.JSONArray;
import org.json.JSONObject;

public class Test extends Thread{

    @Override
    public void run() {
        try {
            Thread.sleep(20000);

//            Consensus con = Consensus.getInstance();
//            Block block = new Block();
//            con.addBlockToNonApprovedBlocks(block);

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
            Transaction transaction = new Transaction("V",sender,"ExchangeOwnership", jsonObject.toString(), nodeID);

            BlockBody blockBody = new BlockBody();
            blockBody.setTransaction(transaction);
            String blockHash = ChainUtil.getInstance().getBlockHash(blockBody);
            BlockHeader blockHeader = new BlockHeader(blockHash);

            System.out.println(blockHeader.getBlockTime());
            Block block = new Block(blockHeader, blockBody);

            Consensus.getInstance().testHandleNonApprovedBlock(block);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
