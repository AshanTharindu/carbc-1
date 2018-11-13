package core.rmv.dao;

import core.connection.ConnectionFactory;
import core.rmv.Registration;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

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

//    //add a service to service type
//    public boolean addRegistrationRecord(Registration registration) throws SQLException {
//        String queryString = "INSERT INTO `Registration`(`vehicle_id`, `registration_number`, `current_owner`, " +
//                "`engine_number`, `chassis_number`, `make`, `model`) " +
//                "VALUES (?,?,?,?,?,?,?)";
//
//        PreparedStatement ptmt = null;
//        boolean succeed = false;
//
//        try {
//            connection = ConnectionFactory.getInstance().getConnection();
//            ptmt = connection.prepareStatement(queryString);
//            ptmt.setString(1, registration.getRegistrationNumber());
//            ptmt.setString(2, registration.getCondition());
//            ptmt.setString(3, registration.getEngineNumber());
//            ptmt.setString(4, registration.getChassisNumber());
//            ptmt.setString(5, registration.getCondition());
//            ptmt.setString(6, registration.getMake());
//            ptmt.setString(7, registration.getModel());
//            succeed = ptmt.execute();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (ptmt != null)
//                ptmt.close();
//            if (connection != null)
//                connection.close();
//            return succeed;
//        }
//    }

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
                registrationInfo.put("chassisNumber", resultSet.getString("chassis_number"));
                registrationInfo.put("make", resultSet.getString("make"));
                registrationInfo.put("model", resultSet.getString("model"));
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

        String queryString = "SELECT `registration_number`, `current_owner`, `engine_number`, `chassis_number`, " +
                "`make`, `model` FROM `Registration` WHERE `registration_number` = ?";

        return getRegistrationInfo(queryString, registrationNumber);
    }

    public JSONObject getRegistrationInfo(String vehicleId) throws SQLException {
        String queryString = "SELECT Registration.registration_number, Registration.current_owner, `vehicle_id` FROM " +
                "`Registration` INNER JOIN `vehicle` ON vehicle.registration_number = Registration.registration_number WHERE `vehicle_id` = ?";

        PreparedStatement ptmt = null;
        ResultSet resultSet = null;
        JSONObject ownershipInfo = new JSONObject();

        try{
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, vehicleId);
            resultSet = ptmt.executeQuery();

            if (resultSet.next()){
                ownershipInfo.put("registrationNumber", resultSet.getString("registration_number"));
                ownershipInfo.put("currentOwner", resultSet.getString("current_owner"));
                ownershipInfo.put("vehicleId", resultSet.getString("vehicle_id"));
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

    public JSONObject getOwnershipInfo(String vehicleId) throws SQLException {
        String queryString = "SELECT `vehicle_registration_number`, `pre_owner`, `new_owner`, `date`, `vehicle_id` FROM `Ownership` INNER " +
                "JOIN `vehicle` ON vehicle.vehicle_id = Ownership.vehicle_registration_number WHERE `vehicle_id` = ?";

        PreparedStatement ptmt = null;
        ResultSet resultSet = null;
        JSONObject ownershipInfo = new JSONObject();

        try{
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, vehicleId);
            resultSet = ptmt.executeQuery();

            if (resultSet.next()){
                ownershipInfo.put("registrationNumber", resultSet.getString("vehicle_registration_number"));
                ownershipInfo.put("preOwner", resultSet.getString("pre_owner"));
                ownershipInfo.put("newOwner", resultSet.getString("new_owner"));
                ownershipInfo.put("date", resultSet.getTimestamp("date"));
                ownershipInfo.put("vehicleId", resultSet.getTimestamp("vehicle_id"));
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
