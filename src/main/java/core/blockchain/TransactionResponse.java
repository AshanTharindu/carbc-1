package core.blockchain;

import chainUtil.ChainUtil;
import chainUtil.KeyGenerator;
import network.communicationHandler.MessageSender;

import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;

public class TransactionResponse {
    private String proposalID;
    private Validator validator;
    private String signature;


    public TransactionResponse(String proposalID, Validator validator, String signature) {
        this.setProposalID(proposalID);
        this.validator = validator;
        this.signature = signature;
    }



    public Validator getValidator() {
        return validator;
    }

    public String getSignature() {
        return signature;
    }



    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }



    public void addResponse() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException,
            SignatureException, InvalidKeySpecException, IOException {


        String proposalID = this.getProposalID();
        TransactionProposal proposal = TransactionProposal.getProposals().get(proposalID);
       // String proposalString = TransactionProposal.getProposalString(proposal);
        String signingObjectString = TransactionProposal.getSigningObjectString(proposal);
        System.out.println(ChainUtil.verify(KeyGenerator.getInstance().getPublicKey(this.getValidator().getValidator())
                ,ChainUtil.hexStringToByteArray(this.getSignature()),signingObjectString));

        System.out.println("validator public key: " + KeyGenerator.getInstance().getPublicKey(this.getValidator().getValidator()));

        System.out.println(KeyGenerator.getInstance().getPublicKey());
        System.out.println("received Signature: "+ this.getSignature());
        System.out.println("received data: "+signingObjectString );
        if (proposalID != null & ChainUtil.verify(KeyGenerator.getInstance().getPublicKey(this.getValidator().getValidator())
                ,ChainUtil.hexStringToByteArray(this.getSignature()),signingObjectString)){ //

            HashMap<String, ArrayList<TransactionResponse>> responsePool = TempResponsePool.getResponsePool();

            if (responsePool.get(proposalID) != null && !responsePool.get(proposalID).isEmpty()) {
                ArrayList<TransactionResponse> responseArray = responsePool.get(proposalID);
                System.out.println(responseArray.get(0).equals(this));
                if (!responseArray.contains(this)) {
                    if (ChainUtil.verify(KeyGenerator.getInstance().getPublicKey(this.getValidator().getValidator()),
                            ChainUtil.hexStringToByteArray(this.getSignature()),TransactionProposal.getSigningObjectString(proposal))){
                        boolean isContain = false;

                        for (TransactionResponse response1: responseArray){
                            if (response1.getValidator().equals(this)){
                                isContain = true;
                                break;
                            }
                        }

                        if (!isContain){
                            responseArray.add(this);
                            responsePool.replace(proposalID, responseArray);
                            if (responseArray.size() > proposal.getTransactionInfo().getThreshod()){
                                int mandatorySignCount = 0;
                                int mandatorySignCountInProposal = 0;

                                for (TransactionResponse response: responseArray){
                                    if(Boolean.parseBoolean(response.getValidator().isMandotory())){
                                        mandatorySignCount++;
                                    }
                                }

                                for (Validator validator:proposal.getValidators()){
                                    if (Boolean.parseBoolean(validator.isMandotory())){
                                        mandatorySignCountInProposal++;
                                    }
                                }

                                if (mandatorySignCount==mandatorySignCountInProposal){
                                    Block block = proposal.createBlock(proposal.getProposalID());
                                    Blockchain.getInstance().getBlockchainArray().add(block);
                                    MessageSender.getInstance().BroadCastBlock(block);
                                    System.out.println(block);
                                }
                            }
                        }
                    }
                }
                return;
            } else {

                if (ChainUtil.verify(KeyGenerator.getInstance().getPublicKey(this.getValidator().getValidator()),
                        ChainUtil.hexStringToByteArray(this.getSignature()),TransactionProposal.getSigningObjectString(proposal))){
                    ArrayList<TransactionResponse> responseArray = new ArrayList<TransactionResponse>();
                    responseArray.add(this);
                    responsePool.put(proposalID, responseArray);
                    TempResponsePool.setResponsePool(responsePool);

                    if (responseArray.size() > proposal.getTransactionInfo().getThreshod()){
                        int mandatorySignCount = 0;
                        int mandatorySignCountInProposal = 0;

                        for (TransactionResponse response: responseArray){
                            if(Boolean.parseBoolean(response.getValidator().isMandotory())){
                                mandatorySignCount++;
                            }
                        }

                        for (Validator validator:proposal.getValidators()){
                            if (Boolean.parseBoolean(validator.isMandotory())){
                                mandatorySignCountInProposal++;
                            }
                        }

                        if (mandatorySignCount==mandatorySignCountInProposal){
                            Block block = proposal.createBlock(proposal.getProposalID());
                            Blockchain.getInstance().getBlockchainArray().add(block);
                            MessageSender.getInstance().BroadCastBlock(block);
                            System.out.println(block);
                        }
                    }
                }
                System.out.println(responsePool);
                return;
            }
        }
        else {
            System.out.println("TransactionResponse -> add response -> invalid");
            return;
        }
    }

    public String getProposalID() {
        return proposalID;
    }

    public void setProposalID(String proposalID) {
        this.proposalID = proposalID;
    }

//    @Override
//    public String toString(){
//        return "'pID:'" + this.pID +"'Validator:'" + this.validator + "'Signatute'" + ChainUtil.bytesToHex(this.signature);
//    }

}
