package core.smartContract;

import core.blockchain.Block;
import core.consensus.Consensus;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class TimeKeeper extends Thread{
    String blockHash;

    public TimeKeeper(String blockHash){
        this.blockHash = blockHash;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(600000);
            Consensus.getInstance().checkAgreementsForBlock(blockHash);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
