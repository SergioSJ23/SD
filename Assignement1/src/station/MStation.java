package station;

import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MStation implements IStation_all {

    private int status = 0;
    private final ReentrantLock lock = new ReentrantLock();
    private final HashSet<Integer> idSet = new HashSet<>();
    private static MStation instance;
    private final Condition voterCondition = lock.newCondition();
    private final Condition clerkCondition = lock.newCondition();
    private final BlockingQueue<Integer> queue;

    private final BlockingQueue<Integer> validationQueue = new LinkedBlockingQueue<>();
    private boolean isIdValid = false;  // Flag to notify voter if ID is validated

    private MStation(int capacity) {
        this.queue = new LinkedBlockingQueue<>(capacity);
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
    public boolean present(int id) {
        lock.lock();
        try {
            validationQueue.put(id);
            System.out.println("Voter " + id + " has presented their ID.");
            clerkCondition.signal();
            while (validationQueue.contains(id)) {
                voterCondition.await();  // Wait until validation is complete
            }
            return this.isIdValid;
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
            while (validationQueue.isEmpty()) {
                clerkCondition.await();
            }
            int id = validationQueue.take();
            if (idSet.contains(id)) {

                System.out.println("Voter " + id + " rejected (duplicate ID).");
                this.isIdValid = false;  // Mark as invalid
            } else {
                idSet.add(id);
                System.out.println("Voter " + id + " validated and added to the list.");
                this.isIdValid = true;  // Mark as valid
            }

            voterCondition.signal();
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
