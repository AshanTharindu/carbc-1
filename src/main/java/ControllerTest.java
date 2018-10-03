import java.util.Random;

public class ControllerTest {
    public static void main(String[] args) {
        String type = "V";
        for(int i= 0;i < 20; i++) {
            Random random = new Random();
            int peerID = 10000 + Math.abs(random.nextInt(90000));
            String TransactionID = type+"-"+peerID;
            System.out.println(TransactionID);
        }
    }
}
