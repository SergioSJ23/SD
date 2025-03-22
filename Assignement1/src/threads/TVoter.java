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
            while (votesBooth.isVotingComplete()) {                
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
                if (votesBooth.isVotingComplete())
                    reborn();
                
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
            this.voteVoter = 'A';
        } else {
            this.voteVoter = 'B';
        }
    }
}