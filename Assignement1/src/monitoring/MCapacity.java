package monitoring;

import java.util.concurrent.locks.ReentrantLock;

public class MCapacity {

    private final int capacity;
    private int votersInside = 0;
    private final ReentrantLock lock = new ReentrantLock();

    public MCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean canEnter() {
        lock.lock();
        try {
            return votersInside < capacity;
        } finally {
            lock.unlock();
        }
    }

    public void enter() {
        lock.lock();
        try {
            if (votersInside < capacity) {
                votersInside++;
            }
        } finally {
            lock.unlock();
        }
    }

    public void leave() {
        lock.lock();
        try {
            if (votersInside > 0) {
                votersInside--;
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean isFull() {
        lock.lock();
        try {
            return votersInside >= capacity;
        } finally {
            lock.unlock();
        }
    }

    public int getVotersInside(){
        return votersInside;
    }
}