package exitpoll;

public interface  IExitPoll_Pollster {
    int getVotesA();
    int getVotesB(); 
    int[] getVotes(); 
    void inquire(int id, char voteVoter); //Pollster
    void answer(); //Pollster
}
