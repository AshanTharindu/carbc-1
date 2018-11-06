package fakeAgreementSender;

import chainUtil.ChainUtil;
import chainUtil.KeyGenerator;
import core.blockchain.Block;
import core.consensus.Agreement;
import core.consensus.Consensus;
import network.Client.RequestMessage;
import network.Node;
import network.Protocol.MessageCreator;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class AgreementSender {


    public boolean generateKeyPair(String orgName) {
        KeyPairGenerator keyGen = null;
        try {
            keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(512, random);
            KeyPair pair = keyGen.generateKeyPair();
            PublicKey publicKey = pair.getPublic();
            PrivateKey privateKey = pair.getPrivate();

            // Store Public Key.
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
            FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir") + "/src/main/resources/" + orgName+ "Public.key");
            fos.write(x509EncodedKeySpec.getEncoded());
            fos.close();

            // Store Private Key.
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
                    privateKey.getEncoded());
            //change path to relative path
            fos = new FileOutputStream(System.getProperty("user.dir") + "/src/main/resources/" + orgName + "Private.key");
            fos.write(pkcs8EncodedKeySpec.getEncoded());
            fos.close();
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

    public String digitalSignature(String data, String orgName) {
        Signature dsa = null;
        String signature = null;
        try {
            dsa = Signature.getInstance("SHA1withDSA", "SUN");
            dsa.initSign(getPrivateKey(orgName));
            byte[] byteArray = data.getBytes();
            dsa.update(byteArray);
            signature = ChainUtil.bytesToHex(dsa.sign());
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

    public PrivateKey getPrivateKey(String orgName) {
        if (getResourcesFilePath(orgName + "Private.key") == null) {
            generateKeyPair(orgName);
        }
        return loadPrivateKey(orgName);
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


    private PrivateKey loadPrivateKey(String orgName) {
        // Read Private Key.
        File filePrivateKey = new File(getResourcesFilePath(orgName + "Private.key"));
        FileInputStream fis = null;
        KeyFactory keyFactory = null;
        PKCS8EncodedKeySpec privateKeySpec = null;
        PrivateKey privateKey = null;

        try {
            fis = new FileInputStream(getResourcesFilePath(orgName + "Private.key"));
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

    public String getPublicKeyAsString(String orgName) {
        return getEncodedPublicKeyString(getPublicKey(orgName));
    }

    public PublicKey getPublicKey(String orgName) {
        if (getResourcesFilePath(orgName + "Public.key") == null) {
            generateKeyPair(orgName);
        }
        return loadPublicKey(orgName);
    }

    private PublicKey loadPublicKey(String orgName) {
        // Read Public Key.
        File filePublicKey = new File(getResourcesFilePath(orgName + "Public.key"));
        FileInputStream fis = null;
        KeyFactory keyFactory = null;
        X509EncodedKeySpec publicKeySpec = null;
        PublicKey publicKey = null;
        try {
            fis = new FileInputStream(getResourcesFilePath(orgName + "Public.key"));
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

    public void createFakeAgreement(String blockHash, String orgName) {
        AgreementSender agreementSender = new AgreementSender();
        String signedBlock = digitalSignature(blockHash, orgName);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("signedBlock", signedBlock);
        jsonObject.put("blockHash", blockHash);
        jsonObject.put("publicKey", agreementSender.getPublicKeyAsString(orgName));
        RequestMessage agreementMessage = MessageCreator.createMessage(jsonObject, "Agreement");
        agreementMessage.addHeader("keepActive", "false");
        Node.getInstance().broadcast(agreementMessage);
        Agreement agreement = new Agreement(signedBlock, signedBlock, blockHash,
                agreementSender.getPublicKeyAsString(orgName));
        Consensus.getInstance().handleAgreement(agreement);
    }

    public void sendFakeAgreements(String orgName) {
        for(Block block : Consensus.getInstance().getNonApprovedBlocks()) {
            createFakeAgreement(block.getBlockHeader().getHash(), orgName);
        }
    }





}
