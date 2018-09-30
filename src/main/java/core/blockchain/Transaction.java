package core.blockchain;

import org.json.JSONObject;

public class Transaction {
    private String transactionId;
    private String sender;
    private String event;
    private JSONObject data;
    private String address;

    public Transaction(String transactionId, String sender, String event, JSONObject data, String address){
        this.transactionId = transactionId;
        this.sender = sender;
        this.event = event;
        this.data = data;
        this.address = address;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getSender() {
        return sender;
    }

    public String getEvent() {
        return event;
    }

    public JSONObject getData() {
        return data;
    }

    public String getAddress() { return address; }

}
