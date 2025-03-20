package voter;

import java.util.ArrayList;

public class Clerk {

    private int votingLimit;
    private final ArrayList<Integer> idList = new ArrayList<>();

    public Clerk(int votingLimit){
        this.votingLimit = votingLimit;
    }

    public boolean validate(int id){
        if (this.idList.contains(id)){
            return false;
        } else {
            this.idList.add(id);
            return true;
        }
    }

    public void vote(int id, int vote){
        System.out.println("Voter " + id + " voted for " + vote);
    }
}
