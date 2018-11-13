package core.rmv;

public class Registration {

    private String registrationNumber;
    private String currentOwner;
    private String engineNumber;
    private String chassisNumber;
    private String make;
    private String model;

    public Registration(String registrationNumber
            , String currentOwner
            , String engineNumber
            , String chassisNumber
            , String make
            , String model){

        this.registrationNumber = registrationNumber;
        this.currentOwner = currentOwner;
        this.engineNumber = engineNumber;
        this.chassisNumber = chassisNumber;
        this.make = make;
        this.model = model;

    }


    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getCurrentOwner() {
        return currentOwner;
    }

    public void setCurrentOwner(String currentOwner) {
        this.currentOwner = currentOwner;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }


}
