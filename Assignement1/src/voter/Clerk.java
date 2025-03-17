package voter;
import java.util.ArrayList;

public class Clerk {

    private static Clerk instance;
    private int votingLimit;
    private int numVotes = 0;
    private final ArrayList<Integer> idList = new ArrayList<>();
    Station station = Station.getInstance(2);

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

    public void close(){
        this.numVotes += 1;
        if(this.numVotes >= this.votingLimit){
            station.close();
        }
    }

    public static Clerk getInstance(int votingLimit){
        if (instance == null){
            instance = new Clerk(votingLimit);
        }
        return instance;
    }
}
