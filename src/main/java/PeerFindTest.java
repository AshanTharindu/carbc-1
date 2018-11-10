import controller.Controller;
import network.Client.RequestMessage;
import network.Node;
import network.Protocol.MessageCreator;
import org.json.JSONObject;

public class PeerFindTest {
    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.startNode();
        RequestMessage testMsg = MessageCreator.createMessage(new JSONObject(), "test");
        Node.getInstance().sendMessageToPeer("192.168.8.107", 49222, testMsg);
    }
}
