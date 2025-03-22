package gui;

import station.MStation;
import votesbooth.IVotesBooth_all;
import votesbooth.MVotesBooth;
import main.*;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class VotingStationGUI {
    private static JPanel entrancePanel, votingStationPanel, exitPanel;
    private static MStation station;
    private static MVotesBooth votesBooth = MVotesBooth.getInstance();

    public static void main(String[] args) {
        int capacitycap = Integer.parseInt(args[0]); // Obter capacitycap do argumento
        station = MStation.getInstance(capacitycap); // Passar para MStation
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
        entrancePanel = createPanel("Entrance", Color.LIGHT_GRAY, 8);
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        mainPanel.add(entrancePanel, gbc);
        
        // Voting Station (Maior)
        votingStationPanel = createVotingStationPanel("Voting Station", Color.WHITE, 4);
        gbc.gridx = 1;
        gbc.weightx = 0.4;
        mainPanel.add(votingStationPanel, gbc);
        
        // Saída da Voting Station
        exitPanel = createExitPanel("Exit", Color.LIGHT_GRAY, 1);
        gbc.gridx = 2;
        gbc.weightx = 0.3;
        mainPanel.add(exitPanel, gbc);
        
        // Botão para encerrar a simulação
        JButton endSimulationButton = new JButton("End Simulation");
        endSimulationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(endSimulationButton, gbc);
        
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static JPanel createPanel(String title, Color color, int maxVoters) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), title));

        panel.setLayout(new GridLayout(maxVoters, 1, 5, 5));
        
        for (int i = 0; i < maxVoters; i++) {
            JLabel voterLabel = new JLabel("Voter " + (i + 1), SwingConstants.CENTER);
            voterLabel.setOpaque(true);
            voterLabel.setBackground(color);
            voterLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panel.add(voterLabel);
        }
        
        return panel;
    }
    
    private static JPanel createVotingStationPanel(String title, Color color, int maxVoters) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), title));
        
        JPanel votersPanel = new JPanel(new GridLayout(maxVoters, 1, 5, 5));
        for (int i = 0; i < maxVoters; i++) {
            JLabel voterLabel = new JLabel("Voter " + (i + 1), SwingConstants.CENTER);
            voterLabel.setOpaque(true);
            voterLabel.setBackground(color);
            voterLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            votersPanel.add(voterLabel);
        }
        
        JPanel pollClerkPanel = new JPanel();
        pollClerkPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), "Poll Clerk"));
        JLabel clerkLabel = new JLabel("Validating Voter x", SwingConstants.CENTER);
        clerkLabel.setOpaque(true);
        clerkLabel.setBackground(color);
        clerkLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pollClerkPanel.add(clerkLabel);
        
        JPanel votingBoothPanel = new JPanel();
        votingBoothPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), "Voting Booth"));
        JLabel boothLabel = new JLabel("Voter x Voting", SwingConstants.CENTER);
        boothLabel.setOpaque(true);
        boothLabel.setBackground(color);
        boothLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        votingBoothPanel.add(boothLabel);
        
        panel.add(votersPanel, BorderLayout.CENTER);
        panel.add(pollClerkPanel, BorderLayout.NORTH);
        panel.add(votingBoothPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private static JPanel createExitPanel(String title, Color color, int maxVoters) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), title));
        
        JPanel votersPanel = new JPanel(new GridLayout(maxVoters, 1, 5, 5));
        for (int i = 0; i < maxVoters; i++) {
            JLabel voterLabel = new JLabel("Voter " + (i + 1), SwingConstants.CENTER);
            voterLabel.setOpaque(true);
            voterLabel.setBackground(color);
            voterLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            votersPanel.add(voterLabel);
        }
        
        JPanel pollsterPanel = new JPanel();
        pollsterPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), "Pollster"));
        pollsterPanel.setBackground(color);
        JLabel pollsterLabel = new JLabel("Surveying Voter x", SwingConstants.CENTER);
        pollsterLabel.setOpaque(true);
        pollsterLabel.setBackground(color);
        pollsterLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pollsterPanel.add(pollsterLabel);
        
        panel.add(votersPanel, BorderLayout.CENTER);
        panel.add(pollsterPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private static void updateGUI() {
        SwingUtilities.invokeLater(() -> {
            updatePanel(entrancePanel, station.getEntranceQueue());
            updatePanel(votingStationPanel, station.getVotingQueue());
            updatePanel(exitPanel, station.getExitQueue());
        });
    }
    
    private static void updatePanel(JPanel panel, List<Integer> voters) {
        panel.removeAll();
        for (int id : voters) {
            JLabel voterLabel = new JLabel("Voter " + id, SwingConstants.CENTER);
            voterLabel.setOpaque(true);
            voterLabel.setBackground(Color.WHITE);
            voterLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panel.add(voterLabel);
        }
        panel.revalidate();
        panel.repaint();
    }
    
    private static void startUpdating() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(VotingStationGUI::updateGUI, 0, 1, TimeUnit.SECONDS);
    }
}
