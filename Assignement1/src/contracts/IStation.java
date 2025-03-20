package contracts;

public interface IStation {
    
    boolean checkCapacity();
    void enterStation();
    void leaveStation();
    void close();
    int getStatus();
}
