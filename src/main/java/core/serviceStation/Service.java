package core.serviceStation;

import java.util.ArrayList;

public class Service {
    private int record_id;
    private int service_id;
    private ArrayList<String> spareParts;
    private String spare_part_serial_number;
    private int cost;

    public Service(){}

    public Service(int service_id, String spare_part_serial_number, int cost){
        this.service_id = service_id;
        this.spare_part_serial_number = spare_part_serial_number;
        this.cost = cost;
    }

    public Service(int service_id, ArrayList<String> spareParts){
        this.service_id = service_id;
        this.spareParts = spareParts;
    }

    public int getRecord_id() {
        return record_id;
    }

    public void setRecord_id(int record_id) {
        this.record_id = record_id;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getSparePart() {
        return spare_part_serial_number;
    }

    public void setSparePart(String sparePart) {
        this.spare_part_serial_number = sparePart;
    }

    public ArrayList<String> getSpareParts() {
        return spareParts;
    }

    public void addSparePart(String spareParts) {
        this.spareParts.add(spareParts);
    }

    public void setSpareParts(ArrayList<String> spareParts) {
        this.spareParts = spareParts;
    }
}
