package voter;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Voter extends Thread {

    Random rand = new Random();

    private int id;
    private char vote;
    private int answerPollester;
    private int liePollester;
    private int repeatVoter;
    private int partyAodds;
    private static int maxVoters;
    private int numPersons = 0;
    private ReentrantLock lock = new ReentrantLock();
  
    public Voter (char vote, int answerPollester, int liePollester, int repeatVoter, int partyAodds, int maxVoters){
        this.vote = vote;
        this.id = rand.nextInt(Integer.MAX_VALUE);
        this.answerPollester = answerPollester;
        this.liePollester = liePollester;
        this.repeatVoter = repeatVoter;
        this.partyAodds = partyAodds;
    }

    @Override
    public void run(){
        try{
            Singleton singleton = Singleton.getInstance();
            while(true){
                if(!singleton.station.checkCapacity(numPersons)){
                System.out.println("Voter " + id + " is waiting outside");
                }
                System.out.println("Voter " + id + " is entering the station");
                numPersons += 1;
                Thread.sleep(rand.nextInt(6) + 5);
                if(singleton.clerk.validate(id)){
                    System.out.println("Voter " + id + " is voting");
                    Thread.sleep(new Random().nextInt(5) + 5);
                    if(rand.nextInt(100) < partyAodds){
                        singleton.votingBooth.vote(id, 'A');
                        vote = 'A';
                    }else{
                        singleton.votingBooth.vote(id, 'B');
                        vote = 'B';
                    }     
                    singleton.clerk.vote(id, vote);
                    System.out.println("Voter " + id + " voted");
                }
                else{
                    System.out.println("Voter " + id + " is leaving");
                }
                numPersons -= 1;
                decrement();
                if (maxVoters <= 0){
                    break;
                }
                reborn();
            }
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }  

    public int showId(){
        return id;
    }

    public char getVote(){
        return vote;
    }
}
