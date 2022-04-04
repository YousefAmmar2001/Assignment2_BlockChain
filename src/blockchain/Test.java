package blockchain;

import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class Test {

    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException, IOException, Exception {
        BlockChain blockchain = new BlockChain();
        blockchain.add_new_transaction("Basecoin->Yousef->10");
        blockchain.add_new_transaction("Yousef->Ali->10");
        blockchain.add_new_transaction("Yousef->Ahmed->10");
        blockchain.mine();
        blockchain.add_new_transaction("Basecoin->Yousef->20");
        blockchain.add_new_transaction("Yousef->Ali->10");
        blockchain.mine();
     
        System.out.println(blockchain.isValid());
        String txt = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain.chain);
        System.out.println(txt);
        
        try {
            FileWriter fileWriter = new FileWriter("Block.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            for (Block b : blockchain.chain) {
                fileWriter.write(
                     b.hash+","
                    +b.index+","
                    +b.header.merkle_root+","
                    +b.header.previous_hash+","
                    +b.header.version+","
                    +b.header.difficulty+","
                    +b.header.getNonce()+","
                    +b.header.timestamp+","
                    +b.transactions_count+"\n");
            }
            fileWriter.close();
            System.out.println("The file has been written successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }

    }

}
