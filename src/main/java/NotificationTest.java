import core.blockchain.Block;
import core.consensus.Consensus;

public class NotificationTest {


    public static void main(String[] args) {
        Consensus con = Consensus.getInstance();
        Block block = new Block();
        con.addBlockToNonApprovedBlocks(block);
    }



}
