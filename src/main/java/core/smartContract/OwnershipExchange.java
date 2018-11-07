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
        JSONObject vehicleInfo = blockJDBCDAO.getVehicleInfoByEvent(vehicleId, "ExchangeOwnership");

        if (vehicleInfo.length()>0){
            JSONObject data = new JSONObject(vehicleInfo.getString("data"));
            if ((data.getString("NewOwner").equals(sender))){
                return true;
            }
        }
        return false;
    }


}
