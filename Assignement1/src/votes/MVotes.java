package votes;

import java.util.concurrent.locks.ReentrantLock;

public class MVotes implements IVotes_all{

    private int votesA = 0;
    private int votesB = 0;
    private final ReentrantLock lock = new ReentrantLock();
    private static IVotes_all instance;

    public static IVotes_all getInstance() {
        if (instance == null) {
            instance = new MVotes();
        }
        return instance;
    }

    @Override
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

    @Override
    public int getVotesA() {
        lock.lock();
        try {
            return votesA;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int getVotesB() {
        lock.lock();
        try {
            return votesB;
        } finally {
            lock.unlock();
        }
    }

    // Optional: Return both votes in one method
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