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
    private int partyAodds;
    private  static int maxVoters;
    private Clerk clerk;
    private Station station;
    
    public Voter (int id, char vote, int answerPollester, int liePollester, int repeatVoter, int partyAodds, int maxVoters){
        this.id = id;
        this.vote = vote;
        this.answerPollester = answerPollester;
        this.liePollester = liePollester;
        this.repeatVoter = repeatVoter;
        this.partyAodds = partyAodds;
        this.maxVoters = maxVoters;
    }

    @Override
    public void run(){
        try{
            Singleton singleton = Singleton.getInstance();
            if(singleton.station.checkCapacity()){
                System.out.println("Voter " + id + " is waiting outside");
            }
            // funçao para entrar na station
            Thread.sleep(rand.nextInt(6) + 5);
            if(singleton.clerk.validate(id)){
                System.out.println("Voter " + id + " is voting");
                Thread.sleep(rand.nextInt(5) + 5);
                if(rand.nextInt(100) < partyAodds){
                    singleton.votingBooth.vote(id, 'A');
                    vote = 'A';
                }else{
                    singleton.votingBooth.vote(id, 'B');
                    vote = 'B';
                }     
                System.out.println("Voter " + id + " is leaving");
                // funçao para sair da station

                // funçao para verificar se o pollster vai perguntar
                singleton.pollster.inquire(this);
                // funçao para atribuir novo id ao voter
            }
            else{
                System.out.println("Voter " + id + " already voted");
            }
            decrement();
            if (maxVoters <= 0){
                break;
            }
            reborn();
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

    public int showId(){
        return id;
    }

    public char getVote(){
        return vote;
    }
}
