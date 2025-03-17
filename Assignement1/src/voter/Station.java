package voter;

public class Station {

    private static Station instance;
    private int status = 0;
    private final int capacity;
    private int numVotes = 0;
    private int votersInside = 0;

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
    }
    
    public void leaveStation(){
        this.votersInside -= 1;
    }

    public void close(){
        this.status = 1;
    }

    public int getStatus(){
        return this.status;
    }
}
