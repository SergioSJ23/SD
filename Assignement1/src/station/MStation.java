package station;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class MStation implements IStation_all{

    private int status = 0;
    private static int votersInside = 0;
    private final ReentrantLock lock = new ReentrantLock();
    private static int capacity;
    private final ArrayList<Integer> idList = new ArrayList<>();

    private MStation(int cap) {
        capacity = cap;
    }

    public static IStation_all getInstance(int capacity) {
        return new MStation(capacity); // Retorna a interface IStation
    }

    @Override
    public void enterStation() {
        lock.lock();
        try {
            // Bloqueia a thread se a capacidade estiver cheia
            while (votersInside >= capacity) {
                //TODO: ADD WAIT LOGIC OR SOMETHING HERE
            }
            votersInside++;
        } catch (InterruptedException e) {
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
    public void leaveStation() {
        lock.lock();
        try {
            if (votersInside > 0) {
                votersInside--;
                //spaceAvailable.signal(); Checka ai isso sergio
            }
        } catch (InterruptedException e) {
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