package threads;
import exitpoll.IExitPoll_all;
import java.util.ArrayList;
import java.util.Random;
import station.*;
import votesbooth.*;
import exitpoll.*;

public class TVoter extends Thread {

    Random rand = new Random();

    private final static ArrayList<Integer> idList = new ArrayList<>();
    private int id;
    private final int repeatVoter = 50;
    private final IStation_Voter station;
    private final IVotesBooth_Voter votesBooth;
    
    public TVoter (int i, IStation_all station, IVotesBooth_all votesBooth, IExitPoll_all exitPoll){
        this.id = i;
        this.station = station;
        this.votesBooth = votesBooth;
    }

    @Override
    public void run(){
        try{
            while(true){
                station.enterStation();
                votesBooth.vote( 'A');
                station.leaveStation();
                reborn();
            }
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }  



    public void reborn(){
        if (rand.nextInt(0,100) > this.repeatVoter){
            synchronized(idList) {
                while (idList.contains(this.id)) {
                    this.id = idList.get(idList.size() - 1)+1;
                }
                idList.add(this.id);
            }
        }
    }
}
