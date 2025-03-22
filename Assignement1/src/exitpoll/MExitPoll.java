package exitpoll;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class MExitPoll implements IExitPoll_all{

    private int votesA = 0;
    private int votesB = 0;
    private char vote;
    private int voterId;
    private int lie;
    private final ReentrantLock lock = new ReentrantLock();
    private static IExitPoll_all instance;
    private final java.util.concurrent.locks.Condition willAnswer = lock.newCondition();
    private final Random rand = new Random();

    public static IExitPoll_all getInstance() {
        if (instance == null) {
            instance = new MExitPoll();
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

    @Override
    public void inquire(int id, char voteVoter) {
        lock.lock();
        if(rand.nextInt(100) > 50){
            try {
                voterId = id;
                vote = voteVoter;
                willAnswer.signalAll();
            } finally {
                lock.unlock();
            }
        }
        else{
            lock.unlock();
        }
    }

    @Override
    public void answer() throws InterruptedException {
        try {
            lock.lock();
            willAnswer.await();
            increment(vote == 'A' ? 0 : 1);
            if(rand.nextInt(100) > 50){
                lie++;
            }
            if(lie > 0){
                if(vote == 'A'){
                    System.out.println("Voter " + voterId + " lied about voting for party B");
                }
                else{
                    System.out.println("Voter " + voterId + " lied about voting for party A");
                }
            }
            else{
                System.out.println("Voter " + voterId + " told the truth and voter for party " + vote);
            }
                
        } finally {
            lock.unlock();
        }


    }
}