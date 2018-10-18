package core.serviceStation.webSocketServer;

import core.blockchain.Block;
import core.consensus.Consensus;

public class Test extends Thread{

    @Override
    public void run() {
        try {
            Thread.sleep(20000);

            Consensus con = Consensus.getInstance();
            Block block = new Block();
            con.addBlockToNonApprovedBlocks(block);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
