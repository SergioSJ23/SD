package monitoring;

import java.util.concurrent.locks.ReentrantLock;

public class MVotes {

    private int votesA = 0;
    private int votesB = 0;
    private final ReentrantLock lock = new ReentrantLock();

    public void increment(int voteId) {
        lock.lock();
        try {
            if (voteId == 0) {
                votesA++;
            } else {
                votesB++;
            }
        } finally {
            lock.unlock();
        }
    }

    public int getVotesA() {
        lock.lock();
        try {
            return votesA;
        } finally {
            lock.unlock();
        }
    }

    public int getVotesB() {
        lock.lock();
        try {
            return votesB;
        } finally {
            lock.unlock();
        }
    }

    // Optional: Return both votes in one method
    public int[] getVotes() {
        lock.lock();
        try {
            return new int[]{votesA, votesB};
        } finally {
            lock.unlock();
        }
    }
}