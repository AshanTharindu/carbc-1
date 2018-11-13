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
            JSONObject secondaryParty = data.getJSONObject("SecondaryParty").getJSONObject("NewOwner");
            if ((secondaryParty.getString("publicKey").equals(sender))){
                return true;
            }
        }else {
            JSONObject vehicleInfo1 = blockJDBCDAO.getVehicleInfoByEvent(vehicleId, "RegisterVehicle");

            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++");
            if (vehicleInfo1.length()>0){


                JSONObject data = new JSONObject(vehicleInfo1.getString("data"));
                System.out.println("------------------------------------------------");
                System.out.println(sender);
                System.out.println(data.getString("currentOwner"));
                System.out.println("------------------------------------------------");


                if ((data.getString("currentOwner").equals(sender))){
                    return true;
                }
            }
        }

        return false;
    }


}
