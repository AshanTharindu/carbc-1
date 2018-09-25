package chainUtil;

import java.io.*;
import java.net.URL;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyGenerator {

    private static KeyGenerator keyGenerator;

    public KeyGenerator(){};

    public static KeyGenerator getInstance(){
        if(keyGenerator == null) {
            keyGenerator = new KeyGenerator();
        }
        return keyGenerator;
    }

    private boolean generateKeyPair() throws NoSuchProviderException, NoSuchAlgorithmException, IOException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyGen.initialize(512, random);
        KeyPair pair = keyGen.generateKeyPair();
        PublicKey publicKey = pair.getPublic();
        System.out.println("pub genarated");

        PrivateKey privateKey = pair.getPrivate();
        System.out.println("prv genarated");

        // Store Public Key.
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
        FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir") + "/src/main/resources" + "/public.key");
        System.out.println("pub stored");
        fos.write(x509EncodedKeySpec.getEncoded());
        fos.close();

        // Store Private Key.
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
                privateKey.getEncoded());
        //change path to relative path
        fos = new FileOutputStream(System.getProperty("user.dir") + "/src/main/resources" + "/private.key");
        fos.write(pkcs8EncodedKeySpec.getEncoded());
        fos.close();
        System.out.println("prv stored");
        return true;
    }

    private PublicKey loadPublicKey() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        // Read Public Key.
        File filePublicKey = new File(getResourcesFilePath("public.key"));
        FileInputStream fis = new FileInputStream(getResourcesFilePath("public.key"));
        byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
        fis.read(encodedPublicKey);
        fis.close();

        //load public key
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
        return keyFactory.generatePublic(publicKeySpec);
    }

    private PrivateKey loadPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        // Read Private Key.
        File filePrivateKey = new File(getResourcesFilePath("private.key"));
        FileInputStream fis = new FileInputStream(getResourcesFilePath("private.key"));
        byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
        fis.read(encodedPrivateKey);
        fis.close();

        //load private key
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
                encodedPrivateKey);
        return keyFactory.generatePrivate(privateKeySpec);
    }

    public PublicKey getPublicKey() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, NoSuchProviderException {
        if (getResourcesFilePath("public.key") == null) {
            generateKeyPair();
        }
        return loadPublicKey();
    }

    public PublicKey getPublicKey(String hexvalue) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, NoSuchProviderException {
        byte[] encodedPublicKey = ChainUtil.hexStringToByteArray(hexvalue);
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
        return keyFactory.generatePublic(publicKeySpec);
    }

    public PrivateKey getPrivateKey() throws NoSuchAlgorithmException, NoSuchProviderException, IOException, InvalidKeySpecException {
        if (getResourcesFilePath("private.key") == null) {
            generateKeyPair();
        }
        return loadPrivateKey();
    }

    public String getResourcesFilePath(String fileName) {
        URL url = getClass().getClassLoader().getResource(fileName);
        if (url == null) {
            return null;
        } else {
            return url.getPath();
        }
    }

    public String getEncodedPublicKeyString(PublicKey publicKey) {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
        return ChainUtil.bytesToHex(x509EncodedKeySpec.getEncoded());
    }

    public String getPublicKeyAsString() throws NoSuchAlgorithmException, NoSuchProviderException, IOException, InvalidKeySpecException {
        return getEncodedPublicKeyString(getPublicKey());

    }

}
