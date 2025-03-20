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
        JPanel votingStationPanel = createPanel("Voting Station", Color.WHITE, 8);
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
            voterLabel.setBackground(Color.CYAN);
            voterLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panel.add(voterLabel);
        }
        
        return panel;
    }
}
