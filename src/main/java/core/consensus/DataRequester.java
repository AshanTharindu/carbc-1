package core.consensus;

import network.Neighbour;
import org.json.JSONObject;

import java.sql.Timestamp;

public class DataRequester {

    private String peerID;
    private String vehicleID;
    private Neighbour dataOwner;
    private JSONObject receivedData;
    private Timestamp date;

    public DataRequester(String peerID, String vehicleID, Timestamp date) {
        this.peerID = peerID;
        this.vehicleID = vehicleID;
        this.date = date;
    }

    public DataRequester(String peerID, String vehicleID, Neighbour dataOwner,Timestamp date ) {
        this.peerID = peerID;
        this.vehicleID = vehicleID;
        this.dataOwner = dataOwner;
        this.date = date;
    }

    public String getPeerID() {
        return peerID;
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public Neighbour getDataOwner() {
        return dataOwner;
    }

    public void setDataOwner(Neighbour dataOwner) {
        this.dataOwner = dataOwner;
    }

    public JSONObject getReceivedData() {
        return receivedData;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setReceivedData(JSONObject receivedData) {
        this.receivedData = receivedData;
    }
}
