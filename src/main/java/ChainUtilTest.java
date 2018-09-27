import chainUtil.ChainUtil;
import chainUtil.KeyGenerator;

import java.nio.charset.StandardCharsets;
import java.security.*;

public class ChainUtilTest {
    public static void main(String[] args) {

        //getHashTest
        String testData = "message";
        try {
            System.out.println("expected output");
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            System.out.println(ChainUtil.bytesToHex(digest.digest(testData.getBytes(StandardCharsets.UTF_8))));

            System.out.println("method output");
            System.out.println(ChainUtil.bytesToHex(ChainUtil.getHashByteArray(testData)));

        } catch (Exception e) {
            e.getMessage();
        }

        try {
            PublicKey publicKey = KeyGenerator.getInstance().getPublicKey();
            String publicKeyString = KeyGenerator.getInstance().getEncodedPublicKeyString(publicKey);
            System.out.println("*********signature*********");
            byte[] signature = ChainUtil.sign(KeyGenerator.getInstance().getPrivateKey(),"ashan");
            String signatureString = ChainUtil.bytesToHex(signature);
            System.out.println("*********verification*********");
            byte[] convertedSignature = ChainUtil.hexStringToByteArray(signatureString);
            PublicKey publicKeyConverted = KeyGenerator.getInstance().getPublicKey(publicKeyString);
            System.out.println(ChainUtil.verify(publicKeyConverted,convertedSignature,"asha2n"));

            System.out.println("byte to hex testing");
            byte[] signatureRaw = ChainUtil.sign(KeyGenerator.getInstance().getPrivateKey(),"agreed");
            System.out.println(ChainUtil.bytesToHex(signatureRaw));
            byte[] signatureRaw2 = ChainUtil.sign(KeyGenerator.getInstance().getPrivateKey(),"agreed");
            byte[] signatureRaw3 = ChainUtil.sign(KeyGenerator.getInstance().getPrivateKey(),"agreed");

            System.out.println(ChainUtil.bytesToHex(signatureRaw2));
            System.out.println(ChainUtil.bytesToHex(signatureRaw3));

            String s1 = ChainUtil.bytesToHex(signatureRaw);
            String s2 = ChainUtil.bytesToHex(signatureRaw2);
            String s3 = ChainUtil.bytesToHex(signatureRaw3);

            System.out.println("VERIFICATIONS USING CONVERTED SIGNATURES");
            boolean b1 = ChainUtil.verify(publicKeyConverted,ChainUtil.hexStringToByteArray(s1),"agreed");
            boolean b2 = ChainUtil.verify(publicKeyConverted,ChainUtil.hexStringToByteArray(s2),"agreed");
            boolean b3 = ChainUtil.verify(publicKeyConverted,ChainUtil.hexStringToByteArray(s3),"agreed");

            System.out.println(b1);
            System.out.println(b2);
            System.out.println(b3);


            System.out.println("GET HASH TESTING");
            String msg = "secrectmessage";
            String h1 = ChainUtil.bytesToHex(ChainUtil.getHashByteArray(msg));
            String h2 = ChainUtil.bytesToHex(ChainUtil.getHashByteArray(msg));
            String h3 = ChainUtil.bytesToHex(ChainUtil.getHashByteArray(msg));

            System.out.println(h1);
            System.out.println(h2);
            System.out.println(h3);


            System.out.println("*************SIGNATURE VERIFICATION**************");
            String data = "agreed";
            String pkString = KeyGenerator.getInstance().getPublicKeyAsString();
            for(int i= 0; i<5 ; i++) {
                System.out.println(ChainUtil.getInstance().digitalSignature(data));
            }
            String Digitalsignature = ChainUtil.getInstance().digitalSignature(data);
            System.out.println(ChainUtil.getInstance().signatureVerification(pkString,Digitalsignature,"agreed"));


            System.out.println("publickeyAsStringTest");
            System.out.println(KeyGenerator.getInstance().getPublicKeyAsString());
            System.out.println(KeyGenerator.getInstance().getPublicKeyAsString());
            System.out.println(KeyGenerator.getInstance().getPublicKeyAsString());
            System.out.println(KeyGenerator.getInstance().getPublicKeyAsString().length());

            System.out.println("public key test");
            System.out.println(KeyGenerator.getInstance().getPublicKeyAsString());

        } catch (Exception e) {
            e.getMessage();
        }
    }
}
