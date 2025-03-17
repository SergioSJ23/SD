package voter;

import java.util.concurrent.locks.ReentrantLock;

public class Station {

    private static Station instance;
    private int status = 0;
    private final int capacity;
    private int votersInside = 0;
    private final ReentrantLock lock = new ReentrantLock();


    public Station(int capacity){
        this.capacity = capacity;
    }

    public boolean checkCapacity(){
        return this.votersInside < this.capacity;
    }

    public static Station getInstance(int capacity){
        System.out.println("Station: Checking if station exists");

        if (instance == null){
            System.out.println("Station: Creating new station with capacity " + capacity);
            instance = new Station(capacity);
        }
        return instance;
    }

    public void enterStation(){
        this.votersInside += 1;
        if (this.votersInside >= this.capacity) {
            lock.lock();
        }
        System.out.println(this.votersInside);
    }
    
    public void leaveStation(){
        if (lock.isLocked()){
            try {
                this.votersInside -= 1;
            } finally {
                lock.unlock();
            }
        }
    }

    public void close(){
        this.status = 1;
    }

    public int getStatus(){
        return this.status;
    }
}
