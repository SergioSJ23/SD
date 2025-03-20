package monitoring;

import contracts.IVoteLimit;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class MVoteLimit implements IVoteLimit{

    private final int votingLimit;
    private int numVotes = 0;
    private final ArrayList<Integer> idList = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();

    public MVoteLimit(int votingLimit) {
        this.votingLimit = votingLimit;
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