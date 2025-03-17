package voter;

public class ExitPoll {
    private static ExitPoll instance;
    private int votesA = 0;
    private int votesB = 0;

    public void increment(int voteId){
        if (voteId == 0){
            this.votesA += 1;
        } else {
            this.votesB += 1;
        }
    }

    //TODO: Explore if returning both in one function isn't better
    public int getVotesA(){
        return this.votesA;
    }

    public int getVotesB(){
        return this.votesB;
    }

    public static ExitPoll getInstance(){
        if (instance == null){
            instance = new ExitPoll();
        }
        return instance;
    }
}
