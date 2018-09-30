package core.consensus;

import core.blockchain.Block;

import java.util.LinkedList;

public class BlockchainShare {

    private String id;
    private String ip;
    private int listeningPort;
    private LinkedList<Block> blockChainInstance;

    public BlockchainShare(String ip, int listeningPort) {
//        this.ip = ip;
//        this.listeningPort = listeningPort;
//        id = ip + String.valueOf(listeningPort);
//        blockChainInstance = Blockchain.getInstance().getBlockchainArray();
    }

    public String getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public int getListeningPort() {
        return listeningPort;
    }

    public LinkedList<Block> getBlockChainInstance() {
        return blockChainInstance;
    }
}
