package station;

import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import repository.IRepository_Station;

import gui.VoterObserver;
import repository.IRepository_VotesBooth;
import repository.MRepository;

public class MStation implements IStation_all {

    private boolean closen = true;
    private static boolean electionDayEnded = false;
    private static int limitVotes = 50;
    private final ReentrantLock lock = new ReentrantLock();
    private final HashSet<Integer> idSet = new HashSet<>();
    private static MStation instance;
    private final Condition voterCondition = lock.newCondition();
    private final Condition clerkCondition = lock.newCondition();
    private final Condition statusCondition = lock.newCondition();
    private final IRepository_Station repository = MRepository.getInstance();

    private final BlockingQueue<Integer> queue;
    private final Random rand = new Random();

    private boolean isIdValid = false;  // Flag to notify voter if ID is validated
    private boolean clerkReady = false; // Flag to prevent deadlock by tracking if clerk is waiting

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
    public void openStation() {
        lock.lock(); // Acquire the lock
        try {
            closen = false;
            repository.Sopen();
            statusCondition.signalAll(); // Signal all waiting threads
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    @Override
    public void enterStation(int id) {
        lock.lock();
        try {
            while (closen && !electionDayEnded) {
                try {
                    statusCondition.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        } finally {
            lock.unlock();
        }
        if (electionDayEnded) {
            Thread.currentThread().interrupt();
            return;
        }

        try {
            queue.put(id);
            System.out.println("Voter " + id + " entered the station.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public boolean present(int id) {
        while (id != queue.peek()) {
        }

        lock.lock();
        try {
            System.out.println("Voter " + id + " has presented their ID.");
            clerkReady = true; // Notify that the clerk should proceed
            clerkCondition.signalAll();
            voterCondition.await(); // Wait until validation is complete
            return this.isIdValid;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void validateAndAdd() throws InterruptedException {
        lock.lock();
        try {
            while (!clerkReady) {
                clerkCondition.await();
            }

            int id = queue.peek();
            System.out.println("Validating voter " + id);
            Thread.sleep(rand.nextInt(6) + 5);  // Simulate validation time
            if (idSet.contains(id)) {
                System.out.println("Voter " + id + " rejected (duplicate ID).\n");
                this.isIdValid = false;  // Mark as invalid
            } else {
                idSet.add(id);
                System.out.println("Voter " + id + " validated and added to the list.");
                this.isIdValid = true;  // Mark as valid
                limitVotes -= 1;
                repository.SaddId(id);
            }
            clerkReady = false; // Reset clerk readiness for the next voter
            voterCondition.signalAll(); // Notify the voter to continue
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean lastVotes() {
        lock.lock();
        try {
            while (!clerkReady) {
                clerkCondition.await();
            }

            int id = queue.peek();
            System.out.println("Validating voter " + id);
            Thread.sleep(rand.nextInt(6) + 5);  // Simulate validation time
            if (idSet.contains(id)) {
                System.out.println("Voter " + id + " rejected (duplicate ID).\n");
                this.isIdValid = false;  // Mark as invalid
            } else {
                idSet.add(id);
                System.out.println("Voter " + id + " validated and added to the list.");
                this.isIdValid = true;  // Mark as valid
                limitVotes -= 1;
                repository.SaddId(id);
            }
            if (queue.size() <= 0) {
                return false;
            }
            clerkReady = false; // Reset clerk readiness for the next voter
            voterCondition.signalAll(); // Notify the voter to continue
            return true;
        } catch (InterruptedException e) {
        } finally {
            lock.unlock();
        }
        return false;
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
        closen = true;
        repository.Sclose();
        lock.lock();
        try {
            voterCondition.signalAll();
            clerkCondition.signalAll();
        } finally {
            lock.unlock();
        }
        System.out.println("Station is closing. All waiting threads are notified.");
    }

    @Override
    public boolean countVotes() {
        return limitVotes <= 0;
    }

    @Override
    public void announceEnding(){
        electionDayEnded = true;
        repository.SannounceEnding();
    }
}