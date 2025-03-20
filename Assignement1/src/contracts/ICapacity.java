package contracts;

public interface ICapacity {
    
    boolean canEnter();
    void enter();
    void leave();
    boolean isFull();
    int getVotersInside();
    
}
