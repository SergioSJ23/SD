package threads;

import exitpoll.IExitPoll_Pollster;


public class TPollster extends Thread{

    private static TPollster instance;
    private final IExitPoll_Pollster exitPoll;



    private TPollster(IExitPoll_Pollster exitPoll) {
        this.exitPoll = exitPoll;
        
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                exitPoll.inquire();
            }
        } catch (InterruptedException e) { 
            System.out.println("Pollster interrupted");
        }
    }

    public static TPollster getInstance(IExitPoll_Pollster exitpoll) {
        if (instance == null) {
           instance = new TPollster(exitpoll);
        }
  
        return instance;
     }
}
