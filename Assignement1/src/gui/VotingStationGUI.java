package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import threads.*;
import main.*;

public class VotingStationGUI implements VoterObserver {
    private static JFrame frame;
    private static JPanel entrancePanel;
    private static JPanel votingStationPanel;
    private static JPanel exitPanel;
    private static Map<Integer, JLabel> voterLabels = new HashMap<>();
    private static int numVoters = Main.getNumVoters();
    private static int capacityCap = Main.getCapacityCap(); 
    private static List<TVoter> voters = Main.getVoters();

    public static void run() {
        SwingUtilities.invokeLater(VotingStationGUI::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        frame = new JFrame("Voting Station");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;

        // Entrada da Voting Station
        entrancePanel = createPanel("Entrance", Color.WHITE, numVoters, voters);
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        mainPanel.add(entrancePanel, gbc);

        // Voting Station (Maior)
        votingStationPanel = createVotingStationPanel("Voting Station", Color.WHITE, capacityCap, voters);
        gbc.gridx = 1;
        gbc.weightx = 0.4;
        mainPanel.add(votingStationPanel, gbc);

        // Saída da Voting Station
        exitPanel = createExitPanel("Exit", Color.WHITE, 1, voters);
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

    private static JPanel createPanel(String title, Color color, int maxVoters, List<TVoter> voters) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), title));
        panel.setLayout(new GridLayout(maxVoters, 1, 5, 5));

        return panel;
    }

    private static JPanel createVotingStationPanel(String title, Color color, int maxVoters, List<TVoter> voters) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), title));

        JPanel votersPanel = new JPanel(new GridLayout(maxVoters, 1, 5, 5));
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

    private static JPanel createExitPanel(String title, Color color, int maxVoters, List<TVoter> voters) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), title));

        JPanel votersPanel = new JPanel(new GridLayout(maxVoters, 1, 5, 5));
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

    @Override
    public void updateVoterState(int voterId, String state) {
        SwingUtilities.invokeLater(() -> {
            if (voterLabels.get(voterId) == null) {
                JLabel voterLabel = new JLabel("Voter " + voterId, SwingConstants.CENTER);
                voterLabel.setOpaque(true);
                voterLabel.setBackground(Color.LIGHT_GRAY);
                voterLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                voterLabels.put(voterId, voterLabel);
            }

            JLabel voterLabel = voterLabels.get(voterId);
            if (voterLabel != null) {
                // Remove voterLabel from all panels
                entrancePanel.remove(voterLabel);
                JPanel VSvotersPanel = (JPanel) votingStationPanel.getComponent(0);
                VSvotersPanel.remove(voterLabel);
                JPanel exitvotersPanel = (JPanel) exitPanel.getComponent(0);
                exitvotersPanel.remove(voterLabel);
                JPanel votingBoothPanel = (JPanel) votingStationPanel.getComponent(2);
                votingBoothPanel.remove(voterLabel);
                JPanel pollsterPanel = (JPanel) exitPanel.getComponent(1);
                pollsterPanel.remove(voterLabel);
                JPanel clerkPanel = (JPanel) votingStationPanel.getComponent(1);
                clerkPanel.remove(voterLabel);

                // Add voterLabel to the new state panel
                switch (state) {
                    case "Entrance":
                        entrancePanel.add(voterLabel);
                        break;
                    case "Voting Station":
                        VSvotersPanel.add(voterLabel);
                        break;
                    case "Exit":
                        exitvotersPanel.add(voterLabel);
                        break;
                    case "Voting Booth":
                        votingBoothPanel.add(voterLabel);
                        break;
                    case "Pollster":
                        pollsterPanel.add(voterLabel);
                        break;
                    case "Clerk":
                        clerkPanel.add(voterLabel);
                        break;
                }
                entrancePanel.revalidate();
                entrancePanel.repaint();
                votingStationPanel.revalidate();
                votingStationPanel.repaint();
                exitPanel.revalidate();
                exitPanel.repaint();
            }
        });
    }
}
