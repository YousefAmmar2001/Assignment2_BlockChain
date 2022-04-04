package blockchain;

public class Header {
    
    String version, previous_hash, merkle_root;
    long timestamp;
    int nonce;
    int difficulty;

    public Header(String version, String previous_hash, String merkle_root, Long timestamp, int difficulty) {
        this.version = version;
        this.previous_hash = previous_hash;
        this.merkle_root = merkle_root;
        this.timestamp = timestamp;
        this.difficulty = difficulty;
        this.nonce = 0;
    }

    @Override
    public String toString() {
        return "Header{" + "version=" + version + ", previous_hash=" + previous_hash + ", merkle_root=" + merkle_root + ", timestamp=" + timestamp + ", nonce=" + nonce + ", difficulty=" + difficulty + '}';
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

}
