package core.blockchain;

import chainUtil.ChainUtil;
import org.json.JSONObject;

public class Block {
    private BlockHeader blockHeader;
    private BlockBody blockBody;

    public Block(){}

    public Block(BlockHeader blockHeader, BlockBody blockBody){
        this.blockHeader = blockHeader;
        this.blockBody = blockBody;
    }

    public Block(BlockHeader genesisHeader){
        this.blockHeader =genesisHeader;
    }

    public BlockHeader getBlockHeader() {
        return blockHeader;
    }

    public void setBlockHeader(BlockHeader blockHeader) {
        this.blockHeader = blockHeader;
    }

    public BlockBody getBlockBody() {
        return blockBody;
    }

    public void setBlockBody(BlockBody blockBody) {
        this.blockBody = blockBody;
    }

    public void broadcast(){}

    public String getBlockHash(){
        return ChainUtil.getInstance().getBlockHash(blockBody);
    }

    public static JSONObject getBlockRepresentation(Block block){
        JSONObject jsonBlock = new JSONObject();

        String previous_hash = block.getBlockHeader().getPreviousHash();
        String block_hash = block.getBlockHeader().getHash();
        String block_timestamp = block.getBlockHeader().getBlockTime();
        long block_number = block.getBlockHeader().getBlockNumber();

        String transaction_id = block.getBlockBody().getTransaction().getTransactionId();
        String sender = block.getBlockBody().getTransaction().getSender();
        String event = block.getBlockBody().getTransaction().getEvent();
        String data = block.getBlockBody().getTransaction().getData();
        String address = block.getBlockBody().getTransaction().getAddress();

        jsonBlock.put("PreviousHash", previous_hash);
        jsonBlock.put("blockHash", block_hash);
        jsonBlock.put("blockTimestamp", block_timestamp);
        jsonBlock.put("blockNumber", block_number);
        jsonBlock.put("transactionId", transaction_id);
        jsonBlock.put("sender", sender);
        jsonBlock.put("event", event);
        jsonBlock.put("data", data);
        jsonBlock.put("address", address);

        return jsonBlock;
    }
}
