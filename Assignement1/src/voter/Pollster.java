package voter;

import java.util.Random;

public class Pollster {

    Random rand = new Random();

    private static Pollster instance;
    private int votersInquire;
    private int answerPollester;
    private int liePollester;

    public Pollster(int votersInquire, int answerPollester, int liePollester){
        this.votersInquire = votersInquire;
        this.answerPollester = answerPollester; 
        this.liePollester = liePollester;
    }

    public void inquire(Voter voter){
        if(rand.nextInt(100) < votersInquire){
            if(rand.nextInt(100) < answerPollester){
                if(rand.nextInt(100) > liePollester){   
                    System.out.println("Pollster: Voter " + voter.showId() + " is voting for party " + voter.getVote() + " (true)");
                }else{
                    if(voter.getVote() == 'A'){
                        System.out.println("Pollster: Voter " + voter.showId() + " is voting for party B (lie)");
                    }else{
                        System.out.println("Pollster: Voter " + voter.showId() + " is voting for party A (lie)");
                    }
                }
            }else{
                System.out.println("Pollster: Voter " + voter.showId() + " doesn't want to answer");
            }
        }
    }

    public static Pollster getInstance(int votersInquire, int answerPollester, int liePollester) {
        if (instance == null) {
           instance = new Pollster(votersInquire, answerPollester, liePollester);
        }
  
        return instance;
     }
}
