package org.example;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class Transaction {
    private String input;
    private String output;
    private double amount;
    private long txTimestamp;
    private String txHash;
    private String signature;

    public Transaction(String input, String output, double amount, PrivateKey privateKey) throws Exception {
        this.input = input;
        this.output = output;
        this.amount = amount;
        this.txTimestamp = System.currentTimeMillis();
        this.txHash = calculateHash();
        this.signature = signTransaction(privateKey);
    }

    private String calculateHash() throws NoSuchAlgorithmException {
        String data = input + output + amount + txTimestamp;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    private String signTransaction(PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initSign(privateKey);
        signature.update(txHash.getBytes(StandardCharsets.UTF_8));
        byte[] sign = signature.sign();
        return Base64.getEncoder().encodeToString(sign);
    }

    public boolean verifySignature(PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initVerify(publicKey);
        signature.update(txHash.getBytes(StandardCharsets.UTF_8));
        return signature.verify(Base64.getDecoder().decode(this.signature));
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "input='" + input + '\'' +
                ", output='" + output + '\'' +
                ", amount=" + amount +
                ", txTimestamp=" + txTimestamp +
                ", txHash='" + txHash + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}
