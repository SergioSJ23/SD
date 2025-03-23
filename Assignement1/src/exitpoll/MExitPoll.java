package exitpoll;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import repository.IRepository_ExitPoll;
import repository.MRepository;

public class MExitPoll implements IExitPoll_all {

    private int votesA = 0;
    private int votesB = 0;
    private char vote;
    private int voterId;
    private final int lie = 20;
    private final int noResponse = 60;
    private final int approached = 10;
    private final ReentrantLock lock = new ReentrantLock();
    private static IExitPoll_all instance;
    private final Condition pollsterCondition = lock.newCondition();
    private final Condition voterCondition = lock.newCondition();
    private boolean pollsterReady = false;
    private final IRepository_ExitPoll repository = MRepository.getInstance();

    private final Random rand = new Random();
    private static boolean isClosed = false;

    public static IExitPoll_all getInstance() {
        if (instance == null) {
            instance = new MExitPoll();
        }
        return instance;
    }

    private void increment(int voteId) {
        lock.lock();
        try {
            if (voteId == 0) {
                votesA++;
                repository.EPincrementA();
            } else {
                votesB++;
                repository.EPincrementB();
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void stationIsClosed() {
        lock.lock();
        try {
            isClosed = true;
            repository.EPclose();
        } finally {
            lock.unlock();
        }
    }


    @Override
    public void enterExitPoll(char vote, int voterId) {
        if (isClosed){
            Thread.currentThread().interrupt();
            return;
        }
        lock.lock();
        try {
            this.vote = vote;
            this.voterId = voterId;
            pollsterReady = true;
            pollsterCondition.signalAll();
            voterCondition.await(); // Wait until validation is complete
            repository.EPenter(vote, voterId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void leaveExitPoll(int voterId) {
        lock.lock();
        try {
            repository.EPleave(voterId);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void inquire() throws InterruptedException {
        lock.lock();
        try {
        while (!pollsterReady) {
            pollsterCondition.await();
        }
        if (rand.nextInt(100) < approached) { // 10% chance of being approached
            repository.EPapproached(voterId);
            increment(vote == 'A' ? 0 : 1);
            if (rand.nextInt(100) > noResponse) { // 60% chance of not responding
                if (rand.nextInt(100) < lie) { // 20% chance of lying
                    if (vote == 'A') {
                        repository.EPlied(voterId , vote);
                    } else {
                        repository.EPlied(voterId , vote);
                    }
                } else {
                    repository.EPtruth(voterId , vote);
                }
            }
        }

        pollsterReady = false; // Reset pollster readiness for the next voter
        voterCondition.signalAll(); // Notify the voter to continue
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt(); // Restore the interrupt status
        throw e; // Re-throw the exception to propagate the interruption
        } finally {
            lock.unlock();
        }
    }
}