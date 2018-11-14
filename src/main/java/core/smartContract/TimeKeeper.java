package core.smartContract;

import core.consensus.Consensus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.text.ParseException;

public class TimeKeeper extends Thread{
    String blockHash;
    private final Logger log = LoggerFactory.getLogger(TimeKeeper.class);

    public TimeKeeper(String blockHash){
        this.blockHash = blockHash;
    }

    @Override
    public void run() {
        try {
            log.info("Starting count down. Inside TimeKeeper");
            Thread.sleep(40000);
            System.out.println("Count down finished");
            Consensus.getInstance().checkAgreementsForBlock(blockHash);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


}
