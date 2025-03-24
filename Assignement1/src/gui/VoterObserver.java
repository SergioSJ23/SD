package gui;

// Interface que define o contrato para observadores de eleitores (voters)
public interface VoterObserver {

    /**
     * Método para atualizar o estado de um eleitor.
     * 
     * @param voterId O identificador único do eleitor.
     * @param state O estado atual do eleitor (ex: "Entrance", "Validated", "Voted", etc.).
     * @param isClosed Indica se a estação de votação está fechada.
     */
    void updateVoterState(int voterId, String state, boolean isClosed);
}