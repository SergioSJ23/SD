package threads;

import java.util.Random;

public class TPollster extends Thread{

    Random rand = new Random();

    private static TPollster instance;
    private final int votersInquire = 50;
    private final int answerPollester = 50;
    private final int liePollester = 50;

    private TPollster(){
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

    public static TPollster getInstance() {
        if (instance == null) {
           instance = new TPollster();
        }
  
        return instance;
     }
}
