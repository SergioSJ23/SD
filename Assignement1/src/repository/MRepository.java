package repository;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MRepository implements IRepository_all {

    private static MRepository instance;
    private final ReentrantLock lock = new ReentrantLock();
    private final Random rand = new Random();

    // Atributos do VotesBooth
    private int numVotes = 0;
    private int votesA = 0;
    private int votesB = 0;


    // Atributos do ExitPoll
    private int exitVotesA = 0;
    private int exitVotesB = 0;
    private int exitVotes = 0;
    // Configurações de simulação
    private final int lie = 20;         // 20% chance de mentir
    private final int noResponse = 60;  // 60% chance de não responder
    private final int approached = 10;   // 10% de chance de ser abordado

    // Variáveis de controle do ExitPoll
    private boolean pollsterReady = false;
    private boolean isClosed = false;
    private boolean votingDayEnded = false;
    private final Condition pollsterCondition = lock.newCondition();
    private final Condition voterCondition = lock.newCondition();
    private final Condition willAnswer = lock.newCondition();
    private final Condition hasAnswered = lock.newCondition();
    
    // Variáveis de controle do VotesBooth
    private ArrayList<Integer> idList = new ArrayList<>();

    private MRepository() { }

    public static MRepository getInstance() {
        if (instance == null) {
            instance = new MRepository();
        }
        return instance;
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
        try{
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
            System.out.println("Voter " + id + " voted for " + vote);
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
            System.out.println("Voter " + id + "entered EP " + vote);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void EPapproached(int id) {
        lock.lock();
        try {
            System.out.println("Voter " + id + " was approached by the pollster");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void EPtruth(int id, char vote) {
        lock.lock();
        try {
            System.out.println("Voter " + id + " told the truth: " + vote);
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
            System.out.println("Voter " + id + " entered the station");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void Sleave(int id) {
        lock.lock();
        try {
            System.out.println("Voter " + id + " left the station");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void Swait(int id) {
        lock.lock();
        try {
            System.out.println("Voter " + id + " is waiting");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void Spresent(int id) {
        lock.lock();
        try {
            System.out.println("Voter " + id + " presented their ID");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void Svalidate(int id) {
        lock.lock();
        try {
            System.out.println("Voter " + id + " is being validated");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void Srejected(int id) {
        lock.lock();
        try {
            System.out.println("Voter " + id + " was rejected");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void Svalidated(int id) {
        lock.lock();
        try {
            System.out.println("Voter " + id + " is valid");
        } finally {
            lock.unlock();
        }
    }
}
