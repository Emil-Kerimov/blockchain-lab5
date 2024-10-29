package org.example;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.util.List;

public class Block {
    private int version;
    private String prevHash;
    private long timestamp;
    private int difficultyTarget;
    private int nonce;
    private String merkleRoot;
    private List<Transaction> transactions;
    private String blockHash;
    private String signature;

    public Block(int version, String prevHash, List<Transaction> transactions, int difficultyTarget, PrivateKey privateKey) throws Exception {
        this.version = version;
        this.prevHash = prevHash;
        this.timestamp = System.currentTimeMillis();
        this.transactions = transactions;
        this.difficultyTarget = difficultyTarget;
        this.nonce = 0;
        this.merkleRoot = calculateMerkleRoot();
        this.blockHash = calculateBlockHash();
        this.signature = signBlock(privateKey);
    }

    private String calculateMerkleRoot() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        StringBuilder sb = new StringBuilder();
        for (Transaction tx : transactions) {
            sb.append(tx.toString());
        }
        byte[] hash = digest.digest(sb.toString().getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    public String calculateBlockHash() throws NoSuchAlgorithmException {
        String data = version + prevHash + timestamp + difficultyTarget + nonce + merkleRoot;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    private String signBlock(PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initSign(privateKey);
        signature.update(blockHash.getBytes(StandardCharsets.UTF_8));
        byte[] sign = signature.sign();
        return Base64.getEncoder().encodeToString(sign);
    }

    public boolean verifyBlock(PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initVerify(publicKey);
        signature.update(blockHash.getBytes(StandardCharsets.UTF_8));
        return signature.verify(Base64.getDecoder().decode(this.signature));
    }

    @Override
    public String toString() {
        return "Block{" +
                "version=" + version +
                ", prevHash='" + prevHash + '\'' +
                ", timestamp=" + timestamp +
                ", difficultyTarget=" + difficultyTarget +
                ", nonce=" + nonce +
                ", merkleRoot='" + merkleRoot + '\'' +
                ", blockHash='" + blockHash + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public int getNonce() {
        return nonce;
    }
}
