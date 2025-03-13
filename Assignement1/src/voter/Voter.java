package voter;
import java.util.ArrayList;
import java.util.Random;

public class Voter extends Thread {

    Random rand = new Random();

    private final static ArrayList<Integer> idList = new ArrayList<>();
    private int id;
    private int vote;
    private int answerPollester;
    private int liePollester;
    private int repeatVoter;
    private static int maxVoters;

    public Voter (int answerPollester, int liePollester, int repeatVoter, int maxVoters) {

        this.id = rand.nextInt(Integer.MAX_VALUE);
        this.vote = rand.nextInt(2);
        this.answerPollester = answerPollester;
        this.liePollester = liePollester;
        this.repeatVoter = repeatVoter;
        this.maxVoters = maxVoters;
    }

    // TODO: Implement monitors in the project
    @Override
    public void run(){
        try{
            Singleton singleton = Singleton.getInstance();
            while(true){
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

    public void reborn(){
        if (rand.nextInt(0,100) < repeatVoter){
            synchronized(idList) {
                while (idList.contains(this.id)) {
                    this.id = rand.nextInt(Integer.MAX_VALUE);
                }
                idList.add(this.id);  // Add the unique id to the list
            }
        }
    }

    public void decrement(){
        this.maxVoters -= 1;
    }
}
