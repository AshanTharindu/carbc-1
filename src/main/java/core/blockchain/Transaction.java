package core.blockchain;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Random;

public class Transaction {
    private String transactionId;
    private String sender;
    private String event;
    private JSONObject data;
    private String address;
    private Timestamp time;

    public Transaction(String transactionType, String sender, String event, JSONObject data, String address, Timestamp time){
        this.transactionId = generateTransactionID(transactionType);
        this.sender = sender;
        this.event = event;
        this.data = data;
        this.address = address;
        this.time = time;
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

    public String generateTransactionID(String transactionType) {
        Random random = new Random();
        int number = 10000 + Math.abs(random.nextInt(90000));
        return transactionType+"-"+ number;
    }

}
