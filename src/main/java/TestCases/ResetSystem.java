package TestCases;

import config.CommonConfigHolder;
import constants.Constants;
import core.connection.BlockJDBCDAO;
import org.slf4j.impl.SimpleLogger;

import java.sql.SQLException;

public class ResetSystem {
    public static void main(String[] args) throws SQLException {
        setPaths();
        resetBlockchainAndVehicle();

    }

    public static void setPaths() {
        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");

        /*
         * Set the main directory as home
         * */
        System.setProperty(Constants.CARBC_HOME, System.getProperty("user.dir"));

        /*
         * At the very beginning
         * A Config common to all: network, blockchain, etc.
         * */
        CommonConfigHolder commonConfigHolder = CommonConfigHolder.getInstance();
    }

    public static void resetBlockchainAndVehicle() throws SQLException {
        BlockJDBCDAO blockJDBCDAO = new BlockJDBCDAO();
        blockJDBCDAO.deleteblockchain();
        blockJDBCDAO.deleteVehicles();
    }
}
