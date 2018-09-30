package chainUtil;

import core.blockchain.Block;
import core.blockchain.BlockBody;
import core.connection.BlockJDBCDAO;
import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public String digitalSignature(String data) {
        Signature dsa = null;
        String signature = null;
        try {
            dsa = Signature.getInstance("SHA1withDSA", "SUN");
            dsa.initSign(KeyGenerator.getInstance().getPrivateKey());
            byte[] byteArray = data.getBytes();
            dsa.update(byteArray);
            signature = bytesToHex(dsa.sign());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return signature;
    }

    public boolean signatureVerification(String publicKey, String signature, String data) {
        return verify(KeyGenerator.getInstance().getPublicKey(publicKey),hexStringToByteArray(signature),data);
    }

    public static byte[] sign(PrivateKey privateKey,String data) throws SignatureException {
        //sign the data
        Signature dsa = null;
        try {
            dsa = Signature.getInstance("SHA1withDSA", "SUN");
            dsa.initSign(privateKey);
            byte[] byteArray = data.getBytes();
            dsa.update(byteArray);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return dsa.sign();
    }

    public static boolean verify(PublicKey publicKey, byte[] signature, String data) {
        Signature sig = null;
        boolean verification = false;
        try {
            sig = Signature.getInstance("SHA1withDSA", "SUN");
            sig.initVerify(publicKey);
            sig.update(data.getBytes(),0,data.getBytes().length);
            verification = sig.verify(signature);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return verification;
    }

//    public publicKeyEncryption() {
//
//    }

    public static byte[] getHashByteArray(String data) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
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

    public String getHash(String data) {
        return bytesToHex(getHashByteArray(data));
    }

    public String getBlockHash(Block block) {
        JSONObject jsonBlock = new JSONObject(block);
        return getHash((jsonBlock.toString()));
    }

    public String getBlockHash(BlockBody blockBody) {
        JSONObject jsonBlock = new JSONObject(blockBody);
        return getHash((jsonBlock.toString()));
    }

    public String getBlockChainHash(LinkedList<Block> blockchain) {
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

    public JSONObject getBlockchain(int from) throws Exception {
        BlockJDBCDAO blockJDBCDAO = new BlockJDBCDAO();
        ResultSet rs = blockJDBCDAO.getBlockchain(from);
        return convertResultSetIntoJSON(rs);

    }

    public JSONObject convertResultSetIntoJSON(ResultSet resultSet) throws Exception {
        JSONObject result = new JSONObject();
        int count = 0;

        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            count++;
            int total_rows = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();

            for (int i = 0; i < total_rows; i++) {
                String columnName = resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase();
                Object columnValue = resultSet.getObject(i + 1);
                // if value in DB is null, then we set it to default value
                if (columnValue == null){
                    columnValue = "null";
                }
                /*
                Next if block is a hack. In case when in db we have values like price and price1 there's a bug in jdbc -
                both this names are getting stored as price in ResulSet. Therefore when we store second column value,
                we overwrite original value of price. To avoid that, i simply add 1 to be consistent with DB.
                 */
                if (obj.has(columnName)){
                    columnName += "1";
                }
                obj.put(columnName, columnValue);
            }
            jsonArray.put(obj);
        }
        result.put("blockchainSize", count);
        result.put("blockchain", jsonArray.toString());
        return result;
    }

    public boolean verifyUser(String peerID, String publicKey) {
        if(peerID.equals(publicKey.substring(0,40))) {
            return true;
        }
        return false;
    }

}