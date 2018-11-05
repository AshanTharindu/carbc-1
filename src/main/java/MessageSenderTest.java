import chainUtil.ChainUtil;
import chainUtil.KeyGenerator;
import core.blockchain.*;
import network.Node;
import network.communicationHandler.MessageSender;
import org.json.JSONObject;

import java.security.*;
import java.sql.Timestamp;
import java.util.ArrayList;

public class MessageSenderTest {

    public static void main(String[] args) {

        Node node = Node.getInstance();
        node.startNode("abc1234", 48666);
        MessageSender.sendTestMsg("192.168.8.105", 49876);
    }
}
