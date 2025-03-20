package votesbooth;

import java.util.concurrent.locks.ReentrantLock;

public class MVotesBooth implements IVotesBooth{

    private int votesA = 0;
    private int votesB = 0;
    private static IVotesBooth instance;
    private final ReentrantLock lock = new ReentrantLock();

    public static IVotesBooth getInstance() {
        if (instance == null) {
            instance = new MVotesBooth();
        }
        return instance;
    }

    @Override
    public void vote(char vote) {
        lock.lock();
        try {
            if (vote == 'A') {
                incrementA();
            } else {
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