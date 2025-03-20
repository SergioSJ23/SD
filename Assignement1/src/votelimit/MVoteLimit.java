package votelimit;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class MVoteLimit implements IVoteLimit_all{

    private final int votingLimit = 10;
    private int numVotes = 0;
    private final ArrayList<Integer> idList = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();
    private static IVoteLimit_all instance;

    public static IVoteLimit_all getInstance() {
        if (instance == null) {
            instance = new MVoteLimit();
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