package repository;

// Interface que define o contrato para operações relacionadas com a sondagem de saída (ExitPoll)
public interface IRepository_ExitPoll {

    /**
     * Incrementa o número de votos para a opção A na sondagem de saída.
     */
    void EPincrementA();

    /**
     * Incrementa o número de votos para a opção B na sondagem de saída.
     */
    void EPincrementB();

    /**
     * Fecha a sondagem de saída.
     */
    void EPclose();

    /**
     * Regista a entrada de um eleitor na sondagem de saída.
     * 
     * @param vote O voto do eleitor ('A' ou 'B').
     * @param id O identificador único do eleitor.
     */
    void EPenter(char vote, int id);

    /**
     * Regista a saída de um eleitor da sondagem de saída.
     * 
     * @param id O identificador único do eleitor.
     */
    void EPleave(int id);

    /**
     * Regista que um eleitor foi abordado por um entrevistador na sondagem de saída.
     * 
     * @param id O identificador único do eleitor.
     */
    void EPapproached(int id);

    /**
     * Regista que um eleitor mentiu sobre o seu voto na sondagem de saída.
     * 
     * @param id O identificador único do eleitor.
     * @param vote O voto real do eleitor ('A' ou 'B').
     */
    void EPlied(int id, char vote);

    /**
     * Regista que um eleitor disse a verdade sobre o seu voto na sondagem de saída.
     * 
     * @param id O identificador único do eleitor.
     * @param vote O voto do eleitor ('A' ou 'B').
     */
    void EPtruth(int id, char vote);
}
