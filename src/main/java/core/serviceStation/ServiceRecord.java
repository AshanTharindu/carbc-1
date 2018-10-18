package core.serviceStation;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;


public class ServiceRecord {
    private int record_id;
    private String vehicle_id;
    private Timestamp serviced_date;
    private ArrayList<Service> services;


    public int getRecord_id() {
        return record_id;
    }

    public void setRecord_id(int record_id) {
        this.record_id = record_id;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public Timestamp getServiced_date() {
        return serviced_date;
    }

    public void setServiced_date(Timestamp serviced_date) {
        this.serviced_date = serviced_date;
    }

    public ArrayList<Service> getService() {
        return services;
    }

    public void setService(Service service) {
        if (services == null){
            this.services = new ArrayList<>();
        }
        this.services.add(service);
    }
}
