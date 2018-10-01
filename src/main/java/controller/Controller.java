package controller;

import core.blockchain.Block;
import core.consensus.Consensus;
import core.consensus.DataRequester;
import core.consensus.TransactionDataCollector;

import java.sql.Timestamp;

public class Controller {

    public void requestTransactionData(String vehicleID, Timestamp date, String peerID) {
        TransactionDataCollector.getInstance().requestTransactionData(vehicleID, date, peerID);
    }

    public void sendTransaction() {

    }

    public void sendConfirmation(Block block) {
        Consensus.getInstance().sendAgreementForBlock(block);
    }

    public void requestAdditionalData(Block block) {

    }


}
