package core.connection;

import chainUtil.ChainUtil;
import core.blockchain.BlockInfo;
import core.consensus.Consensus;
import core.rmv.Registration;
import core.rmv.dao.RMVJDBCDAO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.text.ParseException;

public class BlockJDBCDAO {
    private final Logger log = LoggerFactory.getLogger(BlockJDBCDAO.class);

    public boolean addBlockToBlockchain(BlockInfo blockInfo, Identity identity) throws SQLException {
        log.info("trying to add the block {} to blockchain:", blockInfo.getHash());

        Connection connection = null;
        PreparedStatement ptmt = null;
        PreparedStatement psmt = null;

        String transactionId = blockInfo.getTransactionId();
        String transactionType = transactionId.substring(0, 1);
        String query = "";


        if (transactionType.equals("I")){
            query = "INSERT INTO `Identity`(`block_hash`, `role`, `name`) " +
                    "VALUES (?,?,?,?)";
        }

        JSONObject data = new JSONObject(blockInfo.getData());
        try {
            String queryString = "INSERT INTO `Blockchain`(`previous_hash`, " +
                    "`block_hash`, `block_timestamp`, `block_number`, `validity`," +
                    " `transaction_id`, `sender`, `event`, `data`, `address`, `rating`) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?)";

            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);

            ptmt.setString(1, blockInfo.getPreviousHash());
            ptmt.setString(2, blockInfo.getHash());
            ptmt.setTimestamp(3, blockInfo.getBlockTime());
            ptmt.setLong(4, blockInfo.getBlockNumber());
            ptmt.setBoolean(5, blockInfo.isValidity());
            ptmt.setString(6, blockInfo.getTransactionId());
            ptmt.setString(7, blockInfo.getSender());
            ptmt.setString(8, blockInfo.getEvent());
            ptmt.setString(9, blockInfo.getData());
            ptmt.setString(10, blockInfo.getAddress());
            ptmt.setDouble(11, blockInfo.getRating());
            ptmt.executeUpdate();
            log.info("successfully updated Blockchain table");

            if (transactionType.equals("I")){
                psmt = connection.prepareStatement(query);
                psmt.setString(1, identity.getBlock_hash());
                psmt.setString(2, identity.getPublic_key());
                psmt.setString(3, identity.getRole());
                psmt.setString(4, identity.getName());
                psmt.executeUpdate();
                log.info("successfully updated Identity table");
            }

            if (blockInfo.getEvent().equals("ExchangeOwnership")){
                String query2 = "UPDATE `vehicle` SET `current_owner`= ? WHERE `vehicle_id` = ?";

                psmt = connection.prepareStatement(query2);
                psmt.setString(1, data.getJSONObject("SecondaryParty").getJSONObject("NewOwner").getString("publicKey"));
                psmt.setString(2, blockInfo.getAddress());
                psmt.executeUpdate();
                log.info("successfully updated Vehicle table");
            }

            if(blockInfo.getEvent().equals("RegisterVehicle")){
                String query1 = "INSERT INTO `vehicle`(`registration_number`, `vehicle_id`, `current_owner`) " +
                        "VALUES (?,?,?)";

                psmt = connection.prepareStatement(query1);
                psmt.setString(1, data.getString("registrationNumber"));
                psmt.setString(2, blockInfo.getAddress());
                psmt.setString(3, data.getString("currentOwner"));
                psmt.executeUpdate();
                log.info("successfully updated Vehicle table");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (ptmt != null)
                ptmt.close();
            if (psmt != null)
                psmt.close();
            if (connection != null)
                connection.close();
            return true;
        }

    }

    public JSONObject getRegistrationInfoByRegistrationNumber(String registrationNumber) throws SQLException {
        Connection connection = null;
        PreparedStatement ptmt = null;
        ResultSet resultSet = null;
        JSONObject vehicleInfo = new JSONObject();

        String queryString = "SELECT `data`, `address`, `rating`, `current_owner` FROM `Blockchain` INNER JOIN `vehicle`" +
                " ON Blockchain.address = vehicle.vehicle_id WHERE `validity` = 1 AND `event` = 'RegisterVehicle' AND " +
                "vehicle.registration_number = ?";

        try {
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, registrationNumber);
            resultSet = ptmt.executeQuery();

            if (resultSet.next()){
                JSONObject data = new JSONObject(resultSet.getString("data"));

                vehicleInfo.put("current_owner", resultSet.getString("current_owner"));
                vehicleInfo.put("engine_number", data.getString("engine_number"));
                vehicleInfo.put("vehicle_class", data.getString("vehicle_class"));
                vehicleInfo.put("condition_and_note", data.getString("condition_and_note"));
                vehicleInfo.put("make", data.getString("make"));
                vehicleInfo.put("model", data.getString("model"));
                vehicleInfo.put("year_of_manufacture", data.getString("year_of_manufacture"));
                vehicleInfo.put("registration_number", data.getString("registration_number"));
                vehicleInfo.put("rating", resultSet.getDouble("rating"));
                vehicleInfo.put("address", resultSet.getString("address"));

                System.out.println(vehicleInfo);
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
            return vehicleInfo;
        }
    }

    public JSONArray getVehicleInfoByRegistrationNumber(String registrationNumber, String event) throws SQLException {
        Connection connection = null;
        PreparedStatement ptmt = null;
        ResultSet resultSet = null;
        JSONArray jsonArray = new JSONArray();

        String queryString = "SELECT `data`, `event`, `address`, `rating`, `current_owner` FROM `Blockchain` INNER JOIN `vehicle`" +
                " ON Blockchain.address = vehicle.vehicle_id WHERE vehicle.registration_number = ? " +
                "AND `validity` = 1 ORDER BY `block_number` DESC";

        try {
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, registrationNumber);
            resultSet = ptmt.executeQuery();

            if (resultSet.next()){
                JSONObject vehicleInfo = new JSONObject();
                JSONObject data = new JSONObject(resultSet.getString("data"));
                vehicleInfo.put("data", resultSet.getString("data"));
                vehicleInfo.put("address", data.getString("address"));
                vehicleInfo.put("rating", data.getString("rating"));
                vehicleInfo.put("current_owner", data.getString("current_owner"));

                jsonArray.put(vehicleInfo);
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
            return jsonArray;
        }
    }

    public JSONArray getVehicleInfoByRegistrationNumberAndEvent(String registrationNumber, String event) throws SQLException {
        Connection connection = null;
        PreparedStatement ptmt = null;
        ResultSet resultSet = null;
        JSONArray jsonArray = new JSONArray();

        String queryString = "SELECT `data`, `address`, `rating`, `current_owner` FROM `Blockchain` INNER JOIN `vehicle`" +
                " ON Blockchain.address = vehicle.vehicle_id WHERE vehicle.registration_number = ? " +
                "AND `event` = ? AND `validity` = 1 ORDER BY `block_number` DESC LIMIT 1";

        try {
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, registrationNumber);
            ptmt.setString(2, event);
            resultSet = ptmt.executeQuery();

            if (resultSet.next()){
                JSONObject vehicleInfo = new JSONObject();
                JSONObject data = new JSONObject(resultSet.getString("data"));

                vehicleInfo.put("data", resultSet.getString("data"));
                vehicleInfo.put("address", data.getString("address"));
                vehicleInfo.put("rating", data.getString("rating"));
                vehicleInfo.put("current_owner", data.getString("current_owner"));

                jsonArray.put(vehicleInfo);
                System.out.println(vehicleInfo);
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
            return jsonArray;
        }
    }

    public void saveBlockchain(JSONArray blockchain) throws SQLException {
        System.out.println("inside BlockJDBCDAO/addBlockToBlockchain()");

        Connection connection = null;
        PreparedStatement ptmt = null;
        PreparedStatement psmt = null;
        ResultSet resultSet = null;

        final int batchSize = blockchain.length();
        int count = 0;

        try {
            String queryString = "INSERT INTO `Blockchain`(`previous_hash`, " +
                    "`block_hash`, `block_timestamp`, `block_number`, `validity`," +
                    " `transaction_id`, `sender`, `event`, `data`, `address`, `rating`) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?)";

            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);

            for (int i = 0; i < blockchain.length(); i++){
                JSONObject block = blockchain.getJSONObject(i);
                ptmt.setString(1, block.getString("previous_hash"));
                ptmt.setString(2, block.getString("block_hash"));
                ptmt.setTimestamp(3, ChainUtil.convertStringToTimestamp2(block.getString("block_timestamp")));
                ptmt.setLong(4, block.getLong("block_number"));
                ptmt.setBoolean(5, true);
                ptmt.setString(6, block.getString("transaction_id"));
                ptmt.setString(7, block.getString("sender"));
                ptmt.setString(8, block.getString("event"));
                String data = block.getString("data");
                String jsonFormattedString = data.replaceAll("\\\\", "");
                ptmt.setString(9, jsonFormattedString);
                ptmt.setString(10, block.getString("address"));
                ptmt.setDouble(11, block.getDouble("rating"));

                ptmt.addBatch();

                if(++count % batchSize == 0) {
                    ptmt.executeBatch();
                }
            }
            ptmt.executeBatch();

            System.out.println("Block is Added Successfully");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ptmt != null)
                ptmt.close();
            if (psmt != null)
                psmt.close();
            if (connection != null)
                connection.close();
        }
    }

    public JSONObject getBlockchain(long blockNumber) throws SQLException {
        Connection connection = null;
        PreparedStatement ptmt = null;
        ResultSet resultSet = null;
        JSONObject convertedResultSet = null;

        String queryString = "SELECT `previous_hash`, `block_hash`, `block_timestamp`, " +
                "`block_number`, `transaction_id`, `sender`, `event`, `data`, `address`, `rating` " +
                "FROM `Blockchain` WHERE `block_number` > ? AND `validity` = 1";
        String blockchain = "";

        try {
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setLong(1, blockNumber);
            resultSet = ptmt.executeQuery();
            convertedResultSet = convertResultSetIntoJSON(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ptmt != null)
                ptmt.close();
            if (resultSet != null)
                resultSet.close();
            if (connection != null)
                connection.close();
            return convertedResultSet;
        }
    }


    public static JSONObject convertResultSetIntoJSON(ResultSet resultSet) throws Exception {
        JSONObject result = new JSONObject();
        int count = 0;

        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            count++;
            int total_rows = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();

            for (int i = 0; i < total_rows; i++) {
                String columnName = resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase();
                Object columnValue = resultSet.getObject(i + 1);

                // if value in DB is null, then we set it to default value
                if (columnValue == null){
                    columnValue = "null";
                }
                /*
                Next if block is a hack. In case when in db we have values like price and price1 there's a bug in jdbc -
                both this names are getting stored as price in ResulSet. Therefore when we store second column value,
                we overwrite original value of price. To avoid that, i simply add 1 to be consistent with DB.
                 */
                if (obj.has(columnName)){
                    columnName += "1";
                }

                obj.put(columnName, columnValue);
            }
            jsonArray.put(obj);
        }
        result.put("blockchainLength", count);
        result.put("blockchain", jsonArray);
        return result;
    }


    //get an identity related transactions
    public void updateIdentityTableAtBlockchainReceipt() throws SQLException {
        Connection connection = null;
        PreparedStatement ptmt = null;
        ResultSet resultSet = null;

        String query = "SELECT `data` FROM `Blockchain` WHERE `address` LIKE 'I%'";
        String queryForIdentity = "INSERT INTO `Identity`(`block_hash`, `role`, `name`) VALUES (?,?,?)";
        JSONObject identity = null;

        String block_hash = null;
        String role = null;
        String name = null;

        try {
            connection = ConnectionFactory.getInstance().getConnection();

            Statement st = connection.createStatement();
            resultSet = st.executeQuery(query);

            ptmt = connection.prepareStatement(queryForIdentity);

            if (resultSet.next()){
                block_hash = resultSet.getString("block_hash");
                role = resultSet.getString("role");
                name = resultSet.getString("name");

                ptmt.setString(1, block_hash);
                ptmt.setString(1, role);
                ptmt.setString(1, name);
                ptmt.executeQuery();
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
        }
    }

    public int getBLockchainSize() throws SQLException {
        Connection connection = null;
        PreparedStatement ptmt = null;
        ResultSet resultSet = null;
        int blockchainSize = 0;

        String queryString = "SELECT COUNT(id) AS size FROM `Blockchain` WHERE validity = '1'";

        try {
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            resultSet = ptmt.executeQuery();

            if (resultSet.next()){
                blockchainSize = resultSet.getInt("size");
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
            return blockchainSize;
        }
    }

    public JSONObject getVehicleInfoByEvent(String vehicleId, String event) throws SQLException {
        Connection connection = null;
        PreparedStatement ptmt = null;
        ResultSet resultSet = null;
        JSONObject vehicleInfo = new JSONObject();

        String queryString = "SELECT `previous_hash`, `block_hash`, `block_timestamp`, " +
                "`block_number`, `transaction_id`, `sender`, `event`, `data`, `address`, `rating` " +
                "FROM `Blockchain` WHERE `transaction_id` LIKE ? AND `address` = ? AND " +
                "`event` = ? AND `validity` = 1 ORDER BY `block_number` DESC LIMIT 1";

        try {
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, "V%");
            ptmt.setString(2, vehicleId);
            ptmt.setString(3, event);
            resultSet = ptmt.executeQuery();

            if (resultSet.next()){
                vehicleInfo.put("sender", resultSet.getString("sender"));
                vehicleInfo.put("event", resultSet.getString("event"));
                vehicleInfo.put("data", resultSet.getString("data"));

                System.out.println(vehicleInfo);
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
            return vehicleInfo;
        }
    }


    public long getRecentBlockNumber() throws SQLException {
        String queryString = "SELECT `block_number` FROM Blockchain ORDER BY id DESC LIMIT 1";
        Connection connection = null;
        PreparedStatement ptmt = null;
        ResultSet result = null;
        long blockNumber = 0;

        try {
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            result = ptmt.executeQuery();
            if(result.next()) {
                blockNumber = result.getLong("block_number");
            }else {
                blockNumber = 0;
            }

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
            return blockNumber;
        }
    }

    public JSONObject getPreviousBlockData() throws SQLException {
        String queryString = "SELECT `block_hash`,`block_number`, `block_timestamp` FROM Blockchain WHERE `validity` = 1 ORDER BY id DESC LIMIT 1";
        Connection connection = null;
        PreparedStatement ptmt = null;
        ResultSet result = null;
        JSONObject previousBlock = new JSONObject();

        try {
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            result = ptmt.executeQuery();
            if(result.next()) {
                previousBlock.put("blockHash", result.getString("block_hash"));
                previousBlock.put("blockNumber", result.getString("block_number"));
                previousBlock.put("blockTimeStamp", result.getString("block_timestamp"));
            }

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
            return previousBlock;
        }
    }

    public String getPreviousHash() throws SQLException {
        String queryString = "SELECT `block_hash` FROM Blockchain WHERE `validity` = 1 ORDER BY id DESC LIMIT 1";
        Connection connection = null;
        PreparedStatement ptmt = null;
        ResultSet result = null;
        String previousHash = null;

        try {
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            result = ptmt.executeQuery();
            if(result.next()) {
                previousHash = result.getString("block_hash");
            }else {
                previousHash = null;
            }

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
            return previousHash;
        }
    }

    public JSONObject checkPossibilityToAddBlock(String previousHash) throws SQLException {
        String queryString = "SELECT `block_hash`, `block_timestamp` FROM `Blockchain` WHERE `previous_hash`= ?";
        Connection connection = null;
        PreparedStatement ptmt = null;
        ResultSet result = null;
        JSONObject previousBlock = new JSONObject();

        try {
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, previousHash);
            result = ptmt.executeQuery();

            if(result.next()) {
                previousBlock.put("blockHash", result.getString("block_hash"));
                previousBlock.put("blockTimeStamp", result.getString("block_timestamp"));
            }

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
            return previousBlock;
        }
    }

    public void setValidity(boolean validity, String blockhash) throws SQLException {
        String queryString = "UPDATE `Blockchain` SET `validity` = ? WHERE  `block_hash` = ?";
        PreparedStatement ptmt = null;
        Connection connection = null;
        try {
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setBoolean(1, validity);
            ptmt.setString(2, blockhash);
            ptmt.executeUpdate();

            log.info("Blockchain Table Updated For: {}", blockhash);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (ptmt != null)
                ptmt.close();
            if (connection != null)
                connection.close();
        }
    }

    public void deleteblockchain() throws SQLException {
        String queryString = "DELETE FROM `Blockchain` WHERE  NOT `block_hash` = ?";
        PreparedStatement ptmt = null;
        Connection connection = null;
        try {
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, "115b7f26ca16e04ec8f93a26a71f9a5bf9608f3aba5add4b6182c47670c3f737");
            ptmt.executeUpdate();

            log.info("Blockchain Table Reset");

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (ptmt != null)
                ptmt.close();
            if (connection != null)
                connection.close();
        }
    }

    public void deleteVehicles() throws SQLException {
        String queryString = "DELETE FROM `vehicle`";
        PreparedStatement ptmt = null;
        Connection connection = null;
        try {
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.executeUpdate();

            log.info("Vehicles Deleted");

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (ptmt != null)
                ptmt.close();
            if (connection != null)
                connection.close();
        }
    }

    public void deleteHistory() throws SQLException {
        String queryString = "DELETE FROM `vehicle`";
        PreparedStatement ptmt = null;
        Connection connection = null;
        try {
            connection = ConnectionFactory.getInstance().getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.executeUpdate();

            log.info("History Deleted");

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (ptmt != null)
                ptmt.close();
            if (connection != null)
                connection.close();
        }
    }

}
