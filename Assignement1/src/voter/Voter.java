package voter;
import java.util.Random;



public class Voter extends Thread {

    Random rand = new Random();

    private int id;
    private int vote;
    private int answerPollester;
    private int liePollester;
    private int repeatVoter;
    private Clerk clerk;
    private Station station;

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
            if(singleton.station.checkCapacity()){
                System.out.println("Voter " + id + " is waiting outside");
            }

            Thread.sleep(rand.nextInt(5) + 5);
            if(singleton.clerk.validate(id)){
                System.out.println("Voter " + id + " is voting");
                Thread.sleep(new Random().nextInt(5) + 5);
                singleton.clerk.vote(id, vote);
                System.out.println("Voter " + id + " voted");
            }
            else{
                System.out.println("Voter " + id + " is leaving");
            }
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    
}
