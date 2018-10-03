import Exceptions.FileUtilityException;
import chainUtil.KeyGenerator;
import config.CommonConfigHolder;
import constants.Constants;
import core.blockchain.*;
import network.communicationHandler.MessageSender;
import core.consensus.ConsensusOld;
import network.Node;
import org.json.JSONObject;
import org.slf4j.impl.SimpleLogger;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AgreementRequestTest2 {
    public static void main(String[] args) throws FileUtilityException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, IOException {
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
        commonConfigHolder.setConfigUsingResource("peer2");

        /*
         * when initializing the network
         * */
        Node node = Node.getInstance();
        node.init();

        /*
         * when we want our node to start listening
         * */
        node.startListening();

        try {
//            String timeStampStr = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
//            Validator validator1 = new Validator("val1pubkey","owner","true",3);
//            ArrayList<Validation> validations = new ArrayList<>();
//            validations.add(new Validation(validator1,"3332"));
//            BlockHeader blockHeader = new BlockHeader("101","1234",timeStampStr,
//                    "senderPubkey",123,true);
//            blockHeader.setHash("##blockHash##");
//            Validator validator3 = new Validator(KeyGenerator.getInstance().getEncodedPublicKeyString(KeyGenerator.getInstance().getPublicKey()),"owner","true",3);
//            Validator validator4 = new Validator("v2","seller","true",4);
//            Validator validator5 = new Validator("3081f13081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca4034400024100d1f13f9b315e6fa41e1920ae2d875f28f7129ab4f8e29eb12783d238430585c225e7d05f1e84c2218abb65a9dc5bc7b8df03012dffc5dececb18f76a64440335","seller","true",4);
//            ArrayList<Validator> validators = new ArrayList<>();
//            validators.add(validator3);
//            //validators.add(validator2);
//            Calendar calendar = Calendar.getInstance();
//            java.util.Date now = calendar.getTime();
//
//            //java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
//            Object[] parameters = new Object[2];
//            parameters[0] = "para1";
//            parameters[1] = "5";
//
//            TransactionInfo transactionInfo = new TransactionInfo();
//            transactionInfo.setEvent("ownershipTransfer");
//            transactionInfo.setSmartContractSignature("qq");
//            transactionInfo.setSmartContractMethod("changeOwnership");
//            transactionInfo.setData("sa");
//            transactionInfo.setParameters(parameters);
//            transactionInfo.setSmartContractName("VehicleContract");
//            transactionInfo.setVehicleId("sa");
//
//            TransactionProposal proposal = new TransactionProposal(KeyGenerator.getInstance().getEncodedPublicKeyString(KeyGenerator.getInstance().getPublicKey()),validators,
//                    "data","proposal1",timeStampStr,transactionInfo);
//
//            Transaction transaction = new Transaction("senderpubkey",validations,
//                    "tran1",transactionInfo);
//
//            Block block = new Block(blockHeader,transaction);
//            JSONObject jsonObject = new JSONObject(block);
//            String myJson = jsonObject.toString();
//            System.out.println(myJson);
//
//            System.out.println("block");
//            System.out.println(new JSONObject(block).toString());
//            ConsensusOld.getInstance().addToAgreementCollectors(block);
//            MessageSender.getInstance().requestAgreement(block,1);


        } catch (Exception e) {
            e.getMessage();
        }
    }
}
