package gui;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import repository.MRepository;

// Classe que implementa a interface gráfica (GUI) para o sistema de votação
public class VotingStationGUI implements VoterObserver {
    // Componentes da interface gráfica
    private static JFrame frame; // Janela principal
    private final static Map<Integer, JLabel> voterLabels = new HashMap<>(); // Mapa para armazenar os rótulos dos eleitores
    private static JPanel votesPanel; // Painel para exibir os votos
    private static JLabel votesALabel; // Rótulo para votos da opção A
    private static JLabel votesBLabel; // Rótulo para votos da opção B
    private static JLabel pollsterVotesALabel; // Rótulo para votos da opção A na sondagem
    private static JLabel pollsterVotesBLabel; // Rótulo para votos da opção B na sondagem

    // Painéis para diferentes áreas da interface
    private static JPanel entrancePanel; // Painel para a entrada dos eleitores
    private static JPanel stationPanel; // Painel para a estação de votação
    private static JPanel exitPollPanel; // Painel para a sondagem de saída

    // Subpainéis da estação de votação
    private static JPanel pollClerkPanel; // Painel para o funcionário da estação
    private static JPanel validatedPanel; // Painel para eleitores validados
    private static JPanel rejectedPanel; // Painel para eleitores rejeitados
    private static JPanel votedPanel; // Painel para eleitores que votaram

    // Subpainéis da sondagem de saída
    private static JPanel exitPollingAreaPanel; // Painel para a área de sondagem
    private static JPanel pollsterPanel; // Painel para o entrevistador
    private static JPanel pollsterVotesPanel; // Painel para os votos da sondagem

    // Contadores de votos da sondagem
    private static int pollsterVotesA = 0; // Votos para A na sondagem
    private static int pollsterVotesB = 0; // Votos para B na sondagem

    // Método para iniciar a interface gráfica
    public static void run() {
        SwingUtilities.invokeLater(VotingStationGUI::createAndShowGUI);
    }

    // Método para atualizar a contagem de votos na interface
    private static void updateVoteCount() {
        votesALabel.setText("A: " + MRepository.getInstance().getVotesA());
        votesBLabel.setText("B: " + MRepository.getInstance().getVotesB());
    }

    // Método para criar e exibir a interface gráfica
    private static void createAndShowGUI() {
        frame = new JFrame("Voting Station"); // Cria a janela principal
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Define o comportamento ao fechar
        frame.setSize(1200, 600); // Define o tamanho da janela

        // Painel principal com layout de grade (1 linha, 4 colunas)
        JPanel mainPanel = new JPanel(new GridLayout(1, 4));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adiciona margens

        // Painel para exibir os votos
        votesPanel = new JPanel(new GridLayout(2, 1));
        votesPanel.setBorder(BorderFactory.createTitledBorder("Votes")); // Título do painel
        votesALabel = new JLabel("A: 0", SwingConstants.CENTER); // Rótulo para votos da opção A
        votesBLabel = new JLabel("B: 0", SwingConstants.CENTER); // Rótulo para votos da opção B
        votesPanel.add(votesALabel);
        votesPanel.add(votesBLabel);
        mainPanel.add(votesPanel); // Adiciona o painel de votos ao painel principal

        // Painel para a entrada dos eleitores
        entrancePanel = createPanel("Entrance", Color.WHITE);
        mainPanel.add(entrancePanel);

        // Painel para a estação de votação
        stationPanel = new JPanel(new BorderLayout());
        stationPanel.setBorder(BorderFactory.createTitledBorder("Station")); // Título do painel
        stationPanel.setBackground(Color.WHITE);

        // Subpainéis da estação de votação
        pollClerkPanel = createPanel("Poll Clerk", Color.LIGHT_GRAY); // Funcionário da estação
        validatedPanel = createPanel("Validated", Color.LIGHT_GRAY); // Eleitores validados
        rejectedPanel = createPanel("Rejected", Color.LIGHT_GRAY); // Eleitores rejeitados
        votedPanel = createPanel("Voted", Color.LIGHT_GRAY); // Eleitores que votaram

        // Painel para organizar os subpainéis da estação
        JPanel stationSubPanels = new JPanel(new GridLayout(4, 1));
        stationSubPanels.add(pollClerkPanel);
        stationSubPanels.add(validatedPanel);
        stationSubPanels.add(rejectedPanel);
        stationSubPanels.add(votedPanel);
        stationPanel.add(stationSubPanels, BorderLayout.CENTER); // Adiciona ao painel da estação
        mainPanel.add(stationPanel); // Adiciona o painel da estação ao painel principal

        // Painel para a sondagem de saída
        exitPollPanel = new JPanel(new BorderLayout());
        exitPollPanel.setBorder(BorderFactory.createTitledBorder("ExitPoll")); // Título do painel
        exitPollPanel.setBackground(Color.WHITE);

        // Subpainéis da sondagem de saída
        exitPollingAreaPanel = createPanel("Exit Polling Area", Color.GRAY); // area de sondagem
        pollsterPanel = createPanel("Pollster", Color.GRAY); // Entrevistador
        pollsterVotesPanel = new JPanel(new GridLayout(2, 1)); // Painel para os votos da sondagem
        pollsterVotesPanel.setBorder(BorderFactory.createTitledBorder("Pollster Votes")); // Título do painel
        pollsterVotesALabel = new JLabel("A: 0", SwingConstants.CENTER); // Rótulo para votos da opção A na sondagem
        pollsterVotesBLabel = new JLabel("B: 0", SwingConstants.CENTER); // Rótulo para votos da opção B na sondagem
        pollsterVotesPanel.add(pollsterVotesALabel);
        pollsterVotesPanel.add(pollsterVotesBLabel);

        // Painel para organizar os subpainéis da sondagem
        JPanel exitPollSubPanels = new JPanel(new GridLayout(3, 1));
        exitPollSubPanels.add(exitPollingAreaPanel);
        exitPollSubPanels.add(pollsterPanel);
        exitPollSubPanels.add(pollsterVotesPanel);

        exitPollPanel.add(exitPollSubPanels, BorderLayout.CENTER); // Adiciona ao painel da sondagem
        mainPanel.add(exitPollPanel); // Adiciona o painel da sondagem ao painel principal

        // Botão para terminar a simulação
        JButton endSimulationButton = new JButton("End Simulation");
        endSimulationButton.addActionListener(e -> System.exit(0)); // Fecha o programa ao ser clicado

        // Adiciona o painel principal e o botão à janela
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(endSimulationButton, BorderLayout.SOUTH);
        frame.setVisible(true); // Torna a janela visível
    }

    // Método para criar um painel com título e cor de fundo
    private static JPanel createPanel(String title, Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color); // Define a cor de fundo
        panel.setBorder(BorderFactory.createTitledBorder(title)); // Adiciona um título
        panel.setLayout(new GridLayout(0, 1, 5, 5)); // Define o layout
        return panel;
    }

    // Método para atualizar o estado de um eleitor na interface
    @Override
    public void updateVoterState(int voterId, String state, boolean validationResult) {
        SwingUtilities.invokeLater(() -> {
            // Obtém ou cria um rótulo para o eleitor
            JLabel voterLabel = voterLabels.computeIfAbsent(voterId, id -> {
                JLabel label = new JLabel("Voter " + id, SwingConstants.CENTER);
                label.setOpaque(true); // Permite que o fundo seja colorido
                label.setBackground(Color.LIGHT_GRAY); // Cor de fundo padrão
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Borda preta
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
                    entrancePanel.add(voterLabel); // Adiciona ao painel de entrada
                    break;
                case "Poll Clerk":
                    pollClerkPanel.add(voterLabel); // Adiciona ao painel do funcionário
                    break;
                case "Validated":
                    voterLabel.setBackground(Color.GREEN); // Muda a cor para verde
                    validatedPanel.add(voterLabel); // Adiciona ao painel de validados
                    break;
                case "Rejected":
                    voterLabel.setBackground(Color.RED); // Muda a cor para vermelho
                    rejectedPanel.add(voterLabel); // Adiciona ao painel de rejeitados
                    break;
                case "Voted":
                    votedPanel.add(voterLabel); // Adiciona ao painel de votados
                    updateVoteCount(); // Atualiza a contagem de votos
                    break;
                case "Exit Polling Area":
                    exitPollingAreaPanel.add(voterLabel); // Adiciona ao painel da área de sondagem
                    break;
                case "Pollster":
                    voterLabel.setText("Voter " + voterId); // Atualiza o texto do rótulo
                    pollsterPanel.add(voterLabel); // Adiciona ao painel do entrevistador
                    break;
                case "Truth":
                    voterLabel.setText("Voter " + voterId + " - Truth"); // Atualiza o texto do rótulo
                    pollsterVotesA++; // Incrementa os votos para A na sondagem
                    pollsterVotesALabel.setText("A: " + pollsterVotesA); // Atualiza o rótulo de votos
                    pollsterPanel.add(voterLabel); // Adiciona ao painel do entrevistador
                    break;
                case "Lied":
                    voterLabel.setText("Voter " + voterId + " - Lied"); // Atualiza o texto do rótulo
                    pollsterVotesB++; // Incrementa os votos para B na sondagem
                    pollsterVotesBLabel.setText("B: " + pollsterVotesB); // Atualiza o rótulo de votos
                    pollsterPanel.add(voterLabel); // Adiciona ao painel do entrevistador
                    break;
                case "Left Pollster":
                    voterLabel.setText("Voter " + voterId); // Remove o texto adicional
                    pollsterPanel.remove(voterLabel); // Remove do painel do entrevistador
                    voterLabel.setBackground(Color.GREEN); // Restaura a cor de fundo
                    pollsterPanel.revalidate(); // Atualiza o painel
                    pollsterPanel.repaint(); // Redesenha o painel
                    break;
            }

            // Atualiza todos os painéis para refletir as mudanças
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