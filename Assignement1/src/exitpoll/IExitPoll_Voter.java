package exitpoll;

public interface IExitPoll_Voter {
    void enterExitPoll(char vote, int voterId);
    void leaveExitPoll(int voterId);
}
