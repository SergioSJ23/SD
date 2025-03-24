package repository;

// Interface que define o contrato para operações relacionadas com a urna de votação (VotesBooth)
public interface IRepository_VotesBooth {

    /**
     * Incrementa o número de votos para a opção A.
     */
    void VBincrementA();

    /**
     * Incrementa o número de votos para a opção B.
     */
    void VBincrementB();

    /**
     * Regista o voto de um eleitor.
     * 
     * @param vote O voto do eleitor ('A' ou 'B').
     * @param id O identificador único do eleitor.
     */
    void VBvote(char vote, int id);

    /**
     * Obtém e exibe o número de votos para as opções A e B.
     * 
     * @param votesA Número de votos para a opção A.
     * @param votesB Número de votos para a opção B.
     */
    void VBgetVotes(int votesA, int votesB);
}