package station;

import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MStation implements IStation_all {

    private int status = 0;
    private final int maxVotes = 50;
    private final ReentrantLock lock = new ReentrantLock();
    private final HashSet<Integer> idSet = new HashSet<>();
    private static MStation instance;
    private final Condition voterCondition = lock.newCondition();
    private final Condition clerkCondition = lock.newCondition();
    private final BlockingQueue<Integer> queue;
    private final Random rand = new Random();
    
    private final BlockingQueue<Integer> validationQueue = new LinkedBlockingQueue<>();
    private boolean isIdValid = false;  // Flag para notificar se o ID foi validado

    // Flag de shutdown para sinalizar o encerramento
    private volatile boolean shutdown = false;

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
    public boolean isStationEmpty() {
        return queue.isEmpty();
    }

    @Override
    public boolean present(int id) {
        lock.lock();
        try {
            // Se já foi solicitado shutdown, não prossegue
            if (shutdown) {
                return false;
            }
            validationQueue.put(id);
            System.out.println("Voter " + id + " has presented their ID.");
            clerkCondition.signal(); // Acorda a thread do clerk
            // Enquanto o votante estiver na fila de validação...
            while (validationQueue.contains(id)) {
                // Se shutdown foi solicitado, sai do laço
                if (shutdown) {
                    break;
                }
                // Usa awaitUninterruptibly se não quiser que interrupções afetem o fluxo,
                // ou await() se preferir tratar InterruptedException.
                voterCondition.await();
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
    public void validateAndAdd() throws InterruptedException {
        lock.lock();
        try {
            while (validationQueue.isEmpty()) {
                clerkCondition.await();
            }
            Thread.sleep(rand.nextInt(6)+5);  // Simulate validation time
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

    /**
     * Método para sinalizar o encerramento do monitor.
     * Define a flag shutdown e chama signalAll() para liberar as threads que estejam aguardando.
     */
    @Override
    public void close() {
        lock.lock();
        try {
            shutdown = true;
            voterCondition.signalAll();
            clerkCondition.signalAll();
            System.out.println("Station is closing. All waiting threads are notified.");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int getStatus() {
        return this.status;
    }
}
