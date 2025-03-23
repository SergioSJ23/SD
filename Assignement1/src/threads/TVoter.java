package threads;

import exitpoll.IExitPoll_Voter;
import gui.VoterObserver;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import station.IStation_Voter;
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
    private VoterObserver observer;

    public TVoter (IStation_Voter station, IVotesBooth_Voter votesBooth, IExitPoll_Voter exitPoll){
        this.station = station;
        this.votesBooth = votesBooth;
        this.exitPoll = exitPoll;
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
                reborn();
                idList.add(this.id);
                chooseVote();
                notifyObserver("Entrance");
                station.enterStation(this.id);
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Voter " + this.id + " interrupted. Exiting...");
                    break;
                }
                if (station.present(this.id)) {
                    notifyObserver("Voting Station");
                    votesBooth.vote(this.vote, this.id);
                    notifyObserver("Voting Booth");
                    System.out.println("Voter " + this.id + " voted for party " + this.vote);
                }
                station.leaveStation(this.id);
                notifyObserver("Exit");
                exitPoll.enterExitPoll(this.vote, this.id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Voter " + this.id + " is done");
    }

    private void reborn() {
        if (ThreadLocalRandom.current().nextInt(0, 100) >= this.repeatVoter) {
            // Gera um novo ID aleatório e adiciona à lista
            int newId;
            synchronized (idList) {
                do {
                    newId = ThreadLocalRandom.current().nextInt(1000, 10000); // IDs de 4 dígitos
                } while (idList.contains(newId)); // Garante que o ID seja único
                idList.add(newId);
                this.id = newId;
            }
            System.out.println("NEW Voter " + this.id + " created");
        } else {
            // Reutiliza um ID existente aleatório da lista
            synchronized (idList) {
                if (!idList.isEmpty()) {
                    int randomIndex = ThreadLocalRandom.current().nextInt(idList.size());
                    this.id = idList.get(randomIndex);
                    System.out.println("REUSED Voter " + this.id + " reborn");
                } else {
                    // Caso a lista esteja vazia, gera um novo ID
                    int newId = ThreadLocalRandom.current().nextInt(1000, 10000);
                    idList.add(newId);
                    this.id = newId;
                    System.out.println("NEW Voter " + this.id + " created (fallback)");
                }
            }
        }
    }

    private void chooseVote(){
        if (ThreadLocalRandom.current().nextInt(0, 100) >= this.partyOdds) {
            this.vote = 'A';
        } else {
            this.vote = 'B';
        }
    }
}