package threads;

import contracts.IStation;
import contracts.IVoteLimit;
import monitoring.MStation;
import monitoring.MVoteLimit;

public class TClerk extends Thread{

    private static TClerk instance;
    private final IVoteLimit voteLimitMonitor;
    IStation station = MStation.getInstance(100);
    
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