package votelimit;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class MVoteLimit implements IVoteLimit_all{

    private final int votingLimit;
    private int numVotes = 0;
    private final ArrayList<Integer> idList = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();
    private static IVoteLimit_all instance;

    public MVoteLimit(int votingLimit) {
        this.votingLimit = votingLimit;
    }

    public static IVoteLimit_all getInstance(int maxVoters) {
        if (instance == null) {
            instance = new MVoteLimit(maxVoters);
        }
        return instance;
    }


    @Override
    public boolean validateAndAdd(int id) {
        lock.lock();
        try {
            if (idList.contains(id)) {
                return false;
            } else {
                idList.add(id);
                numVotes += 1;
                isLimitReached();
                return true;
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean isLimitReached() {
        return numVotes >= votingLimit;
    }
}