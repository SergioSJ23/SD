package station;

import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import repository.IRepository_Station;
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
        lock.lock();
        try {
            closen = false;
            repository.Sopen();
             
            statusCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void enterStation(int id) throws InterruptedException {
        lock.lock();
        try {
            System.out.println("AAAAAAAAAAAA");
            while (closen && !electionDayEnded) {
                repository.Swait(id);
                statusCondition.await();
            }
        } finally {
            lock.unlock();
        }
        if (electionDayEnded) {
            Thread.currentThread().interrupt();
            return;
        }

        queue.put(id);
        System.out.println("BBBBBBBBBBBB");
        repository.Senter(id);
        
    }

    @Override
    public boolean present(int id) {
        while (id != queue.peek()) {}

        lock.lock();
        try {
            
            repository.Spresent(id);
            
            clerkReady = true;
            clerkCondition.signalAll();
            voterCondition.await();
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
            Thread.sleep(rand.nextInt(6) + 5);  // Simulate validation time
            if (idSet.contains(id)) {
                repository.Srejected(id);
                this.isIdValid = false;  // Mark as invalid
            } else {
                idSet.add(id);
                repository.Svalidated(id);
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
            Thread.sleep(rand.nextInt(6) + 5);  // Simulate validation time
            if (idSet.contains(id)) {
                repository.Srejected(id);
                this.isIdValid = false;  // Mark as invalid
            } else {
                idSet.add(id);
                repository.Svalidated(id);
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

            queue.remove(id);
            repository.Sleave(id);
            
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void close() {
        lock.lock();
        try {
            closen = true;
            repository.Sclose();
            
            voterCondition.signalAll();
            clerkCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean countVotes() {
        System.out.println(limitVotes);
        return limitVotes <= 0;
    }

    @Override
    public void announceEnding(){
        electionDayEnded = true;
        repository.SannounceEnding();
    }
}