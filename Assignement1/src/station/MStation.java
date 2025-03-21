package station;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MStation implements IStation_all{

    private int status = 0;
    private static boolean isIdReady = false;
    private static boolean isIdValid;
    private static int idValidation;
    private static int votersInside = 0;
    private final ReentrantLock lock = new ReentrantLock();
    private final ArrayList<Integer> idList = new ArrayList<>();
    private static IStation_all instance;
    private final Condition spaceAvailable = lock.newCondition();
    private final Condition validateVoter = lock.newCondition();
    private final Condition validateClerk = lock.newCondition();
    private static BlockingQueue<Integer> queue;


    private MStation(int capacity) {
        queue = new LinkedBlockingQueue<>(capacity);
        
    }

    public static IStation_all getInstance(int capacity) {
        if (instance == null) {
            instance = new MStation(capacity);
        }
        return instance;
    }

    @Override
    public void enterStation(int id) {
        try {
            queue.put(id);
            System.out.println("Voter " + id + " entered the station.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public boolean validate(int id) {
        lock.lock();
        try {
            idValidation = id; 
            isIdReady = true; 
            validateVoter.signal(); 

            while (isIdReady ){
                validateClerk.await();
            }

            return isIdValid; 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void validateAndAdd() {
        lock.lock();
        try {
            while (!isIdReady) {
                validateVoter.await();
            }

            if (idList.contains(idValidation)) {
                System.out.println("Voter " + idValidation + " rejected (duplicate ID)");
                isIdValid = false; 
            } else {
                idList.add(idValidation); 
                System.out.println("Voter " + idValidation + " validated and added to the list");
                isIdValid = true; 
            }

            isIdReady = false; 
            validateClerk.signal();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void leaveStation(int id) {
        try {
            if (queue.remove(id)) {
                System.out.println("Voter " + id + " left the station.");
            } else {
                System.out.println("Voter " + id + " not found in the queue.");
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void close() {
        this.status = 1;
    }

    @Override
    public int getStatus() {
        return this.status;
    }

}