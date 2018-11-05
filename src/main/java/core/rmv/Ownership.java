package core.rmv;

public class Ownership {
    private String id;
    private String vehicle_registration_number;
    private String pre_owner;
    private String new_owner;
    private String date;


    public String getVehicle_registration_number() {
        return vehicle_registration_number;
    }

    public void setVehicle_registration_number(String vehicle_registration_number) {
        this.vehicle_registration_number = vehicle_registration_number;
    }

    public String getPreOwner() {
        return pre_owner;
    }

    public void setPreOwner(String pre_owner) {
        this.pre_owner = pre_owner;
    }

    public String getNewOwner() {
        return new_owner;
    }

    public void setNewOwner(String new_owner) {
        this.new_owner = new_owner;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
