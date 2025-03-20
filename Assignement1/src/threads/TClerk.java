package threads;

import station.IStation_all;
import station.MStation;
import votelimit.IVoteLimit_all;
import votelimit.MVoteLimit;

public class TClerk extends Thread{

    private static TClerk instance;
    IStation_all station = MStation.getInstance(100);
    
    public boolean validate(int id) {
        boolean isValid = voteLimitMonitor.validateAndAdd(id);
        if (isValid) {
            station.close();
        }
        return isValid;
    }

    public static TClerk getInstance(){
        if (instance == null) {
            instance = new TClerk();
        }
        return instance;
    }
}