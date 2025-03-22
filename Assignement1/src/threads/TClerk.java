package threads;

import station.IStation_Clerk;
import station.MStation;
import votesbooth.IVotesBooth_Clerk;


public class TClerk extends Thread{

    private static TClerk instance;
    private final IVotesBooth_Clerk votesBooth;
    IStation_Clerk station = MStation.getInstance(100);
    
    
    private TClerk( IVotesBooth_Clerk votesBooth) {
        this.votesBooth = votesBooth;
    }

    @Override
    public void run() {
        try {
            while (votesBooth.isVotingComplete() || !station.isStationEmpty()) {
                System.out.println("Clerk is validating and adding votes");
                station.validateAndAdd();
            }
        } catch (Exception e) { 
            e.printStackTrace();
        }
        System.out.println("Clerk is done");
    }
    
    public static TClerk getInstance( IVotesBooth_Clerk votesBooth) {
        if (instance == null) {
            instance = new TClerk(votesBooth);
        }
        return instance;
    }
}