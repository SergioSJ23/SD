package votesbooth;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class MVotesBooth implements IVotesBooth_all{

    private static int votesA = 0;
    private static int votesB = 0;
    private final static int partyAodds = 50;
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
    public void vote() {
        lock.lock();
        try {
            if(rand.nextInt(100) < partyAodds){
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
    }

    private void incrementB() {
        votesB++;
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