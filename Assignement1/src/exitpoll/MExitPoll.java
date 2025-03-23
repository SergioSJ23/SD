package exitpoll;

import java.util.concurrent.locks.ReentrantLock;
import repository.IRepository_ExitPoll;
import repository.MRepository;

public class MExitPoll implements IExitPoll_all {

    private static IExitPoll_all instance;
    private final ReentrantLock lock = new ReentrantLock();
    private final IRepository_ExitPoll repository = MRepository.getInstance();

    private MExitPoll() { }

    public static IExitPoll_all getInstance() {
        if (instance == null) {
            instance = new MExitPoll();
        }
        return instance;
    }


    @Override
    public int getVotesA() {
        lock.lock();
        try {
            return repository.getVotesAExit();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int getVotesB() {
        lock.lock();
        try {
            return repository.getVotesBExit();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int[] getVotes() {
        lock.lock();
        try {
            return repository.getExitVotes();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void stationIsClosed() {
        lock.lock();
        try {
            repository.stationIsClosed();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void enterExitPoll(char vote) {
        //tive que retirar o lock pois estava a dar deadlock
       repository.enterExitPoll(vote);
            
    }

    @Override
    public void inquire() throws InterruptedException {
        lock.lock();
        try {
            repository.inquire();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void question() throws InterruptedException {
        lock.lock();
        try {
            repository.question();
        } finally {
            lock.unlock();
        }
    }
}
