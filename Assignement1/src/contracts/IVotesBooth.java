package contracts;

public interface IVotesBooth {

    void vote(char vote);
    void incrementA();
    void incrementB();
    int[] getVotes();
    
}
