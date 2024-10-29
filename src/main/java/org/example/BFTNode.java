package org.example;
import java.security.PublicKey;
import java.util.List;
import java.security.PrivateKey;


public class BFTNode extends Node {
    private List<BFTNode> networkNodes;
    private int validCount = 0;
    private PublicKey publicKey;

    public BFTNode(Blockchain blockchain, List<BFTNode> networkNodes, PublicKey publicKey) {
        super(blockchain);
        this.networkNodes = networkNodes;
        this.publicKey = publicKey;
    }

    // Імітація розсилки блоку для консенсусу
    public void sendBlockForConsensus(Block block) {
        for (BFTNode node : networkNodes) {
            if (node != this) {
                node.receiveBlock(block);
            }
        }
    }

    public void receiveBlock(Block block) {
        try {
            Thread.sleep(100); // Затримка передачі
            if (block.verifyBlock(getPublicKey())) {
                sendValid(block);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendValid(Block block) {
        for (BFTNode node : networkNodes) {
            if (node != this) {
                node.receiveValid();
            }
        }
    }

    public synchronized void receiveValid() {
        validCount++;
        if (validCount >= (2 * networkNodes.size() / 3)) {
            addBlockToBlockchain();
        }
    }

    private void addBlockToBlockchain() {
        this.blockchain.addBlock(blockchain.getLastBlock()); //
    }

    public void mineAndSendBlock(int difficultyTarget, List<Transaction> transactions, PrivateKey privateKey) throws Exception {
        Block newBlock = mineBlock(difficultyTarget, transactions, privateKey);
        sendBlockForConsensus(newBlock);
    }
    public PublicKey getPublicKey() {
        return publicKey;
    }
}
