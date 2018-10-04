import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ControllerTest {
    public static void main(String[] args) {
        String type = "V";
//        for(int i= 0;i < 20; i++) {
//            Random random = new Random();
//            int peerID = 10000 + Math.abs(random.nextInt(90000));
//            String TransactionID = type+"-"+peerID;
//            System.out.println(TransactionID);
//
////            Timestamp blockTimestamp = new Timestamp(System.currentTimeMillis());
////            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss"); //revert this change
////            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
////            Date parsedDate = dateFormat.parse(timeStamp);
////            Timestamp time = new java.sql.Timestamp(parsedDate.getTime())
//        }

        java.util.Date date = new Timestamp(System.currentTimeMillis());
        Timestamp timestamp = new Timestamp(date.getTime());
        System.out.println(timestamp);
    }
}
