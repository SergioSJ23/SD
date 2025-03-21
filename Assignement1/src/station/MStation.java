package station;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MStation implements IStation_all{

    private int status = 0;
    private static int votersInside = 0;
    private final ReentrantLock lock = new ReentrantLock();
    private static int capacity;
    private final ArrayList<Integer> idList = new ArrayList<>();
    private static IStation_all instance;
    private final Condition spaceAvailable = lock.newCondition();

    private MStation(int cap) {
        capacity = cap;
    }

    public static IStation_all getInstance(int capacity) {
        if (instance == null) {
            instance = new MStation(capacity);
        }
        return instance;
    }

    @Override
    public void enterStation(int id) {
        lock.lock();
        try {
            // Bloqueia a thread se a capacidade estiver cheia
            while (votersInside >= capacity) {
                System.out.println("Station is full, voter " + id + " is waiting");
                spaceAvailable.await();
            }
            votersInside++;
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean validateAndAdd(int id) {
        lock.lock();
        try {
            if (idList.contains(id)) {
                return false;
            } else {
                idList.add(id);
                return true;
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void leaveStation(int id) {
        lock.lock();
        try {
            if (votersInside > 0) {
                System.out.println("Voter"+ id +"left the station");
                votersInside--;
                spaceAvailable.signal();
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
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