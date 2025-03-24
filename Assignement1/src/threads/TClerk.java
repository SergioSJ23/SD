package threads;

import exitpoll.IExitPoll_Clerk;
import station.IStation_Clerk;
import votesbooth.IVotesBooth_Clerk;

// Classe que representa um funcionário (clerk) como uma thread
public class TClerk extends Thread {

    // Instância única da classe (Singleton)
    private static TClerk instance;

    // Flag para indicar se o limite de votos foi atingido
    private boolean limitReached;
    private final IStation_Clerk station; // Estação de votação
    private final IVotesBooth_Clerk votesBooth; // Urna de votação
    private final IExitPoll_Clerk exitPoll; // Sondagem de saída

    // Construtor privado para evitar instanciação direta (Singleton)
    private TClerk(IStation_Clerk station, IVotesBooth_Clerk votesBooth, IExitPoll_Clerk exitPoll) {
        this.station = station;
        this.votesBooth = votesBooth;
        this.exitPoll = exitPoll;
    }

    // Método principal da thread
    @Override
    public void run() {
        try {
            // Abre a estação de votação
            station.openStation();

            // Loop principal do funcionário
            while (true) {
                // Valida e adiciona votos à estação
                station.validateAndAdd();

                // Verifica se o limite de votos foi atingido
                limitReached = station.countVotes();

                // Se o limite foi atingido, anuncia o fim e fecha a estação
                if (limitReached) {
                    station.announceEnding();
                    station.close();
                    Thread.currentThread().interrupt(); // Restaura o estado de interrupção
                }
            }
        } catch (InterruptedException e) {
            // Processa os últimos votos enquanto houver votos pendentes
            while (station.lastVotes()) {}

            // Notifica a sondagem de saída que a estação está fechada
            exitPoll.stationIsClosed();

            // Obtém os votos finais da urna
            votesBooth.getVotes();

            // Mensagem de interrupção
            System.out.println("Clerk interrupted");
        }
    }

    // Método estático para obter a instância única da classe (Singleton)
    public static TClerk getInstance(IStation_Clerk station, IVotesBooth_Clerk votesBooth, IExitPoll_Clerk exitPoll) {
        if (instance == null) {
            instance = new TClerk(station, votesBooth, exitPoll); // Cria a instância se não existir
        }
        return instance; // Retorna a instância existente
    }
}