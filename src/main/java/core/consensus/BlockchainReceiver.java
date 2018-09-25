package core.consensus;

public class BlockchainReceiver {

    private String id;
    private String ip;
    private int listeningPort;
    private String signedBlockchain;
    private String blockchainHash;

    public BlockchainReceiver(String publicKey, String ip,int listeningPort,String signedBlockchain,String blockchainHash) {
        this.id = publicKey;
        this.ip = ip;
        this.listeningPort =  listeningPort;
        this.signedBlockchain = signedBlockchain;
        this.blockchainHash = blockchainHash;
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

    public String getSignedBlockchain() {
        return signedBlockchain;
    }

    public String getBlockchainHash() {
        return blockchainHash;
    }

}
