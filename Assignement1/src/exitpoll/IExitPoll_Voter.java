package exitpoll;

public interface IExitPoll_Voter {
    void increment(int voteId); //Voter
    void enterExitPoll(char vote);
}
