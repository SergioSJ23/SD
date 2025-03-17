package monitoring;

import java.util.Random;
import threads.TVoter;

public class IPollster {

    Random rand = new Random();

    private static IPollster instance;
    private int votersInquire;
    private int answerPollester;
    private int liePollester;

    public IPollster(int votersInquire, int answerPollester, int liePollester){
        this.votersInquire = votersInquire;
        this.answerPollester = answerPollester; 
        this.liePollester = liePollester;
    }

    public void inquire(TVoter voter){
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

    public static IPollster getInstance(int votersInquire, int answerPollester, int liePollester) {
        if (instance == null) {
           instance = new IPollster(votersInquire, answerPollester, liePollester);
        }
  
        return instance;
     }
}
