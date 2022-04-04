package blockchain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;

public class BlockChain {

    ArrayList<String> unconfirmed_transactions;
    ArrayList<Block> chain;

    public BlockChain() throws NoSuchAlgorithmException, UnsupportedEncodingException, FileNotFoundException, IOException, Exception {
        this.unconfirmed_transactions = new ArrayList<String>();
        this.chain = new ArrayList<Block>();
        File myFile = new File("Block.txt");
        String[] arrayOfString = null;
        if(myFile.createNewFile()){
            System.out.println("The file has been written successfully.");
        }else{
            System.out.print("The file is exist - ");
            Scanner s = new Scanner(myFile);
            while (s.hasNextLine()) {                
                String text = s.nextLine();
                arrayOfString = text.split(",");
                Header header = new Header(arrayOfString[4], arrayOfString[3], arrayOfString[2],
                        Long.valueOf(arrayOfString[7]), Integer.valueOf(arrayOfString[5]));
                Integer nonce = Integer.valueOf(arrayOfString[6]);
                header.setNonce(nonce);
                Block block = new Block(Integer.valueOf(arrayOfString[1]) , header, Integer.valueOf(arrayOfString[8]));
                chain.add(block);
            }
            if (!isValid()) {
                throw new Exception("The block in file invalid delete the file");
            }
        }
    }

    public Block create_genesis_block() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        this.add_new_transaction("Basecoin->Yousef->30");
        Timestamp timestamp = Timestamp.from(Instant.now());
        Header new_header = new Header("1", "0", "0", timestamp.getTime(), 2);
        return new Block(0, new_header, this.unconfirmed_transactions.size());
    }

    public Block getLastBlock() {
        return this.chain.get(this.chain.size() - 1);
    }

    public boolean add_block(Block block, String proof) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String previous_hash;
        if (this.chain.isEmpty()) {
            previous_hash = "0";
        } else {
            previous_hash = this.getLastBlock().hash;
        }
        if (previous_hash != (block.header.previous_hash)) {
            return false;
        }
        if(!this.is_valid_proof(block, proof)){
            return false;
        }
        block.hash = proof;
        this.chain.add(block);
        return true;
    }

    public boolean is_valid_proof(Block block, String block_hash) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return (block_hash.startsWith(count_of_zeros(block.header.difficulty)) && (block_hash == (block.hash)));
    }

    public String count_of_zeros(int diff) {
        String zeros = "";
        for (int i = 0; i < diff; i++) {
            zeros = zeros + "0";
        }
        return zeros;
    }

    public String proof_of_work(Block block) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        block.header.nonce = 0;
        String computed_hash = block.compute_hash();
        while (!computed_hash.startsWith(count_of_zeros(block.header.difficulty))) {
            block.header.nonce += 1;
            computed_hash = block.compute_hash();
        }
        return computed_hash;
    }

    public void add_new_transaction(String transactions) {
        this.unconfirmed_transactions.add(transactions);
    }

    public boolean mine() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        Block new_block;
        if (this.unconfirmed_transactions.isEmpty()) {
            return false;
        }
        Timestamp timestamp = Timestamp.from(Instant.now());
        if (this.chain.isEmpty()) {
            Header new_header = new Header("1", "0", "0", timestamp.getTime(), 2);
            new_block = new Block(0, new_header, this.unconfirmed_transactions.size());
        } else {
            Block last_block = this.getLastBlock();
            Header new_header = new Header("1", last_block.hash, "0", timestamp.getTime(), 2);
            new_block = new Block(last_block.index + 1, new_header, this.unconfirmed_transactions.size());
        }
        String proof = this.proof_of_work(new_block);
        new_block.hash = proof;
        this.add_block(new_block, proof);

        this.unconfirmed_transactions.clear();
        return true;
    }

    @Override
    public String toString() {
        return "BlockChain{" + "unconfirmed_transactions=" + unconfirmed_transactions + ", chain=" + chain + '}';
    }
    
    public boolean isValid() throws NoSuchAlgorithmException, UnsupportedEncodingException{
        if (chain.isEmpty()) {
            return true;
        }
        if (!chain.get(0).hash.equals(chain.get(0).compute_hash())) {
            return false;
        }
        for (int i = 1; i < chain.size(); i++) {
            if (!chain.get(i).hash.equals(chain.get(i).compute_hash())) {
                return false;
            }
            if (!chain.get(i).header.previous_hash.equals(chain.get(i-1).hash)) {
                return false;
            }
        }
        return true;
    }
    
}
