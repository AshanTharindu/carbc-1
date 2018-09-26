package config;

import network.Neighbour;

import java.util.ArrayList;
import java.util.List;

public class NodeConfig {
    private final String peerID;
    private int ListenerPort;
    private List<Neighbour> neighbours;
    private Neighbour registry;


    public NodeConfig(String peerID) {
        this.peerID = peerID;
        this.neighbours = new ArrayList<>();
    }

    public String getPeerID() {
        return peerID;
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
