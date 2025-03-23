package repository;

public interface IRepository_VotesBooth {
    
    int[] getVotes();
    int getNumVotes();
    void vote(char vote);
    
}
