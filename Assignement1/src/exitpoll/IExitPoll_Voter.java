package exitpoll;

// Interface que define o contrato para um eleitor (voter) interagir com a sondagem de saída
public interface IExitPoll_Voter {

    /**
     * Método para um eleitor entrar na sondagem de saída.
     * 
     * @param vote O voto do eleitor ('A' ou 'B').
     * @param voterId O identificador único do eleitor.
     */
    void enterExitPoll(char vote, int voterId);

    /**
     * Método para um eleitor sair da sondagem de saída.
     * 
     * @param voterId O identificador único do eleitor.
     */
    void leaveExitPoll(int voterId);
}