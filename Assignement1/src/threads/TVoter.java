package threads;

import exitpoll.IExitPoll_Voter;
import java.util.concurrent.ThreadLocalRandom;
import station.IStation_Voter;
import votersId.IVoterId_Voter;
import votesbooth.IVotesBooth_Voter;

// Classe que representa um eleitor (voter) como uma thread
public class TVoter extends Thread {

    private int id; // ID do eleitor
    private char vote; // Voto do eleitor ('A' ou 'B')
    private final int partyOdds = 50; // Probabilidade de votar em 'A' ou 'B' (50%)
    private final IStation_Voter station; // Estação de votação
    private final IVotesBooth_Voter votesBooth; // Urna de votação
    private final IExitPoll_Voter exitPoll; // Sondagem de saída
    private final IVoterId_Voter voterId; // Gerador de IDs de eleitores

    // Construtor da classe TVoter
    private TVoter(IStation_Voter station, IVotesBooth_Voter votesBooth, IExitPoll_Voter exitPoll, IVoterId_Voter voterId) {
        this.station = station;
        this.votesBooth = votesBooth;
        this.exitPoll = exitPoll;
        this.voterId = voterId;
    }

    // Método principal da thread
    @Override
    public void run() {
        try {
            id = 0;
            while (true) {
                id = voterId.reborn(id);
                chooseVote();

                // Entra na estação de votação
                station.enterStation(this.id);

                // Verifica se a thread foi interrompida
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }

                // Verifica se o eleitor está presente na estação
                if (station.present(this.id)) {
                    // Vota na urna
                    votesBooth.vote(this.vote, this.id);
                }

                // Sai da estação de votação
                station.leaveStation(this.id);

                // Entra na sondagem de saída
                exitPoll.enterExitPoll(this.vote, this.id);

                // Sai da sondagem de saída
                exitPoll.leaveExitPoll(this.id);
            }
        } catch (InterruptedException e) {
            System.err.println(e); // Log da exceção
        }
        System.out.println("Voter interrupted"); // Mensagem de interrupção
    }

    // Método para escolher o voto ('A' ou 'B') com base numa probabilidade
    private void chooseVote() {
        if (ThreadLocalRandom.current().nextInt(0, 100) >= this.partyOdds) {
            this.vote = 'A'; // Vota em 'A' com 50% de probabilidade
        } else {
            this.vote = 'B'; // Vota em 'B' com 50% de probabilidade
        }
    }

    // Método estático para obter a instância única da classe (Singleton)
    public static TVoter getInstance(IStation_Voter station, IVotesBooth_Voter votesBooth, IExitPoll_Voter exitPoll, IVoterId_Voter voterId) {
        return new TVoter(station, votesBooth, exitPoll, voterId); // Always return a new instance
    }
    
}