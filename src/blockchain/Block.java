package blockchain;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;

public class Block {

    Header header;
    int index, transactions_count;
    String hash;

    public Block(int index, Header header, int transactions_count) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        this.index = index;
        this.header = header;
        this.transactions_count = transactions_count;
        this.hash = this.compute_hash();
    }

    public String compute_hash() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String block_string = header.toString();
        return get_SHA_256(block_string);
    }

    public static String get_SHA_256(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes("UTF-8"));
        return DatatypeConverter.printHexBinary(hash);
    }

    @Override
    public String toString() {
        return "Block{" + "header=" + header + ", index=" + index + ", transactions_count=" + transactions_count + ", hash=" + hash + '}';
    }
    
}
