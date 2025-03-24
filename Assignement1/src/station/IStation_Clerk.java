package station;

// Interface que define o contrato para um funcionário (clerk) interagir com a estação de votação
public interface IStation_Clerk {

    /**
     * Método para fechar a estação de votação.
     */
    void close();

    /**
     * Método para validar e adicionar o voto de um eleitor.
     * 
     * @throws InterruptedException Se a thread for interrompida durante a validação.
     */
    void validateAndAdd() throws InterruptedException;

    /**
     * Método para processar os últimos votos pendentes.
     * 
     * @return true se ainda houver votos para processar, false caso contrário.
     */
    boolean lastVotes();

    /**
     * Método para verificar se o limite de votos foi atingido.
     * 
     * @return true se o limite de votos foi atingido, false caso contrário.
     */
    boolean countVotes();

    /**
     * Método para abrir a estação de votação.
     */
    void openStation();

    /**
     * Método para anunciar o fim do dia de eleições.
     */
    void announceEnding();
}