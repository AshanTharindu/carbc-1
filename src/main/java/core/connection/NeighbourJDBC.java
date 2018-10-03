package core.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NeighbourJDBC {

    Connection connection;
    PreparedStatement ptmt;
    private final Logger log = LoggerFactory.getLogger(NeighbourJDBC.class);

    public void saveNeighbours(String nodeID, String ip, int port, String publicKey) {
        String queryString = "INSERT INTO `PeerDetails`(`node_id`,`ip`,`port`) VALUES(?,?,?)";
        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();
            PreparedStatement ptmt = connection.prepareStatement(queryString);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void saveNeighbours(String nodeID, String ip, int port) {

    }
}
