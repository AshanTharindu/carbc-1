import controller.Controller;
import core.serviceStation.webSocketServer.BroadcastServer;
import network.communicationHandler.MessageSender;

public class notificationTest {
    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.startNode();
        MessageSender.getInstance().requestIP();

        /*
         * starting UI web socket communication
         */
        BroadcastServer broadcastServer = new BroadcastServer();
        broadcastServer.start();


    }
}
