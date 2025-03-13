package voter;

public class VotingBooth {

    private int votesA;
    private int votesB;

    public VotingBooth(){
        votesA = 0;
        votesB = 0;
    }

    public void vote(int id, char vote){
        System.out.println("Voter " + id + " voted for " + vote);
    }

    public int[] getVotes(){
        int[] votes = {votesA, votesB};
        return votes;
    }
}