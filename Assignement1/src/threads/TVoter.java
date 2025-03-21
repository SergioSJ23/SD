package threads;

import exitpoll.IExitPoll_Voter;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import station.IStation_Voter;
import votesbooth.IVotesBooth_Voter;

public class TVoter extends Thread {

    private final static ArrayList<Integer> idList = new ArrayList<>();
    private int id;
    private char voteVoter;
    private final int repeatVoter = 10;
    private final int partyOdds = 50;
    private final IStation_Voter station;
    private final IVotesBooth_Voter votesBooth;
    private final IExitPoll_Voter exitPoll;
    private boolean valid;

    public TVoter (int i, IStation_Voter station, IVotesBooth_Voter votesBooth, IExitPoll_Voter exitPoll){
        this.id = i;
        this.station = station;
        this.votesBooth = votesBooth;
        this.exitPoll = exitPoll;
    }

    @Override
    public void run() {
        try {
            while (true) {
                idList.add(this.id);
                chooseVote();
                station.enterStation(this.id);
                this.valid = station.present(this.id);
                if (this.valid) {
                    votesBooth.vote(this.voteVoter);
                    System.out.println("Voter " + this.id + " voted for party " + this.voteVoter);
                }
                station.leaveStation(this.id);
                exitPoll.inquire(this.id, this.voteVoter);
                reborn();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void reborn() {
        if (ThreadLocalRandom.current().nextInt(0, 100) >= this.repeatVoter) {
            synchronized (idList) {
                this.id = idList.get(idList.size() - 1) + 1;
                idList.add(this.id); // Ensures the list is updated with the new ID
            }
            System.out.println("NEW");
        } else {
            System.out.println("REPEATED");
        }
    }

    private void chooseVote(){
        if (ThreadLocalRandom.current().nextInt(0, 100) >= this.partyOdds) {
            this.voteVoter = 'A';
        } else {
            this.voteVoter = 'B';
        }
    }
}