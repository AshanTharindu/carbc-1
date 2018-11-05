import controller.Controller;
import network.Node;
import org.json.JSONObject;

public class ControllerTest2 {
    public static void main(String[] args) {
        Node.getInstance().startNode("5678", 42222);


        JSONObject jsonObject =  new JSONObject();
        jsonObject.put("key", "value");
        Controller controller = new Controller();
        controller.sendTransaction("RegisterVehicle", null, jsonObject);
    }
}
