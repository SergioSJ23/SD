package exitpoll;

public interface IExitPoll_Voter {
    void increment(int voteId); //Voter
    void inquire(int id, char voteVoter); //Voter
}
