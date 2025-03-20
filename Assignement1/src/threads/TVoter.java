package threads;
import java.util.ArrayList;
import java.util.Random;
import station.IStation_all;
import station.MStation;
import votesbooth.IVotesBooth_all;
import votesbooth.MVotesBooth;

public class TVoter extends Thread {

    Random rand = new Random();

    private final static ArrayList<Integer> idList = new ArrayList<>();
    private int id;
    private char vote;
    private final int repeatVoter = 50;
    private final int partyAodds = 50;
    private static int maxVoters = 10;
    
    public TVoter (int i){
        this.id = i;
    }

    @Override
    public void run(){

        try{
            TClerk clerk = TClerk.getInstance(0);
            TPollster pollster = TPollster.getInstance(0, 0, 0);
            IStation_all station = MStation.getInstance(100);
            IVotesBooth_all votingBooth = MVotesBooth.getInstance();

            while(maxVoters > 0){

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
                        votingBooth.vote( 'A');
                        this.vote = 'A';
                    }else{
                        votingBooth.vote( 'B');
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
                    this.id = idList.get(idList.size() - 1)+1;
                }
                idList.add(this.id);
            }
        }
    }

    public void decrement(){
        maxVoters -= 1;
    }
}
