package core.smartContract;

import core.connection.BlockJDBCDAO;
import org.json.JSONObject;

import java.sql.SQLException;

public class OwnershipExchange {
    String vehicleId;
    String sender;

    public OwnershipExchange(String vehicleId, String sender){
        this.vehicleId = vehicleId;
        this.sender = sender;
    }

    public boolean isAuthorizedToSeller() throws SQLException {
        BlockJDBCDAO blockJDBCDAO = new BlockJDBCDAO();


        JSONObject vehicleInfo2 = blockJDBCDAO.getVehicleInfoByEvent(vehicleId, "ExchangeOwnership");

        if (vehicleInfo2.length()>0){
            JSONObject data = new JSONObject(vehicleInfo2.getString("data"));
            if ((data.getString("NewOwner").equals(sender))){
                return true;
            }
        }

        JSONObject vehicleInfo1 = blockJDBCDAO.getVehicleInfoByEvent(vehicleId, "RegisterVehicle");

        if (vehicleInfo1.length()>0){
            JSONObject data = new JSONObject(vehicleInfo1.getString("data"));
            if ((data.getString("current_owner").equals(sender))){
                return true;
            }
        }

        return false;
    }


}
