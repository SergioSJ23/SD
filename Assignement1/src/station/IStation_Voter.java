package station;

// Interface que define o contrato para um eleitor (voter) interagir com a estação de votação
public interface IStation_Voter {

    /**
     * Método para um eleitor entrar na estação de votação.
     * 
     * @param id O identificador único do eleitor.
     * @throws InterruptedException Se a thread for interrompida enquanto espera.
     */
    void enterStation(int id) throws InterruptedException;

    /**
     * Método para um eleitor sair da estação de votação.
     * 
     * @param id O identificador único do eleitor.
     */
    void leaveStation(int id);

    /**
     * Método para verificar se o eleitor está presente na estação e pode votar.
     * 
     * @param id O identificador único do eleitor.
     * @return true se o eleitor estiver presente e puder votar, false caso contrário.
     */
    boolean present(int id);
}