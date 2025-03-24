package repository;

import java.util.ArrayList;
import genclass.GenericIO;
import genclass.TextFile;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.List;
import gui.VoterObserver;

// Classe que implementa a interface IRepository_all para gerir o repositório do sistema de votação
public class MRepository implements IRepository_all {

    // Instância única da classe (Singleton)
    private static MRepository instance;

    // Lock para sincronização em ambiente multi-thread
    private final ReentrantLock lock = new ReentrantLock();

    // Nome do ficheiro de log
    private final String logFileName = "voting_log.txt";

    // Atributos relacionados com a urna de votação (VotesBooth)
    private int numVotes = 0; // Número total de votos
    private int votesA = 0;   // Votos para a opção A
    private int votesB = 0;   // Votos para a opção B

    // Atributos relacionados com a sondagem de saída (ExitPoll)
    private int exitVotesA = 0; // Votos para A na sondagem de saída
    private int exitVotesB = 0; // Votos para B na sondagem de saída
    private int exitVotes = 0;  // Total de votos na sondagem de saída

    // Variáveis de controle do ExitPoll
    private boolean isClosed = false;      // Indica se a sondagem de saída está fechada
    private boolean votingDayEnded = false; // Indica se o dia de eleições terminou

    // Atributos relacionados com a estação de votação (Station)
    private final int[] votes = new int[2]; // Array para armazenar votos (A e B)

    // Lista de IDs de eleitores
    private final ArrayList<Integer> idList = new ArrayList<>();

    // Mapa para armazenar o estado atual dos eleitores (ID -> Estado)
    private final Map<Integer, Integer> voterState = new HashMap<>();

    // Lista de observadores (para atualizações da interface gráfica)
    private final List<VoterObserver> observers = new ArrayList<>();

    // Estados possíveis dos eleitores
    private final String[] possibleStates = {
        "Station", "Presenting", "Validated", "Rejected",
        "Voted", "Exit Poll", "Approached", "Truth", "Lied", "Left Pollster"
    };

    // Construtor privado para evitar instanciação direta (Singleton)
    private MRepository() {
        printHead(); // Inicializa o ficheiro de log com um cabeçalho
    }

    // Método estático para obter a instância única da classe (Singleton)
    public static MRepository getInstance() {
        if (instance == null) {
            instance = new MRepository(); // Cria a instância se não existir
        }
        return instance; // Retorna a instância existente
    }

    public int getVotesA() {
        return votesA;
    }

    public int getVotesB() {
        return votesB;
    }

    // ================= Métodos de Gestão de Observadores =================

    /**
     * Adiciona um observador à lista de observadores.
     * 
     * @param observer O observador a ser adicionado.
     */
    public void addObserver(VoterObserver observer) {
        lock.lock();
        try {
            observers.add(observer);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Remove um observador da lista de observadores.
     * 
     * @param observer O observador a ser removido.
     */
    public void removeObserver(VoterObserver observer) {
        lock.lock();
        try {
            observers.remove(observer);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Notifica todos os observadores sobre uma atualização no estado de um eleitor.
     * 
     * @param voterId O ID do eleitor.
     * @param state O estado atual do eleitor.
     * @param validationResult O resultado da validação (true se válido, false caso contrário).
     */
    private void notifyObservers(int voterId, String state, boolean validationResult) {
        for (VoterObserver observer : observers) {
            observer.updateVoterState(voterId, state, validationResult);
        }
    }

    // ================= Métodos da VotesBooth =================

    @Override
    public void VBincrementA() {
        lock.lock();
        try {
            votesA++; // Incrementa os votos para A
            numVotes++; // Incrementa o total de votos
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void VBincrementB() {
        lock.lock();
        try {
            votesB++; // Incrementa os votos para B
            numVotes++; // Incrementa o total de votos
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void VBvote(char vote, int id) {
        lock.lock();
        try {
            voterState.put(id, 4); // Marca o eleitor como tendo votado
            notifyObservers(id, "Voted", false); // Notifica os observadores
            printState(); // Atualiza o ficheiro de log
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void VBgetVotes(int votesA, int votesB) {
        lock.lock();
        try {
            System.out.println("Votes for A: " + votesA);
            System.out.println("Votes for B: " + votesB);
            printTail(); // Escreve o estado final no ficheiro de log
        } finally {
            lock.unlock();
        }
    }

    // ================= Métodos da ExitPoll =================

    @Override
    public void EPincrementA() {
        lock.lock();
        try {
            exitVotesA++; // Incrementa os votos para A na sondagem de saída
            exitVotes++; // Incrementa o total de votos na sondagem de saída
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void EPincrementB() {
        lock.lock();
        try {
            exitVotesB++; // Incrementa os votos para B na sondagem de saída
            exitVotes++; // Incrementa o total de votos na sondagem de saída
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void EPclose() {
        lock.lock();
        try {
            isClosed = true; // Marca a sondagem de saída como fechada
            System.out.println("Exit poll knows the station is closed");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void EPenter(char vote, int id) {
        lock.lock();
        try {
            voterState.put(id, 5); // Marca o eleitor como estando na sondagem de saída
            notifyObservers(id, "Exit Polling Area", false); // Notifica os observadores
            printState(); // Atualiza o ficheiro de log
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void EPapproached(int id) {
        lock.lock();
        try {
            voterState.put(id, 6); // Marca o eleitor como tendo sido abordado
            notifyObservers(id, "Pollster", false); // Notifica os observadores
            printState(); // Atualiza o ficheiro de log
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void EPtruth(int id, char vote) {
        lock.lock();
        try {
            voterState.put(id, 7); // Marca o eleitor como tendo dito a verdade
            notifyObservers(id, "Truth", true); // Notifica os observadores
            printState(); // Atualiza o ficheiro de log
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void EPleave(int id) {
        lock.lock();
        try {
            voterState.remove(id); // Remove o eleitor do mapa de estados
            notifyObservers(id, "Left Pollster", false); // Notifica os observadores
            printState(); // Atualiza o ficheiro de log
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void EPlied(int id, char vote) {
        lock.lock();
        try {
            if (vote == 'A') {
                System.out.println("Voter " + id + " lied about voting for party B");
            } else {
                System.out.println("Voter " + id + " lied about voting for party A");
            }
            voterState.put(id, 8); // Marca o eleitor como tendo mentido
            notifyObservers(id, "Lied", false); // Notifica os observadores
            printState(); // Atualiza o ficheiro de log
            leaveExitPoll(id); // Remove o eleitor da sondagem de saída
        } finally {
            lock.unlock();
        }
    }

    /**
     * Remove um eleitor da sondagem de saída.
     * 
     * @param voterId O ID do eleitor.
     */
    private void leaveExitPoll(int voterId) {
        lock.lock();
        try {
            voterState.remove(voterId); // Remove o eleitor do mapa de estados
            notifyObservers(voterId, "Left", false); // Notifica os observadores
        } finally {
            lock.unlock();
        }
    }

    // ================= Métodos da Station =================

    @Override
    public void Sopen() {
        lock.lock();
        try {
            isClosed = false; // Marca a estação como aberta
            System.out.println("Station is open");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void SaddId(int id) {
        lock.lock();
        try {
            idList.add(id); // Adiciona o ID do eleitor à lista
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void Sclose() {
        lock.lock();
        try {
            isClosed = true; // Marca a estação como fechada
            System.out.println("Station is closed");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void SannounceEnding() {
        lock.lock();
        try {
            votingDayEnded = true; // Marca o fim do dia de eleições
            System.out.println("Voting day has ended");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void Senter(int id) {
        lock.lock();
        try {
            voterState.put(id, 0); // Marca o eleitor como estando na estação
            notifyObservers(id, "Entrance", false); // Notifica os observadores
            printState(); // Atualiza o ficheiro de log
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void Sleave(int id) {
        lock.lock();
        try {
            // Lógica para o eleitor sair da estação (pode ser implementada)
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void Spresent(int id) {
        lock.lock();
        try {
            voterState.put(id, 1); // Marca o eleitor como tendo apresentado o ID
            notifyObservers(id, "Poll Clerk", false); // Notifica os observadores
            printState(); // Atualiza o ficheiro de log
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void Srejected(int id) {
        lock.lock();
        try {
            voterState.put(id, 3); // Marca o eleitor como tendo sido rejeitado
            notifyObservers(id, "Rejected", false); // Notifica os observadores
            printState(); // Atualiza o ficheiro de log
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void Svalidated(int id) {
        lock.lock();
        try {
            voterState.put(id, 2); // Marca o eleitor como tendo sido validado
            notifyObservers(id, "Validated", true); // Notifica os observadores
            printState(); // Atualiza o ficheiro de log
        } finally {
            lock.unlock();
        }
    }

    // ================= Métodos de Logging =================

    /**
     * Escreve o cabeçalho no ficheiro de log.
     */
    private void printHead() {
        TextFile log = new TextFile();
        if (!log.openForWriting(".", logFileName)) {
            GenericIO.writelnString("O processo de criação do ficheiro " + logFileName + " falhou!");
            System.exit(1);
        }

        // Cabeçalho com estados possíveis
        StringBuilder header = new StringBuilder();
        for (String state : possibleStates) {
            header.append(String.format("%-20s | ", state)); // Formatação fixa para cada coluna
        }
        header.append("Votes A       | Votes B      | Exit A       | Exit B       | Closed   ");

        log.writelnString("=== Log do Sistema de Votação ===");
        log.writelnString(header.toString());
        log.writelnString("------------------------------------------------------------------");

        if (!log.close()) {
            GenericIO.writelnString("O processo de fecho do ficheiro " + logFileName + " falhou.");
        }
    }

    /**
     * Escreve o estado atual no ficheiro de log.
     */
    private void printState() {
        TextFile log = new TextFile();
        if (!log.openForAppending(".", logFileName)) {
            GenericIO.writelnString("O processo de abertura do ficheiro " + logFileName + " falhou!");
            System.exit(1);
        }

        // Mapa para agrupar eleitores por estado
        Map<Integer, StringBuilder> stateToVoters = new HashMap<>();
        for (int i = 0; i < possibleStates.length; i++) {
            stateToVoters.put(i, new StringBuilder());
        }

        // Agrupa os IDs dos eleitores por estado
        for (Map.Entry<Integer, Integer> entry : voterState.entrySet()) {
            int id = entry.getKey();
            int state = entry.getValue();
            if (stateToVoters.containsKey(state)) {
                if (stateToVoters.get(state).length() > 0) {
                    stateToVoters.get(state).append(",");
                }
                stateToVoters.get(state).append(id);
            }
        }

        // Prepara a linha com os IDs dos eleitores para cada estado
        StringBuilder stateLine = new StringBuilder();
        for (int i = 0; i < possibleStates.length; i++) {
            String voters = stateToVoters.get(i).toString();
            stateLine.append(String.format("%-20s | ", voters)); // Formatação fixa para cada coluna
        }

        // Adiciona as contagens de votos à linha
        stateLine.append(String.format(" %-12d | %-12d | %-12d | %-12d | %-12s", 
                                     votesA, votesB, exitVotesA, exitVotesB, isClosed));

        // Escreve a linha no ficheiro de log
        log.writelnString(stateLine.toString());

        if (!log.close()) {
            GenericIO.writelnString("O processo de fecho do ficheiro " + logFileName + " falhou.");
        }
    }

    /**
     * Escreve o estado final da simulação no ficheiro de log.
     */
    public void printTail() {
        TextFile log = new TextFile();
        if (!log.openForAppending(".", logFileName)) {
            GenericIO.writelnString("O processo de abertura do ficheiro " + logFileName + " falhou!");
            System.exit(1);
        }

        log.writelnString("O dia de eleições terminou. Total de votos: A = " + votesA + ", B = " + votesB);
        log.writelnString("O dia de eleições terminou. Votos na sondagem de saída: A = " + exitVotesA + ", B = " + exitVotesB);

        if (!log.close()) {
            GenericIO.writelnString("O processo de fecho do ficheiro " + logFileName + " falhou.");
        }
    }
}