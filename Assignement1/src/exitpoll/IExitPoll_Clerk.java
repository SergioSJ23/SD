package exitpoll;

// Interface que define o contrato para um funcionário (clerk) interagir com a sondagem de saída
public interface IExitPoll_Clerk {

    /**
     * Método para notificar que a estação de votação está fechada.
     */
    void stationIsClosed();
}