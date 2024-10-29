package org.example;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        int[] nodeCounts = { 10, 20, 30, 40, 50};
        List<Double> times = new ArrayList<>();

        for (int nodeCount : nodeCounts) {
            double time = runBFTProtocol(nodeCount);
            times.add(time);
            System.out.println("Час виконання для " + nodeCount + " вузлів: " + time + " мс");
        }

        createChart(nodeCounts, times);
    }

    private static double runBFTProtocol(int nodeCount) throws Exception {
        Blockchain blockchain = new Blockchain();
        List<BFTNode> nodes = new ArrayList<>();

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        keyGen.initialize(256);

        for (int i = 0; i < nodeCount; i++) {
            KeyPair keyPair = keyGen.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            BFTNode node = new BFTNode(blockchain, nodes, publicKey);
            nodes.add(node);
        }

        KeyPair keyPair = keyGen.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();

        Transaction tx1 = new Transaction("inp", "out", 20, privateKey);
        Transaction tx2 = new Transaction("out", "output", 10, privateKey);
        List<Transaction> transactions = List.of(tx1, tx2);

        long startTime = System.currentTimeMillis();
        nodes.get(0).mineAndSendBlock(4, transactions, privateKey);
        long endTime = System.currentTimeMillis();

        return endTime - startTime;
    }

    private static void createChart(int[] nodeCounts, List<Double> times) {
        XYSeries series = new XYSeries("Час виконання протоколу BFT");

        for (int i = 0; i < nodeCounts.length; i++) {
            series.add(nodeCounts[i], times.get(i));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Залежність часу виконання протоколу BFT від кількості учасників",
                "Кількість учасників (вузлів)",
                "Час виконання (мс)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        JFrame frame = new JFrame("Дослідження ефективності протоколу BFT");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);
    }
}
