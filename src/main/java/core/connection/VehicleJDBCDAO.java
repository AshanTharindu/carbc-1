package core.connection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleJDBCDAO {

    public boolean searchVehicleByRegistrationNumber(String registrationNumber) throws SQLException {
        Connection connection = null;
        PreparedStatement ptmt = null;
        ResultSet resultSet = null;
        boolean isPresent = true;

        String queryString = "SELECT COUNT(*) FROM `vehicle` WHERE registration_number = ?";

        try {
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, registrationNumber);
            resultSet = ptmt.executeQuery();

            if (resultSet.next()){
                if (resultSet.getInt(1) > 0){
                    isPresent = false;
                }
            }

        } catch (SQLException e) {
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
            return isPresent;
        }
    }
}
