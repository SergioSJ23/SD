package repository;

import java.util.ArrayList;
import genclass.GenericIO;
import genclass.TextFile;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.List;
import gui.VoterObserver;

public class MRepository implements IRepository_all {

    private static MRepository instance;
    private final ReentrantLock lock = new ReentrantLock();
    private final String logFileName = "voting_log.txt";

    // Atributos do VotesBooth
    private int numVotes = 0;
    private int votesA = 0;
    private int votesB = 0;

    // Atributos do ExitPoll
    private int exitVotesA = 0;
    private int exitVotesB = 0;
    private int exitVotes = 0;

    // Variáveis de controle do ExitPoll
    private boolean isClosed = false;
    private boolean votingDayEnded = false;

    // Atributos da Station
    private int[] votes = new int[2];

    // Variáveis de controle do VotesBooth
    private ArrayList<Integer> idList = new ArrayList<>();
    
    // Map to store voter states
    private final Map<Integer, Integer> voterState = new HashMap<>(); // Key: Voter ID, Value: State
    private final List<VoterObserver> observers = new ArrayList<>(); // Observers list

    // Possible states
    private final String[] possibleStates = {
        "Station", "Presenting", "Validated", "Rejected",
        "Voted", "Exit Poll", "Approached", "Truth", "Lied", "Left Pollster"
    };

    private MRepository() {
        printHead(); // Initialize the log file with a header
    }

    public static MRepository getInstance() {
        if (instance == null) {
            instance = new MRepository();
        }
        return instance;
    }

    // Observer management
    public void addObserver(VoterObserver observer) {
        lock.lock();
        try {
            observers.add(observer);
        } finally {
            lock.unlock();
        }
    }

    public void removeObserver(VoterObserver observer) {
        lock.lock();
        try {
            observers.remove(observer);
        } finally {
            lock.unlock();
        }
    }

    private void notifyObservers(int voterId, String state, boolean validationResult) {
        for (VoterObserver observer : observers) {
            observer.updateVoterState(voterId, state, validationResult);
        }
    }

    public int getVotesA() {
        return votesA;
    }
    
    public int getVotesB() {
        return votesB;
    }
    

    // ================= Métodos do VotesBooth =================

    @Override
    public void VBincrementA() {
        lock.lock();
        try {
            votesA++;
            numVotes++;
            
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void VBincrementB() {
        lock.lock();
        try {
            votesB++;
            numVotes++;
            
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void VBvote(char vote, int id) {
        lock.lock();
        try {
            
            voterState.put(id, 4); // Mark voter as having voted
            notifyObservers(id, "Voted", false);
            printState();
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
            printTail();
        } finally {
            lock.unlock();
        }
    }

    // ================= Métodos do ExitPoll =================

    @Override
    public void EPincrementA() {
        lock.lock();
        try {
            exitVotesA++;
            exitVotes++;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void EPincrementB() {
        lock.lock();
        try {
            exitVotesB++;
            exitVotes++;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void EPclose() {
        lock.lock();
        try {
            isClosed = true;
            System.out.println("Exit poll knows the station is closed");
            
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void EPenter(char vote, int id) {
        lock.lock();
        try {
            
            voterState.put(id, 5); // Mark voter as in exit poll
            notifyObservers(id, "Exit Polling Area", false);
            printState();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void EPapproached(int id) {
        lock.lock();
        try {
            
            voterState.put(id, 6); // Mark voter as approached
            notifyObservers(id, "Pollster", false);
            System.out.println();
            printState();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void EPtruth(int id, char vote) {
        lock.lock();
        try {
            voterState.put(id, 7); // Mark voter as having told the truth
            notifyObservers(id, "Truth", true);
            printState();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void EPleave(int id) {
        lock.lock();
        try {
            voterState.remove(id); // Remove voter from the state map
            notifyObservers(id, "Left Pollster", false);
            
            printState();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void EPlied(int id, char vote) {
        lock.lock();
        try {
            if(vote == 'A'){
                System.out.println("Voter " + id + " lied about voting for party B");
            } else {
                System.out.println("Voter " + id + " lied about voting for party A");
            }
            voterState.put(id, 8); // Mark voter as having lied
            notifyObservers(id, "Lied", false);
            printState();
            leaveExitPoll(id);
        } finally {
            lock.unlock();
        }
    }

    private void leaveExitPoll(int voterId) {
        lock.lock();
        try {
            voterState.remove(voterId); // Remove voter from the state map
            notifyObservers(voterId, "Left", false);
        } finally {
            lock.unlock();
        }
    }

    // ================= Métodos da Station =================
    
    @Override
    public void Sopen() {
        lock.lock();
        try {
            isClosed = false;
            System.out.println("Station is open");
           
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void SaddId(int id) {
        lock.lock();
        try {
            idList.add(id);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void Sclose() {
        lock.lock();
        try {
            isClosed = true;
            System.out.println("Station is closed");
            
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void SannounceEnding() {
        lock.lock();
        try {
            votingDayEnded = true;
            System.out.println("Voting day has ended");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void Senter(int id) {
        lock.lock();
        try {
            voterState.put(id, 0); // Mark voter as in station
            notifyObservers(id, "Entrance", false);
            printState();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void Sleave(int id) {
        lock.lock();
        try {
            
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void Spresent(int id) {
        lock.lock();
        try {
            voterState.put(id, 1); // Mark voter as having presented ID
            notifyObservers(id, "Poll Clerk", false);
            printState();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void Srejected(int id) {
        lock.lock();
        try {
            
            voterState.put(id, 3); // Mark voter as rejected
            notifyObservers(id, "Rejected", false);
            printState();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void Svalidated(int id) {
        lock.lock();
        try {
            
            voterState.put(id, 2); // Mark voter as validated
            notifyObservers(id, "Validated", true);
            printState();
        } finally {
            lock.unlock();
        }
    }

    // Restante do código (printHead, printState, printTail) permanece o mesmo


    // ================= Logging Methods =================

    /**
     * Write the header to the logging file.
     */
   // ================= Logging Methods =================

// ================= Logging Methods =================

/**
 * Write the header to the logging file.
 */
private void printHead() {
    TextFile log = new TextFile();
    if (!log.openForWriting(".", logFileName)) {
        GenericIO.writelnString("The process of creating the file " + logFileName + " failed!");
        System.exit(1);
    }

    // Print headers (states) with fixed width
    StringBuilder header = new StringBuilder();
    for (String state : possibleStates) {
        header.append(String.format("%-20s | ", state)); // Fixed width of 20 characters for each column
    }
    header.append("Votes A       | Votes B      | Exit A       | Exit B       | Closed   ");

    log.writelnString("=== Voting System Log ===");
    log.writelnString(header.toString());
    log.writelnString("------------------------------------------------------------------");

    if (!log.close()) {
        GenericIO.writelnString("While trying to close the file " + logFileName + " the process failed.");
    }
}

/**
 * Write the current state to the logging file.
 */
/**
 * Write the current state to the logging file.
 */
private void printState() {
    TextFile log = new TextFile();
    if (!log.openForAppending(".", logFileName)) {
        GenericIO.writelnString("The process of appending in file " + logFileName + " could not be started!");
        System.exit(1);
    }

    // Initialize a map to group voter IDs by state
    Map<Integer, StringBuilder> stateToVoters = new HashMap<>();
    for (int i = 0; i < possibleStates.length; i++) {
        stateToVoters.put(i, new StringBuilder());
    }

    // Group voter IDs by their current state
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

    // Prepare the row with voter IDs for each state
    StringBuilder stateLine = new StringBuilder();
    for (int i = 0; i < possibleStates.length; i++) {
        String voters = stateToVoters.get(i).toString();
        stateLine.append(String.format("%-20s | ", voters)); // Fixed width of 20 characters for each column
    }
    
    // Append the vote counts to the state line
    stateLine.append(String.format(" %-12d | %-12d | %-12d | %-12d | %-12s", 
                                   votesA, votesB, exitVotesA, exitVotesB, isClosed));

    // Write the row to the log file
    log.writelnString(stateLine.toString());

    if (!log.close()) {
        GenericIO.writelnString("While trying to close the file " + logFileName + " the process failed.");
    }
}


    /**
     * Write the final state of the simulation at the end of the logging file.
     */
    public void printTail() {
        TextFile log = new TextFile();
        if (!log.openForAppending(".", logFileName)) {
            GenericIO.writelnString("The process of appending in file " + logFileName + " could not be started!");
            System.exit(1);
        }

        log.writelnString("Voting day has ended. Total votes: A = " + votesA + ", B = " + votesB);
        log.writelnString("Voting day has ended. Exit Poll votes: A = " + exitVotesA + ", B = " + exitVotesB);

        if (!log.close()) {
            GenericIO.writelnString("While trying to close the file " + logFileName + " the process failed.");
        }
    }


}