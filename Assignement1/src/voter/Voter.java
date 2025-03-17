package voter;
import java.util.ArrayList;
import java.util.Random;

public class Voter extends Thread {

    Random rand = new Random();

    private final static ArrayList<Integer> idList = new ArrayList<>();
    private int id;
    private char vote;
    private int answerPollester;
    private int liePollester;
    private int repeatVoter;
    private final int partyAodds;
    private static int maxVoters;
    private int numPersons = 0;
    private final Object lock = new Object();
    
    public Voter (int answerPollester, int liePollester, int repeatVoter, int partyAodds, int max){
        this.id = rand.nextInt(Integer.MAX_VALUE);
        this.answerPollester = answerPollester;
        this.liePollester = liePollester;
        this.repeatVoter = repeatVoter;
        this.partyAodds = partyAodds;
        maxVoters = max;
    }

    @Override
    public void run(){

        synchronized (this.lock){
        try{
            Clerk clerk = Clerk.getInstance(0);
            Pollster pollster = Pollster.getInstance(0, 0, 0);
            Station station = Station.getInstance(0, 0, 0);
            VotingBooth votingBooth = VotingBooth.getInstance();

            while(true){
                if(!station.checkCapacity(this.numPersons)){
                System.out.println("Voter " + this.id + " is waiting outside");
                lock.wait();
                }
                System.out.println("Voter " + this.id + " is entering the station");
                this.numPersons += 1;
                Thread.sleep(rand.nextInt(6) + 5);
                if(clerk.validate(this.id)){
                    decrement();
                    System.out.println(maxVoters);
                    System.out.println("Voter " + this.id + " is voting");
                    Thread.sleep(new Random().nextInt(5) + 5);
                    if(rand.nextInt(100) < this.partyAodds){
                        votingBooth.vote(this.id, 'A');
                        this.vote = 'A';
                    }else{
                        votingBooth.vote(this.id, 'B');
                        this.vote = 'B';
                    }
                }
                else{
                    System.out.println("Voter " + this.id + " is leaving");
                }
                this.numPersons -= 1;
                lock.notify();
                pollster.inquire(this);
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
    }  

    public int showId(){
        return this.id;
    }

    public char getVote(){
        return this.vote;
    }

    public void reborn(){
        if (rand.nextInt(0,100) < this.repeatVoter){
            synchronized(idList) {
                while (idList.contains(this.id)) {
                    this.id = rand.nextInt(Integer.MAX_VALUE);
                }
                idList.add(this.id);  // Add the unique id to the list
            }
        }
    }

    public void decrement(){
        maxVoters -= 1;
    }
}
