package threads;

import exitpoll.IExitPoll_Voter;
import gui.VoterObserver;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import station.IStation_Voter;
import votersId.IVoterId_Voter;
import votesbooth.IVotesBooth_Voter;

public class TVoter extends Thread {

    private final static ArrayList<Integer> idList = new ArrayList<>();
    private int id;
    private char vote;
    private final int repeatVoter = 10;
    private final int partyOdds = 50;
    private final IStation_Voter station;
    private final IVotesBooth_Voter votesBooth;
    private final IExitPoll_Voter exitPoll;
    private final IVoterId_Voter voterId;
    private VoterObserver observer;


    public TVoter (IStation_Voter station, IVotesBooth_Voter votesBooth, IExitPoll_Voter exitPoll, IVoterId_Voter voterId) {
        this.station = station;
        this.votesBooth = votesBooth;
        this.exitPoll = exitPoll;
        this.voterId = voterId;
        
    }

    public void registerObserver(VoterObserver observer) {
        this.observer = observer;
    }

    private void notifyObserver(String state) {
        if (this.observer != null) {
            observer.updateVoterState(this.id, state);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                id = voterId.reborn();
                chooseVote();
                //notifyObserver("Entrance");
                station.enterStation(this.id);
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                if (station.present(this.id)) {
                    //notifyObserver("Voting Station");
                    votesBooth.vote(this.vote, this.id);
                    //notifyObserver("Voting Booth");
                }
                station.leaveStation(this.id);
                notifyObserver("Exit");
                exitPoll.enterExitPoll(this.vote, this.id);
                exitPoll.leaveExitPoll(this.id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Voter " + this.id + " is done");
    }



    private void chooseVote(){
        if (ThreadLocalRandom.current().nextInt(0, 100) >= this.partyOdds) {
            this.vote = 'A';
        } else {
            this.vote = 'B';
        }
    }
}