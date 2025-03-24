package gui;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import main.*;
import repository.MRepository;
import threads.TVoter;

public class VotingStationGUI implements VoterObserver {
    private static JFrame frame;
    private static Map<Integer, JLabel> voterLabels = new HashMap<>();
    private static JPanel votesPanel;
    private static JLabel votesALabel;
    private static JLabel votesBLabel;
    private static JLabel pollsterVotesALabel;
    private static JLabel pollsterVotesBLabel;

    private static int numVoters = Main.getNumVoters();
    private static int capacityCap = Main.getCapacityCap();
    private static List<TVoter> voters = Main.getVoters();

    private static JPanel entrancePanel;
    private static JPanel stationPanel;
    private static JPanel exitPollPanel;

    private static JPanel pollClerkPanel;
    private static JPanel validatedPanel;
    private static JPanel rejectedPanel;
    private static JPanel votedPanel;

    private static JPanel exitPollingAreaPanel;
    private static JPanel pollsterPanel;
    private static JPanel pollsterVotesPanel;

    private static int pollsterVotesA = 0;
    private static int pollsterVotesB = 0;

    public static void run() {
        SwingUtilities.invokeLater(VotingStationGUI::createAndShowGUI);
    }

    private static void updateVoteCount() {
        votesALabel.setText("A: " + MRepository.getInstance().getVotesA());
        votesBLabel.setText("B: " + MRepository.getInstance().getVotesB());
    }
    

    private static void createAndShowGUI() {
        frame = new JFrame("Voting Station");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);

        JPanel mainPanel = new JPanel(new GridLayout(1, 4));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        votesPanel = new JPanel(new GridLayout(2, 1));
        votesPanel.setBorder(BorderFactory.createTitledBorder("Votes"));
        votesALabel = new JLabel("A: 0", SwingConstants.CENTER);
        votesBLabel = new JLabel("B: 0", SwingConstants.CENTER);
        votesPanel.add(votesALabel);
        votesPanel.add(votesBLabel);
        mainPanel.add(votesPanel);

        entrancePanel = createPanel("Entrance", Color.WHITE);
        mainPanel.add(entrancePanel);

        stationPanel = new JPanel(new BorderLayout());
        stationPanel.setBorder(BorderFactory.createTitledBorder("Station"));
        stationPanel.setBackground(Color.WHITE);

        pollClerkPanel = createPanel("Poll Clerk", Color.LIGHT_GRAY);
        validatedPanel = createPanel("Validated", Color.LIGHT_GRAY);
        rejectedPanel = createPanel("Rejected", Color.LIGHT_GRAY);
        votedPanel = createPanel("Voted", Color.LIGHT_GRAY);

        JPanel stationSubPanels = new JPanel(new GridLayout(4, 1));
        stationSubPanels.add(pollClerkPanel);
        stationSubPanels.add(validatedPanel);
        stationSubPanels.add(rejectedPanel);
        stationSubPanels.add(votedPanel);
        stationPanel.add(stationSubPanels, BorderLayout.CENTER);
        mainPanel.add(stationPanel);

        exitPollPanel = new JPanel(new BorderLayout());
        exitPollPanel.setBorder(BorderFactory.createTitledBorder("ExitPoll"));
        exitPollPanel.setBackground(Color.WHITE);

        exitPollingAreaPanel = createPanel("Exit Polling Area", Color.GRAY);
        pollsterPanel = createPanel("Pollster", Color.GRAY);
        pollsterVotesPanel = new JPanel(new GridLayout(2, 1));
        pollsterVotesPanel.setBorder(BorderFactory.createTitledBorder("Pollster Votes"));
        pollsterVotesALabel = new JLabel("A: 0", SwingConstants.CENTER);
        pollsterVotesBLabel = new JLabel("B: 0", SwingConstants.CENTER);
        pollsterVotesPanel.add(pollsterVotesALabel);
        pollsterVotesPanel.add(pollsterVotesBLabel);

        JPanel exitPollSubPanels = new JPanel(new GridLayout(3, 1));
        exitPollSubPanels.add(exitPollingAreaPanel);
        exitPollSubPanels.add(pollsterPanel);
        exitPollSubPanels.add(pollsterVotesPanel);

        exitPollPanel.add(exitPollSubPanels, BorderLayout.CENTER);
        mainPanel.add(exitPollPanel);

        JButton endSimulationButton = new JButton("End Simulation");
        endSimulationButton.addActionListener(e -> System.exit(0));

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(endSimulationButton, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private static JPanel createPanel(String title, Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setLayout(new GridLayout(0, 1, 5, 5));
        return panel;
    }

    @Override
    public void updateVoterState(int voterId, String state, boolean validationResult) {
        SwingUtilities.invokeLater(() -> {
            JLabel voterLabel = voterLabels.computeIfAbsent(voterId, id -> {
                JLabel label = new JLabel("Voter " + id, SwingConstants.CENTER);
                label.setOpaque(true);
                label.setBackground(Color.LIGHT_GRAY);
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                return label;
            });
    
            // Remove o rótulo de todos os painéis
            entrancePanel.remove(voterLabel);
            pollClerkPanel.remove(voterLabel);
            validatedPanel.remove(voterLabel);
            rejectedPanel.remove(voterLabel);
            votedPanel.remove(voterLabel);
            exitPollingAreaPanel.remove(voterLabel);
            pollsterPanel.remove(voterLabel);
    
            // Atualiza a posição e o texto do rótulo conforme o estado
            switch (state) {
                case "Entrance":
                    entrancePanel.add(voterLabel);
                    break;
                case "Poll Clerk":
                    pollClerkPanel.add(voterLabel);
                    break;
                case "Validated":
                    voterLabel.setBackground(Color.GREEN);
                    validatedPanel.add(voterLabel);
                    break;
                case "Rejected":
                    voterLabel.setBackground(Color.RED);
                    rejectedPanel.add(voterLabel);
                    break;
                case "Voted":
                    votedPanel.add(voterLabel);
                    updateVoteCount();
                    break;
                case "Exit Polling Area":
                    exitPollingAreaPanel.add(voterLabel);
                    break;
                case "Pollster":
                    voterLabel.setText("Voter " + voterId);
                    pollsterPanel.add(voterLabel);
                    break;
                case "Truth":
                    voterLabel.setText("Voter " + voterId + " - Truth");
                    pollsterVotesA++;
                    pollsterVotesALabel.setText("A: " + pollsterVotesA);
                    pollsterPanel.add(voterLabel);
                    break;
                case "Lied":
                    voterLabel.setText("Voter " + voterId + " - Lied");
                    pollsterVotesB++;
                    pollsterVotesBLabel.setText("B: " + pollsterVotesB);
                    pollsterPanel.add(voterLabel);
                    break;
                case "Left Pollster":
                    //Remove o texto do rótulo 
                    voterLabel.setText("Voter " + voterId);
                    // Remover da área do "Pollster" após sair
                    pollsterPanel.remove(voterLabel);
                     // Ou qualquer outra forma de limpar o estado, dependendo da implementação
                    voterLabel.setBackground(Color.GREEN);

                    pollsterPanel.revalidate();
                    pollsterPanel.repaint();

                    break;
            }
    
            // Atualiza os painéis para refletir as mudanças
            entrancePanel.revalidate();
            entrancePanel.repaint();
            pollClerkPanel.revalidate();
            pollClerkPanel.repaint();
            validatedPanel.revalidate();
            validatedPanel.repaint();
            rejectedPanel.revalidate();
            rejectedPanel.repaint();
            votedPanel.revalidate();
            votedPanel.repaint();
            exitPollingAreaPanel.revalidate();
            exitPollingAreaPanel.repaint();
            pollsterPanel.revalidate();
            pollsterPanel.repaint();
        });
    }
    
}
