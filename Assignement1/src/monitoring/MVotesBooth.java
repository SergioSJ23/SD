package monitoring;

import contracts.IVotesBooth;
import java.util.concurrent.locks.ReentrantLock;

public class MVotesBooth implements IVotesBooth{

    private int votesA = 0;
    private int votesB = 0;
    private final ReentrantLock lock = new ReentrantLock();

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

    @Override
    public void incrementA() {
        votesA++;
    }

    @Override
    public void incrementB() {
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