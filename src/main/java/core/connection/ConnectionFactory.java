package core.connection;

import Exceptions.FileUtilityException;
import config.DatabaseConfig;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    String driverClassName = "com.mysql.jdbc.Driver";
    String connectionUrl = "jdbc:mysql://localhost:3306/CarBC?verifyServerCertificate=false&useSSL=true";
    String dbUser = "admin";
    String dbPwd = "admin123";

    private static ConnectionFactory connectionFactory = null;

    private ConnectionFactory() {
        try {
//            DatabaseConfig databaseConfig = DatabaseConfig.getInstance();
//            databaseConfig.setConfigUsingResource();
//            JSONObject dbDetail = databaseConfig.setConfigUsingResource();
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        catch (FileUtilityException e) {
//            e.printStackTrace();
//        }
    }

    public Connection getConnection() throws SQLException {
        Connection conn = null;
        conn = DriverManager.getConnection(connectionUrl, dbUser, dbPwd);
        return conn;
    }

    public static ConnectionFactory getInstance() {
        if (connectionFactory == null) {
            connectionFactory = new ConnectionFactory();
        }
        return connectionFactory;
    }
}
