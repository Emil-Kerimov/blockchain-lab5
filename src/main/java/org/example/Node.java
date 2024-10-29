package org.example;
import java.security.PrivateKey;
import java.util.List;

public class Node {
    protected Blockchain blockchain;

    public Node(Blockchain blockchain) {
        this.blockchain = blockchain;
    }

    public Block mineBlock(int difficultyTarget, List<Transaction> transactions, PrivateKey privateKey) throws Exception {
        String prevHash = (blockchain.getLastBlock() != null) ? blockchain.getLastBlock().calculateBlockHash() : "0";
        Block block = new Block(1, prevHash, transactions, difficultyTarget, privateKey);

        // Пошук Nonce
        String target = new String(new char[difficultyTarget]).replace('\0', '0');
        while (!block.calculateBlockHash().substring(0, difficultyTarget).equals(target)) {
            block.setNonce(block.getNonce() + 1);
        }

        blockchain.addBlock(block);
        return block;
    }
}