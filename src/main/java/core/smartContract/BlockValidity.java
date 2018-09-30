package core.smartContract;

import config.EventConfigHolder;
import core.connection.BlockJDBCDAO;
import core.blockchain.Block;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.Iterator;

public class BlockValidity {
    private Block block;

    public BlockValidity(Block block){
        this.block = block;
    }

    public boolean isSecondaryPartyValid() throws SQLException {

        String event = block.getBlockBody().getTransaction().getEvent();
        JSONObject data = block.getBlockBody().getTransaction().getData();
        JSONObject eventConfig = EventConfigHolder.getInstance().getEventJson();

        BlockJDBCDAO blockJDBCDAO = new BlockJDBCDAO();

        JSONObject params = eventConfig.getJSONObject(event).getJSONObject("params");

        JSONObject secondaryParties = params.getJSONObject("SecondaryParty");

        Iterator<String> keys = secondaryParties.keys();
        while ( keys.hasNext() ){
            String key = (String)keys.next(); // First key in your json object

            if (secondaryParties.get(key) instanceof JSONObject) {
                JSONObject jsonObject = secondaryParties.getJSONObject(key);
                String secondaryPartyAddress = jsonObject.getString("address");
                String secondaryPartyRole = jsonObject.getString("role");

                JSONObject identity = blockJDBCDAO.getIdentityByAddress(secondaryPartyAddress);

                //if want, can check the name also

                if (!identity.getString("role").equals(secondaryPartyRole)){
                    return false;
                }
            }

        }

        ////old version
//        for (int i = 0; i < secondaryParties.length(); i++){
//            JSONObject jsonObject = secondaryParties.getJSONObject(i);
//            String secondaryPartyAddress = jsonObject.getString("address");
//            String secondaryPartyRole = jsonObject.getString("role");
//
//            JSONObject identity = blockJDBCDAO.getIdentityByAddress(secondaryPartyAddress);
//
//            //if want, can check the name also
//
//            if (!identity.getString("role").equals(secondaryPartyRole)){
//                return false;
//            }
//
//        }
        return true;
    }

    public boolean checkSecondaryParty(String secondaryPartyRole, String secondatyPartyAddress){


        return true;
    }
}
