package core.smartContract.AgreementHandler;

import core.blockchain.Block;
import core.connection.BlockJDBCDAO;
import org.json.JSONObject;

import java.sql.SQLException;

public class OwnershipExchangeAgreementHandler extends Thread{

    String[] mandatoryValidators = {"NewOwner", "RMV"};


    public OwnershipExchangeAgreementHandler(){
//        this.mandatoryValidators = new JSONObject();
//        mandatoryValidators.put("NewOwner",)
    }

    @Override
    public void run() {

    }

    public JSONObject setMandotaryAgreements(Block block) throws SQLException {
        BlockJDBCDAO blockJDBCDAO = new BlockJDBCDAO();
        JSONObject validator = new JSONObject();
        JSONObject blockData = block.getBlockBody().getTransaction().getData();

        JSONObject secondaryParties = blockData.getJSONObject("secondaryParty");

        validator.put("NewOwner", secondaryParties.getString("NewOwner"));

        validator.put("RMV", blockJDBCDAO.getIdentityByRole("RMV"));

        return secondaryParties;
    }
}
