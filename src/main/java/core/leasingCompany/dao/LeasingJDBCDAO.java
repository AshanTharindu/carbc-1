package core.leasingCompany.dao;

import core.connection.ConnectionFactory;
import core.insuranceCompany.InsuranceRecord;
import core.insuranceCompany.InsuranceType;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LeasingJDBCDAO {
    private static LeasingJDBCDAO INSTANCE;
    private static Object mutex = new Object();

    private final Logger log = LoggerFactory.getLogger(LeasingJDBCDAO.class);
    Connection connection = null;

    private LeasingJDBCDAO(){}

    public static LeasingJDBCDAO getInstance() {
        if(INSTANCE == null){
            synchronized (mutex) {
                if(INSTANCE == null){
                    INSTANCE = new LeasingJDBCDAO();
                }
            }
        }
        return INSTANCE;
    }

    //add a service to service type
    public boolean addInsuranceType(InsuranceType insuranceType) throws SQLException {
        String queryString = "INSERT INTO `InsuranceType`(`insurance_type`) " +
                "VALUES (?)";
        PreparedStatement ptmt = null;
        boolean succeed = false;

        try {
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, insuranceType.getInsurance_type());
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

    //add a service to the service record and update service table accordingly
    public boolean addInsuranceRecord(InsuranceRecord insuranceRecord) throws SQLException {
        PreparedStatement ptmt = null;
        PreparedStatement pstm = null;
        boolean succeed = false;

        try {
            String queryString = "INSERT INTO `InsuranceRecord`( " +
                    "`vehicle_id`, `insured_date`) VALUES (?,?)";

            ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
            connection = connectionFactory.getConnection();
            ptmt = connection.prepareStatement(queryString);

            ptmt.setString(1, insuranceRecord.getVehicle_id());
            ptmt.setTimestamp(2, insuranceRecord.getInsured_date());
            succeed = ptmt.execute();

            log.debug("Vehicle id :{}. Status - inserted insurance record :{}",
                    insuranceRecord.getVehicle_id(), succeed);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (pstm != null)
                pstm.close();
            if (ptmt != null)
                ptmt.close();
            if (connection != null)
                connection.close();
            return succeed;
        }

    }

    public JSONObject getVehicleInsuranceRecords(String vehicleId, String insuranceNumber) throws SQLException {
        String query = "SELECT `record_id`, `vehicle_id`, `insured_date`, `insurance_number`, " +
                "insurance_type, `valid_from`, `valid_to` FROM `InsuranceRecord` ir JOIN " +
                "`InsuranceType` it ON ir.insurance_id = it.insurance_id WHERE `vehicle_id` = ? AND " +
                "`insurance_number` = ?";

        PreparedStatement ptmt = null;
        ResultSet resultSet = null;
        JSONObject insuranceRecord = new JSONObject();

        try {
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(query);
            ptmt.setString(1, vehicleId);
            ptmt.setString(2, insuranceNumber);
            resultSet = ptmt.executeQuery();

            if (resultSet.next()){
                insuranceRecord.put("vehicle_id", resultSet.getString("vehicle_id"));
                insuranceRecord.put("insured_date", resultSet.getTimestamp("insured_date"));
                insuranceRecord.put("insurance_number", resultSet.getString("vehicle_id"));
                insuranceRecord.put("insurance_type", resultSet.getString("insurance_type"));
                insuranceRecord.put("valid_from", resultSet.getString("valid_from"));
                insuranceRecord.put("valid_to", resultSet.getString("valid_to"));
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
            return insuranceRecord;
        }

    }

}
