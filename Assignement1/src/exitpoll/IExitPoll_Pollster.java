package exitpoll;

// Interface que define o contrato para um entrevistador (pollster) realizar a sondagem de saída
public interface IExitPoll_Pollster {

    /**
     * Método para o entrevistador realizar a sondagem.
     * 
     * @throws InterruptedException Se a thread for interrompida durante a espera.
     */
    void inquire() throws InterruptedException;
}