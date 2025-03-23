package repository;

public interface IRepository_VotesBooth {
    
    void VBincrementA();
    void VBincrementB();
    void VBvote(char vote, int id);
    void VBgetVotes(int votesA, int votesB);
    
}
