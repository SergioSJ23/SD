package threads;
import contracts.IStation;
import java.util.ArrayList;
import java.util.Random;
import monitoring.MStation;
import monitoring.VotingBooth;

public class TVoter extends Thread {

    Random rand = new Random();

    private final static ArrayList<Integer> idList = new ArrayList<>();
    private int id;
    private char vote;
    private final int repeatVoter;
    private final int partyAodds;
    private static int maxVoters;
    
    public TVoter (int i, int repeatVoter, int partyAodds, int max){
        this.id = i;
        this.repeatVoter = repeatVoter;
        this.partyAodds = partyAodds;
        maxVoters = max;
    }

    @Override
    public void run(){

        try{
            TClerk clerk = TClerk.getInstance(0);
            TPollster pollster = TPollster.getInstance(0, 0, 0);
            IStation station = MStation.getInstance(100);
            VotingBooth votingBooth = VotingBooth.getInstance();

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
