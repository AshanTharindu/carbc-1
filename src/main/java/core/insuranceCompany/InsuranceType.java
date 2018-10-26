package core.insuranceCompany;

public class InsuranceType {
    private String insurance_id;
    private String insurance_type;

    public InsuranceType(){}

    public InsuranceType(String insurance_type){
        this.insurance_type = insurance_type;
    }

    public String getInsurance_id() {
        return insurance_id;
    }

    public void setInsurance_id(String insurance_id) {
        this.insurance_id = insurance_id;
    }

    public String getInsurance_type() {
        return insurance_type;
    }

    public void setInsurance_type(String insurance_type) {
        this.insurance_type = insurance_type;
    }
}
