package contracts;

public interface IVotes {
    
    void increment(int voteId);
    int getVotesA();
    int getVotesB();
    int[] getVotes();
    
}
