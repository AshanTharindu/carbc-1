package core.rmv.dao;

import core.connection.ConnectionFactory;
import core.rmv.Registration;
import core.serviceStation.Service;
import core.serviceStation.ServiceRecord;
import core.serviceStation.ServiceType;
import core.serviceStation.SparePart;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;

public class RMVJDBCDAO {
    private static RMVJDBCDAO INSTANCE;
    private static Object mutex = new Object();

    private final Logger log = LoggerFactory.getLogger(RMVJDBCDAO.class);
    Connection connection = null;

    private RMVJDBCDAO(){}

    public static RMVJDBCDAO getInstance() {
        if(INSTANCE == null){
            synchronized (mutex) {
                if(INSTANCE == null){
                    INSTANCE = new RMVJDBCDAO();
                }
            }
        }
        return INSTANCE;
    }

    //add a service to service type
    public boolean addRegistrationRecord(Registration registration) throws SQLException {
        String queryString = "INSERT INTO `Registration`(`vehicle_id`, `registration_number`, `current_owner`, " +
                "`engine_number`, `vehicle_class`, `condition_and_note`, `make`, `model`, `year_of_manufacture`) " +
                "VALUES (?,?,?,?,?,?,?,?)";

        PreparedStatement ptmt = null;
        boolean succeed = false;

        try {
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, registration.getRegistrationNumber());
            ptmt.setString(2, registration.getCondition());
            ptmt.setString(3, registration.getEngineNumber());
            ptmt.setString(4, registration.getVehicleClass());
            ptmt.setString(5, registration.getCondition());
            ptmt.setString(6, registration.getMake());
            ptmt.setString(7, registration.getModel());
            ptmt.setString(8, registration.getYearOfManufacture());
            succeed = ptmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ptmt != null)
                ptmt.close();
            if (connection != null)
                connection.close();
            return succeed;
        }
    }

    public JSONObject getRegistrationInfo(String queryString, String type) throws SQLException {


        PreparedStatement ptmt = null;
        ResultSet resultSet = null;
        JSONObject registrationInfo = new JSONObject();

        try{
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, type);
            resultSet = ptmt.executeQuery();

            if (resultSet.next()){
                registrationInfo.put("registrationNumber", resultSet.getString("registration_number"));
                registrationInfo.put("currentOwner", resultSet.getString("current_owner"));
                registrationInfo.put("engineNumber", resultSet.getString("engine_number"));
                registrationInfo.put("vehicleClass", resultSet.getString("vehicle_class"));
                registrationInfo.put("conditionAndNote", resultSet.getString("condition_and_note"));
                registrationInfo.put("make", resultSet.getString("make"));
                registrationInfo.put("model", resultSet.getString("model"));
                registrationInfo.put("yearOfManufacture", resultSet.getString("year_of_manufacture"));
            }

        }catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null)
                resultSet.close();
            if (ptmt != null)
                ptmt.close();
            if (connection != null)
                connection.close();
            return registrationInfo;
        }
    }


    public JSONObject getRegistrationInfoByRegistrationNumber(String registrationNumber) throws SQLException {

        String queryString = "SELECT `registration_number`, `current_owner`, `engine_number`, `vehicle_class`, " +
                "`condition_and_note`, `make`, `model`, `year_of_manufacture` FROM `Registration` WHERE `registration_number` = ?";

        return getRegistrationInfo(queryString, registrationNumber);
    }

    public JSONObject getOwnershipInfo(String registrationNumber) throws SQLException {
        String queryString = "SELECT `id`, `vehicle_registration_number`, `pre_owner`, `new_owner`, `date` FROM `Ownership`" +
                " WHERE `vehicle_registration_number` = ?";

        PreparedStatement ptmt = null;
        ResultSet resultSet = null;
        JSONObject ownershipInfo = new JSONObject();

        try{
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, registrationNumber);
            resultSet = ptmt.executeQuery();

            if (resultSet.next()){
                ownershipInfo.put("registrationNumber", resultSet.getString("vehicle_registration_number"));
                ownershipInfo.put("preOwner", resultSet.getString("pre_owner"));
                ownershipInfo.put("newOwner", resultSet.getString("new_owner"));
                ownershipInfo.put("date", resultSet.getTimestamp("date"));
            }

        }catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null)
                resultSet.close();
            if (ptmt != null)
                ptmt.close();
            if (connection != null)
                connection.close();
            return ownershipInfo;
        }
    }

}
