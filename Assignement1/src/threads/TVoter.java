package threads;
import exitpoll.IExitPoll_Voter;
import java.util.ArrayList;
import java.util.Random;
import station.IStation_Voter;
import votesbooth.IVotesBooth_Voter;


public class TVoter extends Thread {

    Random rand = new Random();

    private final static ArrayList<Integer> idList = new ArrayList<>();
    private int id;
    private static char voteVoter;
    private final int repeatVoter = 50;
    private final IStation_Voter station;
    private final IVotesBooth_Voter votesBooth;
    private final IExitPoll_Voter exitPoll;
    
    public TVoter (int i, IStation_Voter station, IVotesBooth_Voter votesBooth, IExitPoll_Voter exitPoll){
        this.id = i;
        this.station = station;
        this.votesBooth = votesBooth;
        this.exitPoll = exitPoll;
    }

    @Override
    public void run() {

        try {
            while (true) {
                if (rand.nextInt(0,100) > 50){
                    voteVoter = 'A';
                } else {
                    voteVoter = 'B';
                }
                station.enterStation(id);
                votesBooth.vote();
                station.leaveStation(id);
                exitPoll.inquire(id, voteVoter);
                reborn();
            }
        } catch (Exception e) { 
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
