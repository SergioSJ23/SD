package gui;

import javax.swing.*;
import java.awt.*;

public class VotingStationGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(VotingStationGUI::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Voting Station");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        
        // Entrada da Voting Station
        JPanel entrancePanel = createPanel("Entrance", Color.LIGHT_GRAY, 5);
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        mainPanel.add(entrancePanel, gbc);
        
        // Voting Station (Maior)
        JPanel votingStationPanel = createVotingStationPanel("Voting Station", Color.WHITE, 8);
        gbc.gridx = 1;
        gbc.weightx = 0.4;
        mainPanel.add(votingStationPanel, gbc);
        
        // Saída da Voting Station
        JPanel exitPanel = createPanel("Exit", Color.LIGHT_GRAY, 3);
        gbc.gridx = 2;
        gbc.weightx = 0.3;
        mainPanel.add(exitPanel, gbc);
        
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static JPanel createPanel(String title, Color color, int maxVoters) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setLayout(new GridLayout(maxVoters, 1, 5, 5));
        
        for (int i = 0; i < maxVoters; i++) {
            JLabel voterLabel = new JLabel("Voter " + (i + 1), SwingConstants.CENTER);
            voterLabel.setOpaque(true);
            voterLabel.setBackground(Color.LIGHT_GRAY);
            voterLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panel.add(voterLabel);
        }
        
        return panel;
    }

    private static JPanel createVotingStationPanel(String title, Color color, int maxVoters) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createTitledBorder(title));
        
        JPanel votersPanel = new JPanel(new GridLayout(maxVoters, 1, 5, 5));
        for (int i = 0; i < maxVoters; i++) {
            JLabel voterLabel = new JLabel("Voter " + (i + 1), SwingConstants.CENTER);
            voterLabel.setOpaque(true);
            voterLabel.setBackground(Color.WHITE);
            voterLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            votersPanel.add(voterLabel);
        }
        
        JPanel votingBoothPanel = new JPanel();
        votingBoothPanel.setBorder(BorderFactory.createTitledBorder("Voting Booth"));
        JLabel boothLabel = new JLabel("Voter Voting", SwingConstants.CENTER);
        boothLabel.setOpaque(true);
        boothLabel.setBackground(Color.WHITE);
        boothLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        votingBoothPanel.add(boothLabel);
        
        panel.add(votersPanel, BorderLayout.CENTER);
        panel.add(votingBoothPanel, BorderLayout.SOUTH);
        
        return panel;
    }
}
