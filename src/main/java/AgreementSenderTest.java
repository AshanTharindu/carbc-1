import chainUtil.ChainUtil;
import config.CommonConfigHolder;
import constants.Constants;
import fakeAgreementSender.AgreementSender;
import org.slf4j.impl.SimpleLogger;

public class AgreementSenderTest {

    public static void main(String[] args) {
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


        generateOrgsKeyPairs();
//        serviceStationAgreement();
        //serviceStationAgreement();
    }

    public static void serviceStationAgreement() {
        AgreementSender agreementSender = new AgreementSender();
        String blockhash = "blockHash";
        String serviceStatinAgreement = agreementSender.digitalSignature(blockhash, "ServiceStation");
        System.out.println(serviceStatinAgreement);
        boolean verification = ChainUtil.signatureVerification(agreementSender.getPublicKeyAsString("ServiceStation"), serviceStatinAgreement, blockhash);
        System.out.println(verification);
        System.out.println(agreementSender.getPublicKeyAsString("ServiceStation"));
    }

    public static void generateOrgsKeyPairs() {
        AgreementSender agreementSender = new AgreementSender();
//        agreementSender.generateKeyPair("ServiceStation");
//        agreementSender.generateKeyPair("RMV");
//        agreementSender.generateKeyPair("SparePartShop");
//        agreementSender.generateKeyPair("GodFather");

        System.out.println("service Station: "+agreementSender.getPublicKeyAsString("ServiceStation"));
        System.out.println("rmv :" + agreementSender.getPublicKeyAsString("RMV"));
//        System.out.println("spare parts: " +agreementSender.getPublicKeyAsString("SparePartShop"));
//        System.out.println("GodFather: " +agreementSender.getPublicKeyAsString("GodFather"));
    }


}
