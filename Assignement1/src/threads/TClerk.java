package threads;

import station.IStation_all;
import station.MStation;
import votelimit.IVoteLimit;
import votelimit.MVoteLimit;

public class TClerk extends Thread{

    private static TClerk instance;
    private final IVoteLimit voteLimitMonitor;
    IStation_all station = MStation.getInstance(100);
    
    private TClerk(int votingLimit) {
        this.voteLimitMonitor = new MVoteLimit(votingLimit);
    }

    public boolean validate(int id) {
        boolean isValid = voteLimitMonitor.validateAndAdd(id);
        if (isValid) {
            station.close();
        }
        return isValid;
    }

    public static TClerk getInstance(int votingLimit){
        if (instance == null) {
            instance = new TClerk(votingLimit);
        }
        return instance;
    }
}