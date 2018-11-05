package core.serviceStation.dao;

import core.connection.ConnectionFactory;
import core.consensus.Consensus;
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

public class ServiceJDBCDAO {
    private static ServiceJDBCDAO INSTANCE;
    private static Object mutex = new Object();

    private final Logger log = LoggerFactory.getLogger(ServiceJDBCDAO.class);
    Connection connection = null;

    private ServiceJDBCDAO(){}

    public static ServiceJDBCDAO getInstance() {
        if(INSTANCE == null){
            synchronized (mutex) {
                if(INSTANCE == null){
                    INSTANCE = new ServiceJDBCDAO();
                }
            }
        }
        return INSTANCE;
    }

    //add a service to service type
    public boolean addServiceType(ServiceType serviceType) throws SQLException {
        String queryString = "INSERT INTO `ServiceType`(`service_type`) " +
                "VALUES (?)";
        PreparedStatement ptmt = null;
        boolean succeed = false;

        try {
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, serviceType.getService_type());
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

    public JSONArray getServiceTypes() throws SQLException {
        System.out.println("get service types");
        String queryString = "SELECT `service_id`, `service_type` FROM `ServiceType`";

        PreparedStatement ptmt = null;
        ResultSet resultSet = null;
        JSONArray serviceTypeArray = new JSONArray();

        try {
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            resultSet = ptmt.executeQuery();

            while (resultSet.next()){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("service_id", resultSet.getInt("service_id"));
                jsonObject.put("service_type", resultSet.getString("service_type"));

                serviceTypeArray.put(jsonObject);
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
            return serviceTypeArray;
        }
    }

    //add a service to the service record and update service table accordingly
    public boolean addServiceRecord(ServiceRecord serviceRecord) throws SQLException {
        PreparedStatement ptmt = null;
        PreparedStatement pstm = null;

        try {
            String queryString = "INSERT INTO `ServiceRecord`( " +
                    "`vehicle_id`, `serviced_date`, `cost`) VALUES (?,?,?)";

            ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
            connection = connectionFactory.getConnection();
            ptmt = connection.prepareStatement(queryString,
                    Statement.RETURN_GENERATED_KEYS);

            ptmt.setString(1, serviceRecord.getVehicle_id());
            ptmt.setTimestamp(2, serviceRecord.getServiced_date());
            ptmt.setInt(3, serviceRecord.getCost());
            boolean succeed = ptmt.execute();

            log.debug("Vehicle id :{}. Status - inserted service record :{}",
                    serviceRecord.getVehicle_id(), succeed);

            ResultSet rs = ptmt.getGeneratedKeys();

            String query = "INSERT INTO `Service`(`record_id`, `service_id`, `spare_part_serial_number`) " +
                    "VALUES (?,?,?)";

            if (rs.next()){
                int record_id = rs.getInt(1);

                pstm = connection.prepareStatement(query);
                pstm.setInt(1, record_id);

                for(int i = 0; i < serviceRecord.getService().size(); i++){
                    Service service = serviceRecord.getService().get(i);
                    pstm.setInt(2, service.getService_id());

                    ArrayList<String> spareParts = service.getSpareParts();
                    for (int j = 0; j < spareParts.size(); j++){
                        pstm.setString(3, spareParts.get(j));
                        succeed = pstm.execute();
                    }
//                    pstm.setString(3, service.getSparePart());
//                    succeed = pstm.execute();

                    log.debug("Record id :{}. Status - inserted service :{}",
                            record_id, succeed);
                }

            }
            log.info("Service record is inserted to the database successfully");

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
            return true;
        }

    }

    //add spare part purchase details
    public boolean addPurchasedSparePart(SparePart sparePart) throws SQLException {
        String queryString = "INSERT INTO `SparePart`(`serial_number`, `spare_part`," +
                " `seller`) VALUES (?,?,?)";
        PreparedStatement ptmt = null;
        boolean succeed = false;

        try {
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, sparePart.getSerial_number());
            ptmt.setString(2, sparePart.getSparepart());
            ptmt.setString(3, sparePart.getSeller());
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

    //return service details by vehicle number
    public JSONObject getLastServiceRecord(String vehicle_id) throws SQLException {
        String queryString = "SELECT s.record_id, `vehicle_id`, `cost`, `serviced_date`, `service_type`," +
                " `spare_part`, `seller` FROM `ServiceRecord` sr INNER JOIN `Service` s " +
                "ON sr.record_id = s.record_id LEFT JOIN `ServiceType` st " +
                "ON s.service_id = st.service_id LEFT JOIN `SparePart` sp " +
                "ON s.spare_part_serial_number = sp.serial_number " +
                "WHERE `vehicle_id` = ? ORDER BY `service_type`, s.record_id";

        PreparedStatement ptmt = null;
        ResultSet resultSet = null;
        JSONObject serviceRecord = new JSONObject();

        try {
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, vehicle_id);
            resultSet = ptmt.executeQuery();

            int count = 0;
            int count2 = 0;
            String temp = "";

            JSONArray services = new JSONArray();
            JSONArray serviceTypeArray = new JSONArray();
            JSONObject service = new JSONObject();

            while (resultSet.next()){
                if (count == 0){
                    int recordId = resultSet.getInt(1);
                    serviceRecord.put("vehicle_id", resultSet.getString("vehicle_id"));
                    serviceRecord.put("serviced_date", resultSet.getTimestamp("serviced_date"));
                    serviceRecord.put("cost", resultSet.getInt("cost"));
                    count = 1;
                }

                String serviceType = resultSet.getString("service_type");
                System.out.println(serviceType);

                if (!temp.equals(serviceType)){
                    if (count2 != 0){
                        service.put("serviceData", serviceTypeArray);
                        services.put(service);
                        serviceTypeArray = new JSONArray();
                    }
                    count2++;
                    service = new JSONObject();
                    service.put("serviceType", serviceType);
                }
                JSONObject serviceData = new JSONObject();
                serviceData.put("sparePart", resultSet.getString("spare_part"));
                serviceData.put("seller", resultSet.getString("seller"));
                serviceTypeArray.put(serviceData);

                temp = serviceType;
            }
            service.put("serviceData", serviceTypeArray);
            services.put(service);
            serviceRecord.put("services", services);

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
            return serviceRecord;
        }
    }

    //return service details by vehicle number
    public JSONArray getAllServiceRecords() throws SQLException {

        String queryString = "SELECT s.record_id, `vehicle_id`, `cost`, `serviced_date`, `service_type`," +
                " `spare_part`, `seller` FROM `ServiceRecord` sr INNER JOIN `Service` s " +
                "ON sr.record_id = s.record_id LEFT JOIN `ServiceType` st " +
                "ON s.service_id = st.service_id LEFT JOIN `SparePart` sp " +
                "ON s.spare_part_serial_number = sp.serial_number ORDER BY s.record_id DESC";

        PreparedStatement ptmt = null;
        ResultSet resultSet = null;
        JSONArray serviceArray = new JSONArray();

        try {
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            resultSet = ptmt.executeQuery();

            serviceArray = formatServiceRecords(resultSet);


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
            return serviceArray;
        }
    }

    public String getCustomerPublicKey(String nodeID) throws SQLException {
        String queryString = "SELECT `public_key` FROM `Customer_details` WHERE `node_id` = ?";

        PreparedStatement ptmt = null;
        ResultSet result = null;
        String publicKey = null;
        try {
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, nodeID);
            result = ptmt.executeQuery();
            result.next();
            publicKey = result.getString("public_key");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (result != null)
                result.close();
            if (ptmt != null)
                ptmt.close();
            if (connection != null)
                connection.close();
            return publicKey;
        }

    }


    public JSONArray getServiceRecordsPerVehicle(String vehicleId) throws SQLException {

        String queryString = "SELECT s.record_id, `vehicle_id`, `cost`, `serviced_date`, `service_type`," +
                " `spare_part`, `seller` FROM `ServiceRecord` sr INNER JOIN `Service` s " +
                "ON sr.record_id = s.record_id LEFT JOIN `ServiceType` st " +
                "ON s.service_id = st.service_id LEFT JOIN `SparePart` sp " +
                "ON s.spare_part_serial_number = sp.serial_number " +
                "WHERE `vehicle_id` = ? ORDER BY s.record_id DESC";

        PreparedStatement ptmt = null;
        ResultSet resultSet = null;
        JSONArray serviceArray = new JSONArray();

        try {
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, vehicleId);
            resultSet = ptmt.executeQuery();

            serviceArray = formatServiceRecords(resultSet);

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
            return serviceArray;
        }
    }

    public JSONArray formatServiceRecords(ResultSet resultSet) throws SQLException {
        JSONArray serviceArray = new JSONArray();
        JSONObject serviceRecord = new JSONObject();
        JSONArray services = new JSONArray();
        JSONArray serviceTypeArray = new JSONArray();
        JSONObject service = new JSONObject();
        int recordId = -1;
        int count1 = 0;
        int count2 = 0;
        String temp = "";

        while (resultSet.next()){
            if (recordId != resultSet.getInt(1)){
                if (count1 != 0){
                    service.put("serviceData", serviceTypeArray);
                    serviceTypeArray = new JSONArray();
                    services.put(service);
                    serviceRecord.put("services", services);
                    services = new JSONArray();
                    serviceArray.put(serviceRecord);
                    count2 = 0;
                }
                count1++;
                serviceRecord = new JSONObject();
                recordId = resultSet.getInt(1);
                serviceRecord.put("recordId", recordId);
                serviceRecord.put("vehicleId", resultSet.getString("vehicle_id"));
                serviceRecord.put("servicedDate", resultSet.getTimestamp("serviced_date"));
            }

            String serviceType = resultSet.getString("service_type");

            if (!temp.equals(serviceType)){
                if (count2 != 0){
                    service.put("serviceData", serviceTypeArray);
                    serviceTypeArray = new JSONArray();
                    services.put(service);
                }
                count2++;
                service = new JSONObject();
                service.put("serviceType", serviceType);

            }
            JSONObject serviceData = new JSONObject();
            serviceData.put("sparePart", resultSet.getString("spare_part"));
            serviceData.put("seller", resultSet.getString("seller"));
            serviceTypeArray.put(serviceData);


            temp = serviceType;
        }
        service.put("serviceData", serviceTypeArray);
        services.put(service);
        serviceRecord.put("services", services);
        serviceArray.put(serviceRecord);

        return serviceArray;
    }
}
