package org.example;
import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private List<Block> chain;

    public Blockchain() {
        this.chain = new ArrayList<>();
    }

    public void addBlock(Block newBlock) {
        this.chain.add(newBlock);
    }

    public Block getLastBlock() {
        return this.chain.isEmpty() ? null : this.chain.get(this.chain.size() - 1);
    }

    public Block getBlock(int index) {
        if (index < 0 || index >= this.chain.size()) {
            return null;
        }
        return this.chain.get(index);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Block block : chain) {
            sb.append(block.toString()).append("\n");
        }
        return sb.toString();
    }
}
