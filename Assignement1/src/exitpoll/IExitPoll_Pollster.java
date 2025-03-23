package exitpoll;

public interface  IExitPoll_Pollster {
    int getVotesA();
    int getVotesB(); 
    int[] getVotes(); 
    void question() throws InterruptedException; //Pollster
}
