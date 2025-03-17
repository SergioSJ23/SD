package voter;

public class VotingBooth {

    private static VotingBooth instance;
    private int votesA;
    private int votesB;

    public VotingBooth(){
        this.votesA = 0;
        this.votesB = 0;
    }

    public void vote(int id, char vote){
        System.out.println("Voter " + id + " voted for " + vote);
        if (vote == 'A'){
            incrementA();
        } else {
            incrementB();
        }
    }

    private void incrementA(){
        this.votesA += 1;
    }

    private void incrementB(){
        this.votesB += 1;
    }

    public int[] getVotes(){
        int[] votes = {votesA, votesB};
        return votes;
    }

    public static VotingBooth getInstance(){
        if (instance == null){
            instance = new VotingBooth();
        }
        return instance;
    }
}