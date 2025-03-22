package threads;

import exitpoll.IExitPoll_Pollster;
import station.IStation_Pollster;
import votesbooth.IVotesBooth_Pollster;

public class TPollster extends Thread{

    private static TPollster instance;
    private final IExitPoll_Pollster exitPoll;
    private final IVotesBooth_Pollster votesBooth;
    private final IStation_Pollster station;


    private TPollster(IExitPoll_Pollster exitPoll, IVotesBooth_Pollster votesBooth, IStation_Pollster station) {
        this.exitPoll = exitPoll;
        this.votesBooth = votesBooth;
        this.station = station;
    }
    
    @Override
    public void run() {
        try {
            while (votesBooth.isVotingComplete() || !station.isStationEmpty()) {
                System.out.println("Pollster is answering exit poll");
                exitPoll.answer();
            }
        } catch (Exception e) { 
            e.printStackTrace();
        }
        System.out.println("Pollster is done");

    }

    public static TPollster getInstance(IExitPoll_Pollster exitpoll, IVotesBooth_Pollster votesBooth, IStation_Pollster station) {
        if (instance == null) {
           instance = new TPollster(exitpoll, votesBooth, station);
        }
  
        return instance;
     }
}
