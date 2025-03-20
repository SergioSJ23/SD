package votes;

public interface IVotes {
    
    void increment(int voteId); //Voter
    int getVotesA(); //Clerk
    int getVotesB(); //Clerk
    int[] getVotes(); //Clerk
    
}
