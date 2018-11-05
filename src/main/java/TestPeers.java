import Exceptions.FileUtilityException;
import config.CommonConfigHolder;
import constants.Constants;
import core.connection.NeighbourDAO;
import network.Neighbour;
import network.Node;
import org.json.JSONObject;
import org.slf4j.impl.SimpleLogger;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

public class TestPeers {
    public static void main(String[] args) throws FileUtilityException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, IOException {
        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");

        //Set the main directory as home
        System.setProperty(Constants.CARBC_HOME, System.getProperty("user.dir"));

        //at the very beginning
        //A Config common to all: network, blockchain, etc.
        CommonConfigHolder commonConfigHolder = CommonConfigHolder.getInstance();
        commonConfigHolder.setConfigUsingResource("peer1");

        //when initializing the network
        Node node = Node.getInstance();
        node.init();

        System.out.println("****************");
        getAllPeers();
    }

    public static void getPeer() {
        try {
            NeighbourDAO neighbourDAO = new NeighbourDAO();
            Neighbour neighbour = neighbourDAO.getPeer("16de46ff1f9e7c273a63754fe56cc753eb6bbab");
            System.out.println(new JSONObject(neighbour));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getAllPeers() {
        try {
            NeighbourDAO neighbourDAO = new NeighbourDAO();
            ArrayList<Neighbour> neighbours = neighbourDAO.getPeers();
            for (Neighbour neighbour : neighbours) {
                System.out.println(new JSONObject(neighbour));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
