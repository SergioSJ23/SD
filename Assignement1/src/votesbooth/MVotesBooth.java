package votesbooth;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
import repository.IRepository_VotesBooth;
import repository.MRepository;

public class MVotesBooth implements IVotesBooth_all{

    private static int numVotes = 0;
    private static int votesA = 0;
    private static int votesB = 0;
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
    public void vote(char vote, int voterId) {
        lock.lock();
        try {
            try{
            Thread.sleep(rand.nextInt(16));
            if(vote == 'A'){
                incrementA();
            }else{
                incrementB();
            }
            repository.VBvote(vote, voterId);

        } finally {
            lock.unlock();
        }
        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void incrementA() {
        votesA++;
        numVotes++;
        repository.VBincrementA();
    }

    private void incrementB() {
        votesB++;
        numVotes++;
        repository.VBincrementB();
    }
    
    @Override
    public int[] getVotes() {
        lock.lock();
        try {
            repository.VBgetVotes(votesA, votesB);
            return new int[]{votesA, votesB};
        } finally {
            lock.unlock();
        }
    }
}