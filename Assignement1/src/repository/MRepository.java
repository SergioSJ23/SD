package repository;

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
    private char exitVote;
    // Configurações de simulação
    private final int lie = 20;         // 20% chance de mentir
    private final int noResponse = 60;  // 60% chance de não responder
    private final int approached = 10;   // 10% de chance de ser abordado

    // Variáveis de controle do ExitPoll
    private boolean pollsterReady = false;
    private boolean isClosed = false;
    private final Condition pollsterCondition = lock.newCondition();
    private final Condition voterCondition = lock.newCondition();
    private final Condition willAnswer = lock.newCondition();
    private final Condition hasAnswered = lock.newCondition();

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
        votesA++;
        numVotes++;
    }

    @Override
    public void VBincrementB() {
        votesB++;
        numVotes++;
    }

    // ================= Métodos do ExitPoll =================


    @Override
    public void EPincrementA() {
        lock.lock();
        try {
            votesA++;
            numVotes++;
        } finally {
            lock.unlock();
        }
    }


    @Override
    public void EPincrementB() {
        lock.lock();
        try {
            votesB++;
            numVotes++;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void EPclose() {
        lock.lock();
        try {
            isClosed = true;
        } finally {
            lock.unlock();
        }
    }

    // ================= Métodos do VotesBooth =================
    
}
