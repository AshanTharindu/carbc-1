package core.consensus;

import core.blockchain.Block;
import core.blockchain.Transaction;

//remove this class
public class Agreement {

    private String blockHash;
    private String digitalSignature;
    private String publicKey;

    public Agreement(String blockHash, String digitalSignature, String publicKey) {
        this.blockHash = blockHash;
        this.digitalSignature = digitalSignature;
        this.publicKey = publicKey;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public String getDigitalSignature() {
        return digitalSignature;
    }

    public String getPublicKey() {
        return publicKey;
    }


}
