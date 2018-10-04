package core.blockchain;

import chainUtil.ChainUtil;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class BlockHeader {
    private String previousHash;
    private String hash;
    private String signature;
    private String blockTime;
    private long blockNumber;
    private boolean validity = false;

    public BlockHeader(){}

    public BlockHeader(String previousHash, String hash){
        this.previousHash = previousHash;
        this.blockTime = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        this.hash = hash;
        this.blockNumber = ChainUtil.getInstance().getRecentBlockNumber() + 1;
        signature = ChainUtil.getInstance().digitalSignature(hash);
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

    public String getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(String blockTime) {
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

    public String getSignature() {
        return signature;
    }
}
