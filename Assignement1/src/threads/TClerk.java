package threads;

import station.IStation_Clerk;
import station.MStation;

public class TClerk extends Thread{

    private static TClerk instance;
    IStation_Clerk station = MStation.getInstance(100);
    
    
    private TClerk() {
        
    }

    @Override
    public void run() {
        try {
            while (true) {
                station.validateAndAdd();
            }
        } catch (InterruptedException e) { 
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