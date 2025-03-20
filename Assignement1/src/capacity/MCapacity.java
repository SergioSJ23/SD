package capacity;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MCapacity implements ICapacity {

    private final int capacity;
    private static ICapacity instance;
    private int votersInside = 0;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition spaceAvailable = lock.newCondition(); // Condição para espera

    public MCapacity(int capacity) {
        this.capacity = capacity;
    }

    public static ICapacity getInstance(int capacity) {
        if (instance == null) {
            instance = new MCapacity(capacity);
        }
        return instance;
    }




    @Override
    public void enter() {
        lock.lock();
        try {
            // Bloqueia a thread se a capacidade estiver cheia
            while (votersInside >= capacity) {
                spaceAvailable.await(); // Espera até receber um sinal de espaço disponível
            }
            votersInside++;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void leave() {
        lock.lock();
        try {
            if (votersInside > 0) {
                votersInside--;
                spaceAvailable.signal(); // Notifica uma thread em espera
            }
        } finally {
            lock.unlock();
        }
    }

    // Métodos auxiliares (mantidos para compatibilidade com a interface)
    @Override
    public boolean canEnter() {
        lock.lock();
        try {
            return votersInside < capacity;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean isFull() {
        lock.lock();
        try {
            return votersInside >= capacity;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int getVotersInside() {
        lock.lock();
        try {
            return votersInside;
        } finally {
            lock.unlock();
        }
    }
}