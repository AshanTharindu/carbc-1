import chainUtil.ChainUtil;
import chainUtil.KeyGenerator;
import controller.Controller;
import core.blockchain.Block;
import core.blockchain.BlockBody;
import core.blockchain.BlockHeader;
import core.blockchain.Transaction;
import core.consensus.AgreementCollector;
import core.consensus.Rating;
import network.Node;
import org.json.JSONArray;
import org.json.JSONObject;

public class RatingTest {
    public static void main(String[] args) {

        //ExchangeOwnership
        Rating rating = new Rating("ExchangeOwnership");
        rating.setSpecialValidators(0);
        rating.setMandatory(2);
        rating.setAgreementCount(15);
        double ratingValue = rating.calRating(0,0);
        System.out.println("ExchangeOwnership rating: "+ ratingValue);

        //ServiceRepair
        Rating ratings = new Rating("ServiceRepair");
        ratings.setSpecialValidators(5);
        ratings.setMandatory(1);
        ratings.setAgreementCount(26);
        double ratingValues = ratings.calRating(0,0);
        System.out.println("ServiceRepair rating: " + ratingValues);

        Rating ratingI = new Rating("Insure");
        ratingI.setSpecialValidators(0);
        ratingI.setMandatory(1);
        ratingI.setAgreementCount(17);
        double ratingValueI = ratingI.calRating(0,0);
        System.out.println("Insure rating: "+ ratingValueI);
    }
}
