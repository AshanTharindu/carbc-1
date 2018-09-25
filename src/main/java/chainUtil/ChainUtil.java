package chainUtil;

import core.blockchain.Block;
import core.blockchain.Blockchain;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.LinkedList;

public class ChainUtil {

    private static ChainUtil chainUtil;

    //change to private after changes
    public ChainUtil() {}

    public static ChainUtil getInstance() {
        if (chainUtil == null) {
            chainUtil = new ChainUtil();
        }
        return chainUtil;
    }

    public String digitalSignature(String data) throws NoSuchProviderException, NoSuchAlgorithmException, IOException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
        dsa.initSign(KeyGenerator.getInstance().getPrivateKey());
        byte[] byteArray = data.getBytes();
        dsa.update(byteArray);
        return bytesToHex(dsa.sign());
    }

    public boolean signatureVerification(String publicKey, String signature, String data) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, IOException, SignatureException, InvalidKeyException {
        return verify(KeyGenerator.getInstance().getPublicKey(publicKey),hexStringToByteArray(signature),data);
    }

    public static byte[] sign(PrivateKey privateKey,String data) throws InvalidKeyException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException {
        //sign the data
        Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
        dsa.initSign(privateKey);
        byte[] byteArray = data.getBytes();
        dsa.update(byteArray);
        return dsa.sign();
    }

    public static boolean verify(PublicKey publicKey, byte[] signature, String data) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
        sig.initVerify(publicKey);
        sig.update(data.getBytes(),0,data.getBytes().length);
        return sig.verify(signature);
    }

//    public publicKeyEncryption() {
//
//    }

    public static byte[] getHashByteArray(String data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(data.getBytes(StandardCharsets.UTF_8));
    }

    public static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public String getHash(String data) throws NoSuchAlgorithmException {
        return bytesToHex(getHashByteArray(data));
    }

    public String getBlockHash(Block block) throws NoSuchAlgorithmException {
        JSONObject jsonBlock = new JSONObject(block);
        return getHash((jsonBlock.toString()));
    }

    public String getBlockChainHash(LinkedList<Block> blockchain) throws NoSuchAlgorithmException {
        String blockChainString = "";
        for(Block block: blockchain) {
            blockChainString+=new JSONObject(block).toString();
        }
        return getHash(blockChainString);
    }

    public String getBlockchainAsJsonString(LinkedList<Block> blockchain) {
        JSONObject jsonBlockchain = new JSONObject();
        for(int i = 0; i < blockchain.size(); i++) {
            jsonBlockchain.put(String.valueOf(i), new JSONObject(blockchain.get(i)).toString());
        }

        return jsonBlockchain.toString();
    }

}