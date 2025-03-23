package exitpoll;

public interface  IExitPoll_Pollster {
    int[] getVotes(); 
    void inquire() throws InterruptedException;
}
