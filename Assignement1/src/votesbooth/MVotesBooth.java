package votesbooth;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
import repository.IRepository_VotesBooth;
import repository.MRepository;

public class MVotesBooth implements IVotesBooth_all{

    private static int numVotes = 0;
    private static int votesA = 0;
    private static int votesB = 0;
    private static final int votingLimit = 50;
    private static IVotesBooth_all instance;
    private final ReentrantLock lock = new ReentrantLock();
    private final IRepository_VotesBooth repository = MRepository.getInstance();

    
        
    private final Random rand = new Random();


    public static IVotesBooth_all getInstance() {
        if (instance == null) {
            instance = new MVotesBooth();
        }
        return instance;
    }

    @Override
    public void vote(char vote){
        lock.lock();
        try{

            repository.vote(vote);

        } finally {
            lock.unlock();
        }

    }
    
    @Override
    public int[] getVotes() {
        lock.lock();
        try {

             return repository.getVotes();

        } finally {
            lock.unlock();
        }
    }

    @Override
    public int getNumVotes() {
        lock.lock();
        try {

            return repository.getNumVotes();

        } finally {
            lock.unlock();
        }
    }
}