package votersId;

// Interface que define o contrato para gerar ou reutilizar um ID de eleitor
public interface IVoterId_Voter {

    /**
     * Método para gerar ou reutilizar um ID de eleitor.
     * 
     * @return Um número inteiro que representa o ID do eleitor.
     */
    int reborn();
}