package network.Protocol;

import network.Client.RequestMessage;
import network.Node;
import org.json.JSONObject;

import java.sql.Timestamp;

public class MessageCreator {

    public static RequestMessage createMessage(JSONObject object, String Type){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String sender = Node.getInstance().getNodeConfig().getNodeID();
        String receiver = "yourPublicKey";
        String messageType = Type;

        RequestMessage requestMessage = new RequestMessage();
        requestMessage.addHeader("timestamp", timestamp.toString());
        requestMessage.addHeader("sender", sender);
        //requestMessage.addHeader("receiver", receiver);
        requestMessage.addHeader("messageType", messageType);
        requestMessage.addTheData(object.toString());
        return requestMessage;
    }
}
