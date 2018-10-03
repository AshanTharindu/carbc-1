package core.blockchain;

import chainUtil.ChainUtil;

import java.sql.Timestamp;

public class BlockHeader {
    private String previousHash;
    private String hash;
    private Timestamp blockTime;
    private long blockNumber;
    private boolean validity = false;

    public BlockHeader(){}

    public BlockHeader(String previousHash, String hash, Timestamp blockTime){
        this.previousHash = previousHash;
        this.blockTime = blockTime;
        this.hash = hash;
        this.blockNumber = ChainUtil.getInstance().getRecentBlockNumber() + 1;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Timestamp getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(Timestamp blockTime) {
        this.blockTime = blockTime;
    }

    public long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public boolean isValidity() {
        return validity;
    }

    public void setValidity(boolean validity) {
        this.validity = validity;
    }
}
