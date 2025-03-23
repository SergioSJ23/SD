package repository;


import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;


public class MRepository implements IRepository_all{
    
    private static MRepository instance;
    private final ReentrantLock lock = new ReentrantLock();
    private int numVotes = 0;
    private int votesA = 0;
    private int votesB = 0;
    private Random rand = new Random();


    private MRepository() {
    }

    public static MRepository getInstance() {
        if (instance == null) {
            instance = new MRepository();
        }
        return instance;
    }

    @Override
    public int[] getVotes() {
        lock.lock();
        try {
            return new int[]{votesA, votesB};
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int getNumVotes() {
        lock.lock();
        try {
            return numVotes;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void vote(char vote){
        lock.lock();
        
        try {
            try{
            Thread.sleep(rand.nextInt(16));
            if(vote == 'A'){
                incrementA();
            }else{
                incrementB();
            }
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
    }

    private void incrementB() {
        votesB++;
        numVotes++;
    }


    
}
