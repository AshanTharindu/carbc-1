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

    private boolean generateKeyPair() {
        KeyPairGenerator keyGen = null;
        try {
            keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
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
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    private PublicKey loadPublicKey() {
        // Read Public Key.
        File filePublicKey = new File(getResourcesFilePath("public.key"));
        FileInputStream fis = null;
        KeyFactory keyFactory = null;
        X509EncodedKeySpec publicKeySpec = null;
        PublicKey publicKey = null;
        try {
            fis = new FileInputStream(getResourcesFilePath("public.key"));
            byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
            fis.read(encodedPublicKey);
            fis.close();

            //load public key
            keyFactory = KeyFactory.getInstance("DSA");
            publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
            publicKey = keyFactory.generatePublic(publicKeySpec);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return publicKey;
    }

    private PrivateKey loadPrivateKey() {
        // Read Private Key.
        File filePrivateKey = new File(getResourcesFilePath("private.key"));
        FileInputStream fis = null;
        KeyFactory keyFactory = null;
        PKCS8EncodedKeySpec privateKeySpec = null;
        PrivateKey privateKey = null;

        try {
            fis = new FileInputStream(getResourcesFilePath("private.key"));
            byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
            fis.read(encodedPrivateKey);
            fis.close();

            //load private key
            keyFactory = KeyFactory.getInstance("DSA");
            privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
            privateKey = keyFactory.generatePrivate(privateKeySpec);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return privateKey;
    }

    public PublicKey getPublicKey() {
        if (getResourcesFilePath("public.key") == null) {
            generateKeyPair();
        }
        return loadPublicKey();
    }

    public PublicKey getPublicKey(String hexvalue) {
        byte[] encodedPublicKey = ChainUtil.hexStringToByteArray(hexvalue);
        KeyFactory keyFactory = null;
        X509EncodedKeySpec publicKeySpec = null;
        PublicKey publicKey = null;
        try {
            keyFactory = KeyFactory.getInstance("DSA");
            publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
            publicKey = keyFactory.generatePublic(publicKeySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return publicKey;
    }

    public PrivateKey getPrivateKey() {
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

    public String getPublicKeyAsString() {
        return getEncodedPublicKeyString(getPublicKey());
    }

}
