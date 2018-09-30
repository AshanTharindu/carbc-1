package network.communicationHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.consensus.Consensus;
import core.blockchain.Block;
import network.Node;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.text.ParseException;

public class Handler extends Thread{
    String messageType;
    String data;
    String peerID;

    public Handler(String messageType, String data, String peerID){
        this.messageType = messageType;
        this.data = data;
        this.peerID = peerID;
    }

    @Override
    public void run() {
        try{
            switch (messageType) {
                //request of the new protocol
                case "BlockBroadcast":
                    handleBroadcastBlock();
                    break;

                case "IPResponse":
                    handleIPResponse();
                    break;

                case "Hello":
                    handleHelloRequest();
                    break;

                case "HelloResponse":
                    handleHelloResponse();

                case "BlockChainHashRequest":
                    System.out.println("BlockChainHashRequest");
                    handleBlockChainHashRequest();

                case "BlockChainSign":
                    System.out.println("BlockChainSign");
                    System.out.println();
                    handleBlockChainSignRequest();

                case "BlockChainRequest":
                    System.out.println("BlockChainRequest");
                    handleBlockChainRequest();

                case "BlockchainSend":
                    System.out.println("BlockchainSend");
                    handleReceivedBlockchainRequest();

                case "Agreement":
                    System.out.println("Agreement");
                    handleReceivedAgreement();

                default:
                    System.out.println("default");

            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            } catch (NoSuchProviderException e) {
            e.printStackTrace();
            } catch (IOException e) {
            e.printStackTrace();
            } catch (ParseException e) {
            e.printStackTrace();
            } catch (SQLException e) {
            e.printStackTrace();
            } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }


    public void handleIPResponse() throws IOException {
        Node.getInstance().joinNetwork(data);
    }

    public void handleHelloRequest() {
        JSONObject clientInfo = new JSONObject(data);
        String ip = clientInfo.getString("ip");
        int listeningPort = clientInfo.getInt("ListeningPort");
        MessageSender.getInstance().sendHelloResponse(Node.getInstance().getNodeConfig().getListenerPort(),ip, listeningPort);
        Node.getInstance().addActiveNeighbour(ip, listeningPort);
    }

    public void handleHelloResponse() {
        JSONObject clientInfo = new JSONObject(data);
        String ip = clientInfo.getString("ip");
        int listeningPort = clientInfo.getInt("ListeningPort");
        Node.getInstance().addActiveNeighbour(ip, listeningPort);
    }

    public void handleBlockChainHashRequest() throws NoSuchAlgorithmException, IOException, SignatureException, NoSuchProviderException, InvalidKeyException, InvalidKeySpecException {
        JSONObject clientInfo = new JSONObject(data);
        String ip = clientInfo.getString("ip");
        int listeningPort = clientInfo.getInt("ListeningPort");
        Consensus.getInstance().handleBlockchainHashRequest(ip,listeningPort);
    }

    public void handleBlockChainSignRequest() throws NoSuchAlgorithmException, IOException, SignatureException, NoSuchProviderException, InvalidKeyException, InvalidKeySpecException {
        JSONObject jsonObject = new JSONObject(data);
        String ip = jsonObject.getString("ip");
        int listeningPort = jsonObject.getInt("ListeningPort");
        String signedBlockchain = jsonObject.getString("signedBlockchain");
        String blockchainHash = jsonObject.getString("blockchainHash");
        String publicKey = jsonObject.getString("publicKey"); //get from header
        Consensus.getInstance().handleReceivedSignedBlockchain(publicKey,ip,listeningPort,signedBlockchain,blockchainHash);
    }

    public void handleBlockChainRequest() {
        JSONObject jsonObject = new JSONObject(data);
        String ip = jsonObject.getString("ip");
        int listeningPort = jsonObject.getInt("ListeningPort");
        Consensus.getInstance().sendBlockchain(ip,listeningPort);
    }

    public void handleReceivedBlockchainRequest() throws ParseException, NoSuchAlgorithmException, IOException, SQLException, NoSuchProviderException, InvalidKeySpecException {
        JSONObject jsonObject = new JSONObject(data);
        String ip = jsonObject.getString("ip");
        int listeningPort = jsonObject.getInt("ListeningPort");
        int blockchainLength = jsonObject.getInt("blockchainLength");
        JSONObject jsonBlockchain = new JSONObject(jsonObject.getString("blockchain"));
        Consensus.getInstance().addReceivedBlockchain(peerID,jsonBlockchain,blockchainLength);
    }

    public void handleReceivedAgreement(){
        JSONObject jsonObject = new JSONObject(data);
        String signature = jsonObject.getString("digitalSignature");
        String signedBlock = jsonObject.getString("signedBlock");
        String blockHash = jsonObject.getString("blockHash");
        String publicKey = jsonObject.getString("publicKey");
        Consensus.getInstance().handleReceivedAgreement(signature, signedBlock, blockHash, publicKey);
    }

    // 0-> block comes
    public void handleBroadcastBlock() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, IOException, ParseException, SQLException {
        System.out.println("handleBroadcastBlock");
        JSONObject receivedJSONObject = new JSONObject(data);
        String JSONBlock = (String) receivedJSONObject.get("block");
        Block decodedBLock = JSONStringToBlock(JSONBlock);

        Consensus.getInstance().handleNonApprovedBlock(decodedBLock);
    }

    public Block JSONStringToBlock(String JSONblock) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, IOException {
//        byte[] prevhash = ChainUtil.hexStringToByteArray("1234");
//        byte[] hash = ChainUtil.hexStringToByteArray("5678");
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        byte[] data = ChainUtil.hexStringToByteArray("1456");
//        byte[] signatue1 = ChainUtil.hexStringToByteArray("3332");
//        byte[] signatue2 = ChainUtil.hexStringToByteArray("3442");
//        PublicKey publicKey = KeyGenerator.getInstance().getPublicKey();
//        Validator validator1 = new Validator("val1pubkey","owner",true,3);
//        Validator validator2 = new Validator("val2pubkey","seller",true,4);
//        ArrayList<Validation> validations = new ArrayList<>();
//        validations.add(new Validation(validator1,"3332"));
//        validations.add(new Validation(validator2,"3442"));
//        BlockHeader blockHeader = new BlockHeader("101","1234",timestamp,
//                "senderPubkey",123,true);
//        Transaction transaction = new Transaction("senderpubkey",validations,
//                "tran1",new TransactionInfo());

//        Block block = new Block(blockHeader,transaction);
        JSONObject jsonObject = new JSONObject(JSONblock);
        Gson gson = new GsonBuilder().serializeNulls().create();
        Block block = gson.fromJson(JSONblock,Block.class);

        return block;
    }

}
