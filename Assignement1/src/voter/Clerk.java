package voter;

public class Clerk {

    private int votingLimit;

    public Clerk(int votingLimit){
        this.votingLimit = votingLimit;

    }

    public boolean validate(int id){
        return true;
    }    

    public void vote(int id, char vote){
        System.out.println("Voter " + id + " voted for " + vote);
    }
}
