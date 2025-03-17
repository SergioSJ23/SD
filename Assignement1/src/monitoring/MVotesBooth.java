package monitoring;

import java.util.concurrent.locks.ReentrantLock;

public class MVotesBooth {

    private int votesA = 0;
    private int votesB = 0;
    private final ReentrantLock lock = new ReentrantLock();

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

    public int[] getVotes() {
        lock.lock();
        try {
            return new int[]{votesA, votesB};
        } finally {
            lock.unlock();
        }
    }
}