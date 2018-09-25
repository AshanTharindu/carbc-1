package core.smartContract;

import com.google.gson.Gson;
import core.blockchain.*;
import core.connection.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.Map;

public class Main {


    //    public static void main(String[] args) throws Exception {
//        Connection connection = null;
//        PreparedStatement ptmt = null;
//        ResultSet resultSet = null;
//
//        add(connection, ptmt);
//
////        DatabaseClassLoader databaseClassLoader = new DatabaseClassLoader();
////        Class<?> cl = databaseClassLoader.findClass("VehicleRegistration");
//
//    }
//    public static void main(String[] args) throws Exception {
//
////        TransactionDummy tr = new TransactionDummy();
////        tr.executeSmartContractMethod();
//
////        VehicleHistoryJDBCDAO smartContractJDBCDAO = new VehicleHistoryJDBCDAO();
////        VehicleHistory vehicleHistory = new VehicleHistory("a","a","a","a","a","a","a","a");
////
////        smartContractJDBCDAO.add(vehicleHistory);
//
////        System.out.println((getClassFromFile("VehicleRegistration")));
//
//        Connection connection = null;
//        PreparedStatement ptmt = null;
//        ResultSet resultSet = null;
//
//        add(connection, ptmt);
//
////        DatabaseClassLoader databaseClassLoader = new DatabaseClassLoader();
////        Class<?> cl = databaseClassLoader.findClass("VehicleRegistration");
//
//    }


    public static void add(Connection connection, PreparedStatement ptmt) throws FileNotFoundException {

        File file = new File("/home/sajinie/Desktop/VehicleContract.class");
        FileInputStream input = new FileInputStream(file);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        try {
            String queryString = "INSERT INTO `SmartContract`(`signature`,`contractName`, `code`, " +
                    "`owner`, `message`, `block_number`, `block_timestamp`, `block_hash`, `vehicleId`) VALUES(?,?,?,?,?,?,?,?,?)";

            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, "qq");
            ptmt.setString(2, "VehicleContract");
            ptmt.setBinaryStream(3, input);
            ptmt.setString(4, "qq");
            ptmt.setString(5, "qq");
            ptmt.setInt(6, 5);
            ptmt.setTimestamp(7, timestamp);
            ptmt.setString(8, "aaa");
            ptmt.setString(9, "qq");
            ptmt.executeUpdate();
            System.out.println("Data Added Successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                if (ptmt != null)
                    ptmt.close();
                if (connection != null)
                    connection.close();
            }catch(SQLException e){
                e.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public static String getFileContent(FileInputStream fis) throws IOException {
        StringBuilder sb = new StringBuilder();
        Reader r = new InputStreamReader(fis, "UTF-8");  //or whatever encoding
        int ch = r.read();
        while(ch >= 0) {
            sb.append(ch);
            ch = r.read();
        }
        return sb.toString();
    }

    public static String readCodeFromFile(String fileLocation) throws IOException {
        File file = new File(fileLocation);
        FileInputStream input = new FileInputStream(file);
        String contractCode = getFileContent(input);
        return contractCode;
    }

    public static InputStream getContractInputStream(String contract){
        InputStream stream = new ByteArrayInputStream(contract.getBytes(StandardCharsets.UTF_8));
        return stream;
    }

    public void executeTransaction(Block block) throws SQLException, ParseException {
        //getting block details
        BlockHeader blockHeader = block.getHeader();
        int blockNumber = (int)blockHeader.getBlockNumber();
        String blockHash = blockHeader.getHash();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //revert this change
        Date parsedDate = dateFormat.parse(blockHeader.getTimestamp());
        Timestamp blockTimestamp = new java.sql.Timestamp(parsedDate.getTime());

        //getting transaction details
        Transaction transaction = block.getTransaction();

        String transactionID = transaction.getTransactionID();
        String sender = transaction.getSender();
        ArrayList<Validation> validation = transaction.getValidations();
        TransactionInfo transactionInfo = transaction.getTransactionInfo();

        //getting transactionInfo
        String smartContractName = transactionInfo.getSmartContractName();
        String smartContractSignature = transactionInfo.getSmartContractSignature();
        String smartContractMethod = transactionInfo.getSmartContractMethod();
        Object[] parameters = transactionInfo.getParameters();
        String event = transactionInfo.getEvent();
        String data = transactionInfo.getData();
        String vehicleID = transactionInfo.getVehicleId();

        //making validation array
        int noOfValidators = validation.size();
        String validationArray[][] = new String[noOfValidators][2];
        for (int i=0; i<noOfValidators; i++){
            Validation validations = validation.get(i);
            validationArray[i][0] = validations.getValidator().getValidator();
            validationArray[i][1] = validations.getSignature().toString();
        }

//        int noOfParameters = parameters.length;
//        Object[] newParameters = new Object[noOfParameters + 2];
//        for (int i=0; i<noOfParameters+2; i++){
////            newParameters[i]
//        }

        String signedData = getSigningObjectString(sender, transactionInfo);
//        parameters.

        boolean isSuccess = false;
        if (event.equals("deployContract")){
            isSuccess = deploySmartContract(smartContractSignature, smartContractName, data,
                    sender, data, blockNumber, blockTimestamp, blockHash);
        }else{
            VehicleHistoryJDBCDAO vehicleHistoryJDBCDAO = new VehicleHistoryJDBCDAO();
            VehicleHistory vehicleHistory = new VehicleHistory(vehicleID, transactionID,
                    blockNumber, blockHash, event, sender, validationArray.toString(), data, smartContractName,
                    smartContractSignature, smartContractMethod, parameters);

//            System.out.println("other event = " + event);
            isSuccess = addVehicleRecord(signedData, validationArray, vehicleHistoryJDBCDAO, vehicleHistory);
            if(isSuccess){
                vehicleHistoryJDBCDAO.add(vehicleHistory);
            }

        }

    }

    public boolean addVehicleRecord(String signedData, String[][] validations, VehicleHistoryJDBCDAO vehicleHistoryJDBCDAO, VehicleHistory vehicleHistory) throws SQLException {
        //find contract
        SmartContractJDBCDAO smartContractJDBCDAO = new SmartContractJDBCDAO();
        Map contract = smartContractJDBCDAO.getSmartContract(vehicleHistory.getSmartContractSignature());


        //find vehicle
        ResultSet resultSet = vehicleHistoryJDBCDAO.findVehicle(vehicleHistory);
        DatabaseClassLoader databaseClassLoader = new DatabaseClassLoader();

        if(vehicleHistory.getEvent().equals("vehicleRegistration")){
            //check possibility
            if (resultSet.next()){
                return false;
            }
            String vID = vehicleHistory.getTransactionId() + vehicleHistory.getBlockHash();
            vehicleHistory.setVid(vID);
            vehicleHistoryJDBCDAO.add(vehicleHistory);
        }else{
            boolean success = databaseClassLoader.findClass(signedData, validations,
                    (byte[])contract.get("code"), (String)contract.get("contractName"),
                    vehicleHistory.getParameters());
        }
        return true;
    }


    public boolean deploySmartContract(String signature, String contractName, String code,
                                       String owner, String message, int block_number,
                                       Timestamp block_timestamp, String block_hash){

        SmartContractJDBCDAO smartContractJDBCDAO = new SmartContractJDBCDAO();
        SmartContract smartContract = new SmartContract(signature, contractName, code,
                owner, message, block_number, block_timestamp, block_hash);

        boolean contractExists = smartContractJDBCDAO.findDuplicates(smartContract);
        if (!contractExists){
            smartContractJDBCDAO.add(smartContract);
            return true;
        }
        return false;
    }

    public String getSigningObjectString(String sender, TransactionInfo transactionInfo){
        SigningObject object = new SigningObject(sender, transactionInfo);
        Gson gson = new Gson();
        return gson.toJson(object);
    }

}




























//        String message;
//        JSONObject obj = new JSONObject();
//        JSONParser parser = new JSONParser();

//        obj.put("name", "foo");
//        obj.put("num", new Integer(100));
//        obj.put("balance", new Double(1000.21));
//        obj.put("is_vip", new Boolean(true));
//
//        byte[] objAsBytes = obj.toString().getBytes("UTF-8");
//
//        System.out.println(objAsBytes.toString());
//
//
//
//        JSONObject testV = new JSONObject(new String(objAsBytes));
//
//        System.out.println(testV.toString());
//        String testV=new JSONObject(new String(responseBody)).toString();