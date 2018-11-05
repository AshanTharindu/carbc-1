package config;

import Exceptions.FileUtilityException;
import constants.Constants;
import org.json.JSONObject;
import utils.FileUtils;


public class DatabaseConfig {
    private static final DatabaseConfig INSTANCE = new DatabaseConfig();
    private JSONObject dbConfig;

    public static DatabaseConfig getInstance() {
        return INSTANCE;
    }

    private DatabaseConfig(){}

    public JSONObject getDBJson() {
        return dbConfig;
    }

    public void setConfigUsingResource() throws FileUtilityException {
//        String resourcePath = System.getProperty(Constants.CARBC_HOME)
//                + "/src/main/resources/db.json";

//        DatabaseConfig databaseConfig = DatabaseConfig.getInstance();
//        databaseConfig.setConfigUsingResource();
//        JSONObject dbDetail = databaseConfig.getDBJson();
//        String resourcePath = dbDetail.getString("path");

//        String resourcePath = "/home/nicer/CSE/fyp/carbc-1_extended/src/main/resources/db.json";
        String resourcePath = "/home/sajinie/Documents/fyp_stuff/CARbc/car-bc-extended/src/main/resources/db.json";
        this.dbConfig = new JSONObject(FileUtils.readFileContentAsText(resourcePath));
    }

}
