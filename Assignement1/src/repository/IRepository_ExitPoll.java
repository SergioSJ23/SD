package repository;

public interface IRepository_ExitPoll {

    void incrementExit(int voteId);

    int[] getExitVotes();

    void stationIsClosed();

    void enterExitPoll(char vote);

    void inquire() throws InterruptedException;

}
