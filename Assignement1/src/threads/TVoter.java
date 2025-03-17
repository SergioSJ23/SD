package threads;
import java.util.ArrayList;
import java.util.Random;
import monitoring.IClerk;
import monitoring.IPollster;
import monitoring.IStation;
import monitoring.IVotingBooth;

public class TVoter extends Thread {

    Random rand = new Random();

    private final static ArrayList<Integer> idList = new ArrayList<>();
    private int id;
    private char vote;
    private final int repeatVoter;
    private final int partyAodds;
    private static int maxVoters;
    
    public TVoter (int repeatVoter, int partyAodds, int max){
        this.id = rand.nextInt(Integer.MAX_VALUE);
        this.repeatVoter = repeatVoter;
        this.partyAodds = partyAodds;
        maxVoters = max;
    }

    @Override
    public void run(){

        try{
            IClerk clerk = IClerk.getInstance(0);
            IPollster pollster = IPollster.getInstance(0, 0, 0);
            IStation station = IStation.getInstance(0);
            IVotingBooth votingBooth = IVotingBooth.getInstance();

            while(true){

                if (maxVoters <= 0){
                    break;
                }

                if(!station.checkCapacity()){
                System.out.println("Voter " + this.id + " is waiting outside");
                }
                station.enterStation();
                System.out.println("Voter " + this.id + " is entering the station");

                Thread.sleep(rand.nextInt(6) + 5);
                if(clerk.validate(this.id)){
                    decrement();
                    System.out.println("Voter " + this.id + " is voting");
                    Thread.sleep(new Random().nextInt(5) + 5);
                    if(rand.nextInt(100) < this.partyAodds){
                        votingBooth.vote(this.id, 'A');
                        this.vote = 'A';
                    }else{
                        votingBooth.vote(this.id, 'B');
                        this.vote = 'B';
                    }
                    System.out.println("Voter " + this.id + " is leaving the Station");
                }
                else{
                    System.out.println("Voter " + this.id + " was kicked out");
                }
                station.leaveStation();
                pollster.inquire(this);
                reborn();
            }
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }  

    public int showId(){
        return this.id;
    }

    public char getVote(){
        return this.vote;
    }

    public void reborn(){
        if (rand.nextInt(0,100) > this.repeatVoter){
            synchronized(idList) {
                while (idList.contains(this.id)) {
                    this.id = rand.nextInt(Integer.MAX_VALUE);
                }
                idList.add(this.id);
            }
        }
    }

    public void decrement(){
        maxVoters -= 1;
    }
}
