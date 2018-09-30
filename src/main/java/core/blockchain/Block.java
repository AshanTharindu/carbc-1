package core.blockchain;

import chainUtil.ChainUtil;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Block {
    private BlockHeader blockHeader;
    private BlockBody blockBody;

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

    public static Block createGenesis() throws ParseException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        BlockHeader blockHeader = new BlockHeader("genesis Block", timestamp, (long)1);
        Block genesisBlock = new Block(blockHeader);
        return genesisBlock;
    }

    public void broadcast(){}

    public String getBlockHash(){
        return ChainUtil.getInstance().getBlockHash(blockBody);
    }
}
