package threads;

import exitpoll.IExitPoll_Clerk;
import exitpoll.MExitPoll;
import station.IStation_Clerk;
import station.MStation;

public class TClerk extends Thread{

    private static TClerk instance;
    private boolean limitReached;
    IStation_Clerk station = MStation.getInstance(100);
    IExitPoll_Clerk exitPoll = MExitPoll.getInstance();
    
    
    private TClerk() {
        
    }

    @Override
    public void run() {
        try {
            station.openStation();
            while (true) {
                station.validateAndAdd();
                limitReached = station.countVotes();
                if (limitReached){
                    station.announceEnding();
                    station.close();
                    Thread.currentThread().interrupt(); // Restore the interrupt status
                }
            }
        } catch (InterruptedException e) {
            while (station.lastVotes()){}
            exitPoll.stationIsClosed();
            System.out.println("Clerk interrupted");
        }
        System.out.println("Clerk is done");
    }
    
    public static TClerk getInstance() {
        if (instance == null) {
            instance = new TClerk();
        }
        return instance;
    }
}