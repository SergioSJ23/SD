package votesbooth;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class MVotesBooth implements IVotesBooth_all{

    private static int numVotes = 0;
    private static int votesA = 0;
    private static int votesB = 0;
    private static final int votingLimit = 50;
    private static IVotesBooth_all instance;
    private final ReentrantLock lock = new ReentrantLock();
        
    private final Random rand = new Random();


    public static IVotesBooth_all getInstance() {
        if (instance == null) {
            instance = new MVotesBooth();
        }
        return instance;
    }

    @Override
    public void vote(char vote) {
        lock.lock();
        try {
            if(vote == 'A'){
                incrementA();
            }else{
                incrementB();
            }
        } finally {
            lock.unlock();
        }
    }

    private void incrementA() {
        votesA++;
        numVotes++;
    }

    private void incrementB() {
        votesB++;
        numVotes++;
    }

    @Override
    public int[] getVotes() {
        lock.lock();
        try {
            return new int[]{votesA, votesB};
        } finally {
            lock.unlock();
        }
    }
}