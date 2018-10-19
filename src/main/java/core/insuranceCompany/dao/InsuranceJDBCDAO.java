package core.insuranceCompany.dao;

import core.connection.ConnectionFactory;
import core.insuranceCompany.InsuranceRecord;
import core.insuranceCompany.InsuranceType;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class InsuranceJDBCDAO {
    private static InsuranceJDBCDAO INSTANCE;
    private static Object mutex = new Object();

    private final Logger log = LoggerFactory.getLogger(InsuranceJDBCDAO.class);
    Connection connection = null;

    private InsuranceJDBCDAO(){}

    public static InsuranceJDBCDAO getInstance() {
        if(INSTANCE == null){
            synchronized (mutex) {
                if(INSTANCE == null){
                    INSTANCE = new InsuranceJDBCDAO();
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

    public JSONObject getVehicleInfo(String vehicleId) throws SQLException {
        String query = "SELECT `record_id`, `vehicle_id`, `insured_data`, `insurance_id`, " +
                "`insurance_number`, `valid_from`, `valid_to` FROM `InsuranceRecord` ir JOIN " +
                "`InsuranceType` it ON ir.insurance_id = it.insurance_id WHERE `vehicle_id` = ?";

        PreparedStatement ptmt = null;
        ResultSet resultSet = null;
        JSONObject insuranceRecord = new JSONObject();

        try {
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(query);
            ptmt.setString(1, vehicleId);
            resultSet = ptmt.executeQuery();

            if (resultSet.next()){
                //TODO
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
