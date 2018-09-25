import Exceptions.FileUtilityException;
import config.CommonConfigHolder;
import constants.Constants;
import network.Node;
import network.communicationHandler.MessageSender;
import org.slf4j.impl.SimpleLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;


public class Peer3Test {
    public static void main(String[] args) throws FileUtilityException, IOException {

        InetAddress inetAddress = InetAddress.getLocalHost();
        System.out.println("IP Address:- " + inetAddress.getHostAddress());
        System.out.println("Host Name:- " + inetAddress.getHostName());

        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");

        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                        whatismyip.openStream()));

        String ip = in.readLine(); //you get the IP as a String
        System.out.println(ip);
        /*
         * Set the main directory as home
         * */
        System.setProperty(Constants.CARBC_HOME, System.getProperty("user.dir"));

        /*
         * At the very beginning
         * A Config common to all: network, blockchain, etc.
         * */
        CommonConfigHolder commonConfigHolder = CommonConfigHolder.getInstance();
        commonConfigHolder.setConfigUsingResource("peer3");
        /*
         * when initializing the network
         * */
        Node node = Node.getInstance();
        node.initTest();

        /*
         * when we want our node to start listening
         * */
        node.startListening();
        MessageSender.getInstance().requestIP(49233);

    }
}
