package voter;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



public class Voter extends Thread {

    Random rand = new Random();

    private int id;
    private int vote;
    private int answerPollester;
    private int liePollester;
    private int repeatVoter;
    private int numPersons = 0;
    private ReentrantLock lock = new ReentrantLock();

    public Voter (int id, int vote, int answerPollester, int liePollester, int repeatVoter) {
        this.id = id;
        this.vote = vote;
        this.answerPollester = answerPollester;
        this.liePollester = liePollester;
        this.repeatVoter = repeatVoter;

    }

    @Override
    public void run(){
        try{
            Singleton singleton = Singleton.getInstance();
            numPersons += 1;
            if(!singleton.station.checkCapacity(numPersons)){
                System.out.println("Voter " + id + " is waiting outside");
            }
            else{
                System.out.println("Voter " + id + " is entering the station");
            }

            Thread.sleep(rand.nextInt(5) + 5);
            if(singleton.clerk.validate(id)){
                System.out.println("Voter " + id + " is voting");
                Thread.sleep(rand.nextInt(5) + 5);
                singleton.clerk.vote(id, vote);
                System.out.println("Voter " + id + " voted");
            }
            else{

                System.out.println("Voter " + id + " is leaving");
                numPersons -= 1;
                return;
            }
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    
}
