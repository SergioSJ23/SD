package voter;
import java.util.ArrayList;

public class Clerk {

    private static Clerk instance;
    private int votingLimit;
    private final ArrayList<Integer> idList = new ArrayList<>();

    private Clerk(int votingLimit){
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

    public static Clerk getInstance(int votingLimit){
        if (instance == null){
            instance = new Clerk(votingLimit);
        }
        return instance;
    }
}
