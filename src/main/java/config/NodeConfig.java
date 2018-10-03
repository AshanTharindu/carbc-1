package config;

import network.Neighbour;

import java.util.ArrayList;
import java.util.List;

public class NodeConfig {
    private final String nodeID;
    private int ListenerPort;
    private List<Neighbour> neighbours;
    private Neighbour registry;


    public NodeConfig(String nodeID) {
        this.nodeID = nodeID;
        this.neighbours = new ArrayList<>();
    }

    public String getNodeID() {
        return nodeID;
    }

    public final void setListenerPort(int ListenerPort) {
        this.ListenerPort = ListenerPort;
    }

    public int getListenerPort() {
        return ListenerPort;
    }

    public void addNeighbour(Neighbour neighbour) {
        this.neighbours.add(neighbour);
    }

    public List<Neighbour> getNeighbours() {
        return neighbours;
    }

    public Neighbour getNeighbourByPublicKey(String publicKey) {
        Neighbour neighbour = null;

        for(Neighbour peer: neighbours) {
            if(publicKey.equals(peer.getPublicKey())) {
                neighbour = peer;
            }
        }
        return neighbour;
    }
}
