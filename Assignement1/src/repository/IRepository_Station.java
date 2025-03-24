package repository;

// Interface que define o contrato para operações relacionadas com a estação de votação (Station)
public interface IRepository_Station {

    /**
     * Abre a estação de votação.
     */
    void Sopen();

    /**
     * Adiciona o ID de um eleitor à lista de IDs.
     * 
     * @param id O identificador único do eleitor.
     */
    void SaddId(int id);

    /**
     * Fecha a estação de votação.
     */
    void Sclose();

    /**
     * Anuncia o fim do dia de eleições.
     */
    void SannounceEnding();

    /**
     * Regista a entrada de um eleitor na estação de votação.
     * 
     * @param id O identificador único do eleitor.
     */
    void Senter(int id);

    /**
     * Regista a saída de um eleitor da estação de votação.
     * 
     * @param id O identificador único do eleitor.
     */
    void Sleave(int id);

    /**
     * Regista a apresentação do ID de um eleitor na estação de votação.
     * 
     * @param id O identificador único do eleitor.
     */
    void Spresent(int id);

    /**
     * Regista a rejeição do ID de um eleitor na estação de votação.
     * 
     * @param id O identificador único do eleitor.
     */
    void Srejected(int id);

    /**
     * Regista a validação do ID de um eleitor na estação de votação.
     * 
     * @param id O identificador único do eleitor.
     */
    void Svalidated(int id);
}