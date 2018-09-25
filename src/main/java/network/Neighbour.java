package network;

public class Neighbour {
    private String ip;
    private int port;
    private String publicKey;

    public Neighbour(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public Neighbour(String publicKey, String ip, int port) {
        this.publicKey = publicKey;
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }


}
