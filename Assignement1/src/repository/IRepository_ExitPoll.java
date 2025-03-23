package repository;

public interface IRepository_ExitPoll {

    void incrementExit(int voteId);

    int getVotesAExit();

    int getVotesBExit();

    int[] getExitVotes();

    void stationIsClosed();

    void enterExitPoll(char vote);

    void inquire() throws InterruptedException;

    void question() throws InterruptedException;
    
}
