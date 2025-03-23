package exitpoll;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;


public class MExitPoll implements IExitPoll_all{

    private int votesA = 0;
    private int votesB = 0;
    private char vote;
    private int voterId;
    private final int lie = 20;
    private final int noResponse = 60;
    private final int approached = 10;
    private final ReentrantLock lock = new ReentrantLock();
    private static IExitPoll_all instance;
    private final java.util.concurrent.locks.Condition willAnswer = lock.newCondition();
    private final java.util.concurrent.locks.Condition hasAnswered = lock.newCondition();
    private final Random rand = new Random();
    private static boolean isClosed = false;

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
    public void stationIsClosed() {
        lock.lock();
        try {
            isClosed = true;
            System.out.println("Exit poll knows the station is closed");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void inquire(int id, char voteVoter) {
        if (isClosed){
            Thread.currentThread().interrupt();
        }
        lock.lock();
        if(rand.nextInt(100) < approached){ // 10% chance of being approached
            try {
                voterId = id;
                vote = voteVoter;
                System.out.println("Voter " + voterId + " was approached by the exit poll with vote " + vote);
                willAnswer.signalAll();
                hasAnswered.await();
                System.out.println("Voter " + voterId + " has answered the exit poll");
            
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
        else{
            lock.unlock();
        }
    }

    @Override
    public void question() throws InterruptedException {
        try {
            lock.lock();
            willAnswer.await();
            increment(vote == 'A' ? 0 : 1);
            if(rand.nextInt(100) > noResponse){ // 60% chance of not responding
                if(rand.nextInt(100) < lie){ // 20% chance of lying
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
            }
            hasAnswered.signalAll();
                
        } finally {
            lock.unlock();
        }


    }
}