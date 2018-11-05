package core.insuranceCompany;

import java.sql.Date;
import java.sql.Timestamp;

public class InsuranceRecord {
    private int record_id;
    private String vehicle_id;
    private Timestamp insured_date;
    private int insurance_id;
    private String insurance_number;
    private Date valid_from;
    private Date valid_to;


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

    public Timestamp getInsured_date() {
        return insured_date;
    }

    public void setInsured_date(Timestamp insured_date) {
        this.insured_date = insured_date;
    }

    public int getInsurance_id() {
        return insurance_id;
    }

    public void setInsurance_id(int insurance_type) {
        this.insurance_id = insurance_type;
    }

    public String getInsurance_number() {
        return insurance_number;
    }

    public void setInsurance_number(String insurance_number) {
        this.insurance_number = insurance_number;
    }

    public Date getValid_from() {
        return valid_from;
    }

    public void setValid_from(Date valid_from) {
        this.valid_from = valid_from;
    }

    public Date getValid_to() {
        return valid_to;
    }

    public void setValid_to(Date valid_to) {
        this.valid_to = valid_to;
    }
}
