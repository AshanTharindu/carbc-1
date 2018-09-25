package core.blockchain;

import core.smartContract.Main;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.LinkedList;

public class Blockchain {
    private  LinkedList<Block> blockchainArray;
    private static Blockchain blockchain;

    private Blockchain() {
        this.blockchainArray = new LinkedList<Block>();
        this.blockchainArray.add(Block.createGenesis());
    }

    public static Blockchain getInstance() {
        if (blockchain == null){
            blockchain = new Blockchain();
        }
        return blockchain;
    }

    public  LinkedList<Block> getBlockchainArray() {
        return blockchainArray;
    }

    public void addBlock(Block block) throws SQLException, ParseException {
        this.blockchainArray.add(block);
        Main main = new Main();
//        Main.main(new String[2]);
        main.executeTransaction(block);
    }


    public  void rollBack(long blockNumber){
        LinkedList<Block> validBlockchain = new LinkedList<Block>();
        if (blockchainArray.size()>(int)blockNumber){
            validBlockchain = (LinkedList<Block>) blockchainArray.subList(0, (int) blockNumber);
            blockchainArray = validBlockchain;
            //should save invalid blocks in some whare else
        }
    }


    public Block getBlockByNumber(long blockNumber){
        if (blockchainArray.size()>(int)blockNumber){
            Block block = blockchainArray.get((int) blockNumber);
            return block;
        }
        return null;
    }


}
