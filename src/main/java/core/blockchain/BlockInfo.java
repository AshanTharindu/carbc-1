package core.blockchain;

import org.json.JSONObject;

import java.sql.Timestamp;

public class BlockInfo {
    //details from blockheader
    private String previous_hash;
    private String block_hash;
    private Timestamp block_timestamp;
    private long block_number;
    private boolean validity;

    //details from bloc kbody
    private String transaction_id;
    private String sender;
    private String event;
    private String data;
    private String address;
    private double rating;

    public BlockInfo(){}

    public BlockInfo(String previousHash, String hash, Timestamp blockTime, long blockNumber,
                     boolean validity, String transactionId, String sender, String event,
                     JSONObject data, String address, double rating){
        this.previous_hash = previousHash;
        this.block_hash = hash;
        this.block_timestamp = blockTime;
        this.block_number = blockNumber;
        this.validity = validity;
        this.transaction_id = transactionId;
        this.sender = sender;
        this.event = event;
        this.data = data.toString();
        this.address = address;
        this.rating = rating;
    }

    public String getPreviousHash() {
        return previous_hash;
    }

    public void setPreviousHash(String previousHash) {
        this.previous_hash = previousHash;
    }

    public String getHash() {
        return block_hash;
    }

    public void setHash(String hash) {
        this.block_hash = hash;
    }

    public Timestamp getBlockTime() {
        return block_timestamp;
    }

    public void setBlockTime(Timestamp blockTime) {
        this.block_timestamp = blockTime;
    }

    public long getBlockNumber() {
        return block_number;
    }

    public void setBlockNumber(long blockNumber) {
        this.block_number = blockNumber;
    }

    public boolean isValidity() {
        return validity;
    }

    public void setValidity(boolean validity) {
        this.validity = validity;
    }

    public String getTransactionId() {
        return transaction_id;
    }

    public void setTransactionId(String transactionId) {
        this.transaction_id = transactionId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
