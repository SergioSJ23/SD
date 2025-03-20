package capacity;

public interface ICapacity {
    
    boolean canEnter(); //Station
    void enter(); //Voter & Station
    void leave(); //Voter & Station
    boolean isFull(); //Station
    int getVotersInside(); //Station
    
}
